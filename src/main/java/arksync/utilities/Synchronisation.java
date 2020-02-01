package arksync.utilities;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Synchronisation
{

    public static void synchroniseDirectories(File directoryA, File directoryB, String filter)
    {
        HashMap<String, File> locAFiles = new HashMap<>();
        HashMap<String, File> locBFiles = new HashMap<>();

        if(filter == null)
        {
            for (File file : Objects.requireNonNull(directoryA.listFiles()))
            {
                locAFiles.put(file.getName(), file);
            }
            for (File file : Objects.requireNonNull(directoryB.listFiles()))
            {
                locBFiles.put(file.getName(), file);
            }
        } else {
            for (File file : Objects.requireNonNull(directoryA.listFiles((dir, name) -> name.toLowerCase().endsWith(filter))))
            {
                locAFiles.put(file.getName(), file);
            }
            for (File file : Objects.requireNonNull(directoryB.listFiles((dir, name) -> name.toLowerCase().endsWith(filter))))
            {
                locBFiles.put(file.getName(), file);
            }
        }


        List<String> matchingFiles = new ArrayList<>();
        for(File fileA : locAFiles.values())
        {
            if(locBFiles.containsKey(fileA.getName()))
            {
                matchingFiles.add(fileA.getName());
            }
        }

        HashMap<String, File> allFiles = new HashMap<>();
        allFiles.putAll(locBFiles);
        allFiles.putAll(locAFiles);

        for(File file : allFiles.values())
        {
            File fileA = locAFiles.get(file.getName());
            File fileB = locBFiles.get(file.getName());
            if (matchingFiles.contains(file.getName()))
            {
                handleMatchingFiles(fileA, fileB);
            }
            else if (locAFiles.containsKey(file.getName()))
            {
                handleMissingFile(directoryA, directoryB, fileA);
            }
            else if (locBFiles.containsKey(file.getName()))
            {
                handleMissingFile(directoryA, directoryB, fileB);
            }
        }
    }

    private static void handleMatchingFiles(File fileA, File fileB)
    {
        System.out.println("Handling matching file: " + fileA.getName());
        if (fileA.lastModified() < fileB.lastModified())
        {
            try
            {
                try
                {
                    if(fileB.canWrite() && fileA.canWrite())
                    {
                        Files.copy(fileB.toPath(), fileA.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File updated : " + fileA.getAbsolutePath());
                    }
                }
                catch (FileSystemException fse)
                {
                    if(fileB.canWrite() && fileA.canWrite())
                    {
                        InputStream sourceStream = new FileInputStream(fileB);
                        Files.copy(sourceStream, fileA.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File updated : " + fileA.getAbsolutePath());
                        sourceStream.close();
                        Files.copy(fileA.toPath(), fileB.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println("Failed to update file!");
            }
        }
        else if (fileA.lastModified() > fileB.lastModified())
        {
            try
            {
                try
                {
                    if(fileB.canWrite() && fileA.canWrite())
                    {
                        Files.copy(fileA.toPath(), fileB.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File updated : " + fileB.getAbsolutePath());
                    }
                }
                catch (FileSystemException fse)
                {
                    if(fileB.canWrite() && fileA.canWrite())
                    {
                        InputStream sourceStream = new FileInputStream(fileA);
                        Files.copy(sourceStream, fileB.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File updated : " + fileB.getAbsolutePath());
                        sourceStream.close();
                        Files.copy(fileB.toPath(), fileA.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println("Failed to update file!");
            }
        } else {
            System.out.println("Both files are up to date");
        }
    }

    private static void handleMissingFile(File fromDirectory, File toDirectory, File file)
    {
        if (fromDirectory.lastModified() < toDirectory.lastModified())
        {
            if(file.delete())
            {
                System.out.println("File removed: " + file.getName());
            }
            else
            {
                System.out.println("Failed to remove file " + file.getName());
            }
        }
        else
        {
            try
            {
                File outputFile = new File(toDirectory.getAbsolutePath() + "\\" + file.getName());
                try
                {
                    if(file.canWrite() && outputFile.canWrite())
                    {
                        Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File copied");
                    }
                }
                catch (FileSystemException fse)
                {
                    if(file.canWrite() && outputFile.canWrite())
                    {
                        InputStream sourceStream = new FileInputStream(file);
                        Files.copy(sourceStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File copied");
                        sourceStream.close();
                        Files.copy(outputFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println("Failed to copy file!");
            }
        }
    }

}
