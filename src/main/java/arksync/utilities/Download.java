package arksync.utilities;

import arksync.dto.ArkPlayerData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Download
{

    public static void downloadFromCloud(HashMap<File, File> cloudToLocalMapDirectoryMapping, ArkPlayerData arkPlayerData,
                                         File cloudObelisk, File localObelisk)
    {
        downloadMaps(cloudToLocalMapDirectoryMapping);
        downloadPlayerData(cloudToLocalMapDirectoryMapping, arkPlayerData);
        downloadFiles(cloudObelisk, localObelisk);
    }

    private static void downloadMaps(HashMap<File, File> cloudToLocalMapDirectoryMapping)
    {
        for(File cloudDirectory : cloudToLocalMapDirectoryMapping.keySet())
        {
            File cloudMapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                    cloudDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ark"))))).get(0);
            File localMapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                    cloudToLocalMapDirectoryMapping.get(cloudDirectory).listFiles((dir, name) ->
                            name.toLowerCase().endsWith(".ark"))))).get(0);
            try
            {
                Files.copy(cloudMapFile.toPath(), localMapFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded map file: " + cloudMapFile.getName());
            }
            catch (IOException ioe)
            {
                System.out.println("Failed to download map file: " + cloudMapFile.getName());
            }
        }
    }

    private static void downloadPlayerData(HashMap<File, File> cloudToLocalMapDirectoryMapping, ArkPlayerData arkPlayerData)
    {
        for(File localDirectory : cloudToLocalMapDirectoryMapping.values())
        {
            downloadFiles(arkPlayerData.getArkProfile(), localDirectory);
            downloadFiles(arkPlayerData.getArkTribe(), localDirectory);
            downloadFiles(arkPlayerData.getArkTributeTribe(), localDirectory);
            downloadFiles(arkPlayerData.getProfileBak(), localDirectory);
            downloadFiles(arkPlayerData.getTribeBak(), localDirectory);
        }
    }

    private static void downloadFiles(File fromDirectory, File toDirectory)
    {
        for(File file : Objects.requireNonNull(fromDirectory.listFiles()))
        {
            File toFile = new File(toDirectory.getAbsolutePath() + "\\" + file.getName());
            try
            {
                Files.createDirectories(toFile.toPath().getParent());
                Files.copy(file.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded file: " + file.getName());
            }
            catch (IOException missingDirectories)
            {
                System.out.println("Creating missing directories for: " + file.getName());
                try
                {
                    Files.createDirectories(toFile.toPath().getParent());
                    Files.copy(file.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException ioe)
                {
                    System.out.println("Failed to download file: " + file.getName());
                }
            }
        }
    }

}
