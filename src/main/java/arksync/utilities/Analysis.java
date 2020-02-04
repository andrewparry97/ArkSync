package arksync.utilities;

import arksync.dto.ArkPlayerData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Analysis
{

    private static List<String> mapNames = new ArrayList<>();

    public static HashMap<File, File> analyseDirectories(File localGameDirectory, File cloudDirectory, boolean install)
    {
        File localSaveDirectory = new File(localGameDirectory.getAbsolutePath() + "\\LocalState\\Saved");
        List<File> localMapDirectories = new ArrayList<>(getLocalMapDirectories(localSaveDirectory));
        List<File> cloudMapDirectories = new ArrayList<>(getCloudMapDirectories(cloudDirectory));

        return mapDirectories(localMapDirectories, cloudMapDirectories, install);
    }

    private static List<File> getLocalMapDirectories(File localSaveDirectory)
    {
        File localSavedMapsDirectory = new File(localSaveDirectory.getAbsolutePath() + "\\Maps");
        List<File> mapDirectories = new ArrayList<>();
        for(File mapDirectory : Objects.requireNonNull(localSavedMapsDirectory.listFiles()))
        {
            while(!mapDirectory.getName().contains("SavedArks"))
            {
                mapDirectory = Objects.requireNonNull(mapDirectory.listFiles())[0];
            }
            File mapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                    mapDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ark"))))).get(0);
            mapDirectories.add(mapDirectory);
            mapNames.add(FilenameUtils.getBaseName(mapFile.getName()));
        }
        return mapDirectories;
    }

    private static List<File> getCloudMapDirectories(File cloudDirectory)
    {
        File cloudMapsDirectory = new File(cloudDirectory.getAbsolutePath() + "\\maps");
        for(String mapName : mapNames)
        {
            File mapDirectory = new File(cloudMapsDirectory.getAbsolutePath() + "\\" + mapName);
            if(!mapDirectory.exists())
            {
                if(mapDirectory.mkdir())
                {
                    System.out.println("Created cloud directory for " + mapName);
                } else {
                    System.out.println("Failed to create cloud directory for " + mapName);
                }
            }
        }
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(cloudMapsDirectory.listFiles())));
    }

    private static HashMap<File, File> mapDirectories(List<File> localMapDirectories, List<File> cloudMapDirectories,
                                                      boolean install)
    {
        HashMap<File, File> cloudToLocalMapDirectoryMapping = new HashMap<>();
        for(File cloudDirectory : cloudMapDirectories)
        {
            File matchedDirectory = null;
            File cloudMapFile = null;
            if(!install)
            {
                 List<File> cloudMapFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                        cloudDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ark")))));
                 if(!cloudMapFiles.isEmpty())
                 {
                     cloudMapFile = cloudMapFiles.get(0);
                 }
            }
            for(File localDirectory : localMapDirectories)
            {
                File localMapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                        localDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ark"))))).get(0);

                if(!install && cloudDirectory.getName().equals(FilenameUtils.getBaseName(localMapFile.getName())))
                {
                    matchedDirectory = localDirectory;
                    if(cloudMapFile == null)
                    {
                        cloudMapFile = new File(cloudDirectory.getAbsolutePath() + "\\"
                                + localMapFile.getName());
                        try
                        {
                            FileUtils.copyFile(localMapFile, cloudMapFile);
                            System.out.println("Uploaded map file " + localMapFile.getName());
                        }
                        catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                        }
                    }
                    break;
                }
                else if(install && localMapFile.getName().contains(cloudDirectory.getName()))
                {
                    matchedDirectory = localDirectory;
                    break;
                }
            }
            if(matchedDirectory != null)
            {
                cloudToLocalMapDirectoryMapping.put(cloudDirectory, matchedDirectory);
                System.out.println(cloudDirectory.getName() + " mapped to " + matchedDirectory.getName());
            } else {
                System.out.println("Required folder structure does not exist for " + cloudDirectory.getName());
            }
        }
        return cloudToLocalMapDirectoryMapping;
    }

    public static ArkPlayerData obtainPlayerDataMapping(HashMap<File, File> cloudToLocalMapDirectoryMapping,
                                               String cloudDirectoryPath)
    {
        ArkPlayerData arkPlayerData = new ArkPlayerData(cloudDirectoryPath);

        for(File file : cloudToLocalMapDirectoryMapping.values())
        {
            System.out.println(file.getName() + " mapped to " + arkPlayerData.getArkProfile());
            System.out.println(file.getName() + " mapped to " + arkPlayerData.getArkTribe());
            System.out.println(file.getName() + " mapped to " + arkPlayerData.getArkTributeTribe());
            System.out.println(file.getName() + " mapped to " + arkPlayerData.getProfileBak());
            System.out.println(file.getName() + " mapped to " + arkPlayerData.getTribeBak());
        }

        return arkPlayerData;
    }

}
