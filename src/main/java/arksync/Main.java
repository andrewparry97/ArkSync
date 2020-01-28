package arksync;

import arksync.dto.ArkPlayerData;
import arksync.runnables.RunnableSyncJob;
import arksync.utilities.Analysis;
import arksync.utilities.Download;
import arksync.utilities.Install;
import arksync.utilities.Synchronisation;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class Main
{

    private static File cloudObelisk;
    private static File localObelisk;
    private static ArkPlayerData arkPlayerData;
    private static HashMap<File, File> cloudToLocalMapDirectoryMapping;

    public static void main(String[] args)
    {
        String localGamePath = "", cloudDirectoryPath = "";
        if(args.length == 2)
        {
            cloudDirectoryPath = args[0];
            localGamePath = args[1];
        }
        else if(args.length == 1)
        {
            cloudDirectoryPath = args[0];
            localGamePath = getLocalPath();
        }
        else
        {
            cloudDirectoryPath = getCloudPath();
            localGamePath = getLocalPath();
        }
        cloudObelisk = new File(cloudDirectoryPath + "\\obelisk");
        localObelisk = new File(localGamePath + "\\LocalState\\Saved\\clusters\\solecluster");

        if(!new File(cloudDirectoryPath).exists())
        {
            Install.uploadServer(localGamePath, cloudDirectoryPath);
        } else {
            cloudToLocalMapDirectoryMapping = Analysis.analyseDirectories(new File(localGamePath), new File(cloudDirectoryPath), false);
            arkPlayerData = Analysis.obtainPlayerDataMapping(cloudToLocalMapDirectoryMapping, cloudDirectoryPath);
            Download.downloadFromCloud(cloudToLocalMapDirectoryMapping, arkPlayerData, cloudObelisk, localObelisk);
        }

        Thread scheduledUpdate = new Thread(new RunnableSyncJob());
        scheduledUpdate.start();
    }

    private static String getLocalPath()
    {
        String localGamePath = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Packages";
        File[] files = Objects.requireNonNull(new File(localGamePath).listFiles((dir, name) ->
                name.toLowerCase().contains("studiowildcard")));
        for(File file : files)
        {
            if(!file.getName().contains("ARK"))
            {
                System.out.println(file.getName());
                localGamePath += "\\" + file.getName();
                break;
            }
        }
        return localGamePath;
    }

    private static String getCloudPath()
    {
        String cloudDirectoryPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Dropbox\\ArkSync";
        System.out.println(cloudDirectoryPath);
        return cloudDirectoryPath;
    }

    public static File getCloudObelisk()
    {
        return cloudObelisk;
    }

    public static File getLocalObelisk()
    {
        return localObelisk;
    }

    public static ArkPlayerData getArkPlayerData()
    {
        return arkPlayerData;
    }

    public static HashMap<File, File> getCloudToLocalMapDirectoryMapping()
    {
        return cloudToLocalMapDirectoryMapping;
    }

    public static void setArkPlayerData(ArkPlayerData arkPlayerData)
    {
        Main.arkPlayerData = arkPlayerData;
    }

    public static void setCloudToLocalMapDirectoryMapping(HashMap<File, File> cloudToLocalMapDirectoryMapping)
    {
        Main.cloudToLocalMapDirectoryMapping = cloudToLocalMapDirectoryMapping;
    }

}
