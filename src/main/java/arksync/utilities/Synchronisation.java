package arksync.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Logger;

public class Synchronisation
{

    private static final Logger updateLog = Logger.getLogger(Synchronisation.class.getName());

    public static void synchroniseDirectories(File directoryA, File directoryB, String filter)
    {
        HashMap<String, File> locAFiles = new HashMap<>();
        HashMap<String, File> locBFiles = new HashMap<>();

        if(filter == null)
        {
            for (File file : Objects.requireNonNull(directoryA.listFiles()))
            {
                if(file.getName().contains("conflicted copy"))
                {
                    file = handleConflictedCopy(file);
                }
                locAFiles.put(file.getName(), file);
            }
            for (File file : Objects.requireNonNull(directoryB.listFiles()))
            {
                if(file.getName().contains("conflicted copy"))
                {
                    file = handleConflictedCopy(file);
                }
                locBFiles.put(file.getName(), file);
            }
        } else {
            for (File file : Objects.requireNonNull(directoryA.listFiles((dir, name) -> name.toLowerCase().endsWith(filter))))
            {
                if(file.getName().contains("conflicted copy"))
                {
                    file = handleConflictedCopy(file);
                }
                locAFiles.put(file.getName(), file);
            }
            for (File file : Objects.requireNonNull(directoryB.listFiles((dir, name) -> name.toLowerCase().endsWith(filter))))
            {
                if(file.getName().contains("conflicted copy"))
                {
                    file = handleConflictedCopy(file);
                }
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
                handleMissingFile(directoryB, directoryA, fileB);
            }
        }
    }

    private static void handleMatchingFiles(File fileA, File fileB)
    {
        updateLog.info("Handling matching file: " + fileA.getName());
        if (fileA.lastModified() < fileB.lastModified())
        {
            synchroniseFiles(fileB, fileA);
        }
        else if (fileA.lastModified() > fileB.lastModified())
        {
            synchroniseFiles(fileA, fileB);
        } else {
            updateLog.info("Both files are up to date");
        }
    }

    private static void synchroniseFiles(File fileA, File fileB)
    {
        try
        {
            Date date = new Date(fileA.lastModified());
            InputStream sourceStream = new FileInputStream(fileA);
            Files.copy(sourceStream, fileB.toPath(), StandardCopyOption.REPLACE_EXISTING);
            sourceStream.close();
            if(fileA.setLastModified(date.getTime()) && fileB.setLastModified(date.getTime()))
            {
                updateLog.info("File updated : " + fileB.getAbsolutePath());
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            updateLog.info("Failed to update file!");
        }
    }

    private static void handleMissingFile(File fromDirectory, File toDirectory, File file)
    {
        if (fromDirectory.lastModified() < toDirectory.lastModified())
        {
            if(file.delete())
            {
                updateLog.info("File removed: " + file.getName());
            }
            else
            {
                updateLog.info("Failed to remove file " + file.getName());
            }
        } else {
            try
            {
                File outputFile = new File(toDirectory.getAbsolutePath() + "\\" + file.getName());
                Date date = new Date(file.lastModified());
                InputStream sourceStream = new FileInputStream(file);
                Files.copy(sourceStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                sourceStream.close();
                if(outputFile.setLastModified(date.getTime()) && file.setLastModified(date.getTime()))
                {
                    updateLog.info("File copied");
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                updateLog.info("Failed to copy file!");
            }
        }
    }

    private static File handleConflictedCopy(File file)
    {
        updateLog.info("Resolving conflict!");
        StringBuilder unwantedString = new StringBuilder();
        boolean test = false;
        for(char character : file.getAbsolutePath().toCharArray())
        {
            if(test)
            {
                if(character == '.')
                {
                    test = false;
                } else {
                    unwantedString.append(character);
                }
            }
            else if(Character.isWhitespace(character))
            {
                test = true;
                unwantedString.append(character);
            }
        }
        File replacement = new File(file.getAbsolutePath().replace(unwantedString.toString(), ""));
        try
        {
            Files.copy(file.toPath(), replacement.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ioe)
        {
            updateLog.info("Issue encountered whilst handling conflicted copy!");
        }
        if(file.delete())
        {
            updateLog.info("File removed: " + file.getName());
        }
        else
        {
            updateLog.info("Failed to remove file " + file.getName());
        }

        return replacement;
    }

}
