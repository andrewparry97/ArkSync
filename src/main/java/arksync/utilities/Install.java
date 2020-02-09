package arksync.utilities;


import arksync.Main;
import arksync.dto.ArkPlayerData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        File serverDirectory = new File(cloudDirectoryPath);
        File obeliskDirectory = new File(cloudDirectoryPath + "\\obelisk");
        File mapsDirectory = new File(cloudDirectoryPath + "\\maps");
        File playerDataDirectory = new File(cloudDirectoryPath + "\\player_data");
        if(serverDirectory.mkdir() && obeliskDirectory.mkdir() && mapsDirectory.mkdir() && playerDataDirectory.mkdir())
        {
            System.out.println("New directory created");
        } else {
            System.out.println("Failed to create new directory!");
        }
        File arkProfileDirectory = new File(playerDataDirectory.getAbsolutePath() + "\\arkprofile");
        File arkTribeDirectory = new File(playerDataDirectory.getAbsolutePath() + "\\arktribe");
        File arkTributeTribeDirectory = new File(playerDataDirectory.getAbsolutePath() + "\\arktributetribe");
        File arkProfileBakDirectory = new File(playerDataDirectory.getAbsolutePath() + "\\profilebak");
        File arkTribeBakDirectory = new File(playerDataDirectory.getAbsolutePath() + "\\tribebak");
        if(arkProfileDirectory.mkdir() && arkTribeDirectory.mkdir() && arkTributeTribeDirectory.mkdir() &&
                arkProfileBakDirectory.mkdir() && arkTribeBakDirectory.mkdir())
        {
            System.out.println("player_data directory created");
        } else {
            System.out.println("Failed to create player_data directory!");
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
                Date date = new Date(localMapFile.lastModified());
                InputStream sourceStream = new FileInputStream(localMapFile);
                Files.copy(sourceStream, cloudMapFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                sourceStream.close();
                if(localMapFile.setLastModified(date.getTime()) && cloudMapFile.setLastModified(date.getTime()))
                {
                    System.out.println("Copied map file: " + localMapFile.getName());
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println("Failed to copy map file: " + localMapFile.getName());
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
            if(!toFile.exists() || file.lastModified() > toFile.lastModified())
            {
                try
                {
                    Date date = new Date(file.lastModified());
                    InputStream sourceStream = new FileInputStream(file);
                    Files.copy(sourceStream, toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    sourceStream.close();
                    if(toFile.setLastModified(date.getTime()) && file.setLastModified(date.getTime()))
                    {
                        System.out.println("Copied file: " + file.getName());
                    }
                }
                catch (IOException missingDirectories)
                {
                    System.out.println("Failed to copy file: " + file.getName());
                }
            }
        }
    }

}
