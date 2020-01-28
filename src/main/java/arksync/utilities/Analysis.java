package arksync.utilities;

import arksync.dto.ArkPlayerData;

import java.io.File;
import java.util.*;

public class Analysis
{

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
            mapDirectories.add(mapDirectory);
        }

        return mapDirectories;
    }

    private static List<File> getCloudMapDirectories(File cloudDirectory)
    {
        File cloudMapsDirectory = new File(cloudDirectory.getAbsolutePath() + "\\maps");

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
                 cloudMapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                        cloudDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ark"))))).get(0);
            }
            for(File localDirectory : localMapDirectories)
            {
                File localMapFile = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                        localDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ark"))))).get(0);

                if(!install && cloudMapFile.getName().equals(localMapFile.getName()))
                {
                    matchedDirectory = localDirectory;
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
