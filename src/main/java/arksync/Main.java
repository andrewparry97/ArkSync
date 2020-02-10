package arksync;

import arksync.dto.ArkPlayerData;
import arksync.runnables.RunnableSyncJob;
import arksync.utilities.Analysis;
import arksync.utilities.Download;
import arksync.utilities.Install;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class Main
{

    private static final Logger updateLog = Logger.getLogger(Main.class.getName());

    private static SyncProperties syncProperties;
    private static File cloudObelisk;
    private static File localObelisk;
    private static ArkPlayerData arkPlayerData;
    private static HashMap<File, File> cloudToLocalMapDirectoryMapping;

    public static void main(String[] args)
    {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(new File(args[0])));
            syncProperties = new SyncProperties(properties);
            if(syncProperties.getCloudLocation().equals("null"))
            {
                syncProperties.setCloudLocation(getCloudPath());
            }
            if(syncProperties.getLocalLocation().equals("null"))
            {
                syncProperties.setLocalLocation(getLocalPath());
            }
            if(syncProperties.getBackupLocation().equals("null"))
            {
                syncProperties.setBackupLocation("C:\\Users\\"+ System.getProperty("user.name") +"\\ArkSyncBackups");
            }

            cloudObelisk = new File(syncProperties.getCloudLocation() + "\\obelisk");
            localObelisk = new File(syncProperties.getLocalLocation()
                    + "\\LocalState\\Saved\\clusters\\solecluster");

            if(!new File(syncProperties.getCloudLocation()).exists())
            {
                Install.uploadServer(syncProperties.getLocalLocation(), syncProperties.getCloudLocation());
            } else {
                cloudToLocalMapDirectoryMapping = Analysis.analyseDirectories(new File(syncProperties.getLocalLocation()),
                        new File(syncProperties.getCloudLocation()), false);
                arkPlayerData = Analysis.obtainPlayerDataMapping(cloudToLocalMapDirectoryMapping,
                        syncProperties.getCloudLocation());
                if(syncProperties.isDownloadServer())
                {
                    Download.downloadFromCloud(cloudToLocalMapDirectoryMapping, arkPlayerData, cloudObelisk, localObelisk);
                }
            }
            if(syncProperties.isSyncServer() || syncProperties.isBackupDaily() || syncProperties.isBackupHourly())
            {
                Thread scheduledUpdate = new Thread(new RunnableSyncJob());
                scheduledUpdate.start();
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
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
                localGamePath += "\\" + file.getName();
                break;
            }
        }
        updateLog.info(localGamePath);
        return localGamePath;
    }

    private static String getCloudPath()
    {
        String cloudDirectoryPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Dropbox\\ArkSync";
        updateLog.info(cloudDirectoryPath);
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

    public static SyncProperties getSyncProperties()
    {
        return syncProperties;
    }

}
