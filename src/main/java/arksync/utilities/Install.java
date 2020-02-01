package arksync.utilities;


import arksync.Main;
import arksync.dto.ArkPlayerData;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Install
{
    public static void uploadServer(String localGamePath, String cloudDirectoryPath)
    {
        uploadEmptyServerDirectory(cloudDirectoryPath);
        HashMap<File, File> cloudToLocalMapDirectoryMapping =
                Analysis.analyseDirectories(new File(localGamePath), new File(cloudDirectoryPath), true);
        ArkPlayerData arkPlayerData = Analysis.obtainPlayerDataMapping(cloudToLocalMapDirectoryMapping, cloudDirectoryPath);
        Main.setArkPlayerData(arkPlayerData);
        Main.setCloudToLocalMapDirectoryMapping(cloudToLocalMapDirectoryMapping);

        uploadMaps(cloudToLocalMapDirectoryMapping);
        uploadPlayerData(cloudToLocalMapDirectoryMapping, arkPlayerData);
        uploadFiles(Main.getLocalObelisk(), Main.getCloudObelisk(), null);
    }

    private static void uploadEmptyServerDirectory(String cloudDirectoryPath)
    {
        String arkSyncDirectoryPath = "C:\\Program Files\\ArkSync\\ArkSync";
        try
        {
            FileUtils.copyDirectory(new File(arkSyncDirectoryPath), new File(cloudDirectoryPath));
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private static void uploadMaps(HashMap<File, File> cloudToLocalMapDirectoryMapping)
    {
        for(File cloudDirectory : cloudToLocalMapDirectoryMapping.keySet())
        {
            File localMapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                    cloudToLocalMapDirectoryMapping.get(cloudDirectory).listFiles((dir, name) ->
                            name.toLowerCase().endsWith(".ark"))))).get(0);
            File cloudMapFile = new File(cloudDirectory.getAbsolutePath() + "\\" + localMapFile.getName());

            try
            {
                try
                {
                    Files.copy(localMapFile.toPath(), cloudMapFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Uploaded map file: " + localMapFile.getName());
                }
                catch (FileSystemException fse)
                {
                    InputStream sourceStream = new FileInputStream(localMapFile);
                    Files.copy(sourceStream, cloudMapFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Uploaded map file: " + localMapFile.getName());
                    sourceStream.close();
                    Files.copy(cloudMapFile.toPath(), localMapFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println("Failed to upload map file: " + localMapFile.getName());
            }
        }
    }

    private static void uploadPlayerData(HashMap<File, File> cloudToLocalMapDirectoryMapping, ArkPlayerData arkPlayerData)
    {
        for(File localDirectory : cloudToLocalMapDirectoryMapping.values())
        {
            uploadFiles(localDirectory, arkPlayerData.getArkProfile(), ".arkprofile");
            uploadFiles(localDirectory, arkPlayerData.getArkTribe(), ".arktribe");
            uploadFiles(localDirectory, arkPlayerData.getArkTributeTribe(), ".arktributetribe");
            uploadFiles(localDirectory, arkPlayerData.getProfileBak(), ".profilebak");
            uploadFiles(localDirectory, arkPlayerData.getTribeBak(), ".tribebak");
        }
    }

    private static void uploadFiles(File fromDirectory, File toDirectory, String filter)
    {
        File[] fromDirectoryFiles;
        if(filter == null)
        {
            fromDirectoryFiles = fromDirectory.listFiles();
        } else {
            fromDirectoryFiles = Objects.requireNonNull(fromDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(filter)));
        }
        for(File file : Objects.requireNonNull(fromDirectoryFiles))
        {
            File toFile = new File(toDirectory.getAbsolutePath() + "\\" + file.getName());
            if(!toFile.exists())
            {
                try
                {
                    try
                    {
                        Files.copy(file.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Uploaded file: " + file.getName());
                    }
                    catch (FileSystemException fse)
                    {
                        InputStream sourceStream = new FileInputStream(file);
                        Files.copy(sourceStream, toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Uploaded file: " + file.getName());
                        sourceStream.close();
                        Files.copy(toFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                catch (IOException missingDirectories)
                {
                    System.out.println("Failed to upload file: " + file.getName());
                }
            }
        }
    }

}