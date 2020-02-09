package arksync;

import java.util.Properties;

public class SyncProperties
{

    private String cloudLocation;
    private String localLocation;
    private String backupLocation;
    private boolean playerSync;
    private boolean obeliskSync;
    private boolean mapsSync;
    private int playerPeriod;
    private int obeliskPeriod;
    private int mapsPeriod;
    private boolean downloadServer;
    private boolean syncServer;
    private boolean backupHourly;
    private boolean backupDaily;
    private boolean wipeBackups;
    private int wipeHour;
    private int wipeDay;

    public SyncProperties(Properties properties)
    {
        cloudLocation = properties.getProperty("loc.cloud");
        localLocation = properties.getProperty("loc.local");
        backupLocation = properties.getProperty("loc.backup");
        playerSync = Boolean.valueOf(properties.getProperty("sync.player"));
        obeliskSync = Boolean.valueOf(properties.getProperty("sync.obelisk"));
        mapsSync = Boolean.valueOf(properties.getProperty("sync.maps"));
        playerPeriod = Integer.valueOf(properties.getProperty("period.player"));
        obeliskPeriod = Integer.valueOf(properties.getProperty("period.obelisk"));
        mapsPeriod = Integer.valueOf(properties.getProperty("period.maps"));
        downloadServer = Boolean.valueOf(properties.getProperty("task.download"));
        syncServer = Boolean.valueOf(properties.getProperty("task.sync"));
        backupHourly = Boolean.valueOf(properties.getProperty("backup.hourly"));
        backupDaily = Boolean.valueOf(properties.getProperty("backup.daily"));
        wipeBackups = Boolean.valueOf(properties.getProperty("backup.wipe"));
        wipeHour = Integer.valueOf(properties.getProperty("wipe.hour"));
        wipeDay = Integer.valueOf(properties.getProperty("wipe.day"));
    }

    public boolean isMapsSync()
    {
        return mapsSync;
    }

    public boolean isObeliskSync()
    {
        return obeliskSync;
    }

    public boolean isPlayerSync()
    {
        return playerSync;
    }

    public int getMapsPeriod()
    {
        return mapsPeriod;
    }

    public int getObeliskPeriod()
    {
        return obeliskPeriod;
    }

    public int getPlayerPeriod()
    {
        return playerPeriod;
    }

    public String getCloudLocation()
    {
        return cloudLocation;
    }

    public String getLocalLocation()
    {
        return localLocation;
    }

    public void setLocalLocation(String localLocation)
    {
        this.localLocation = localLocation;
    }

    public void setCloudLocation(String cloudLocation)
    {
        this.cloudLocation = cloudLocation;
    }

    public boolean isDownloadServer()
    {
        return downloadServer;
    }

    public boolean isSyncServer()
    {
        return syncServer;
    }

    public boolean isBackupDaily()
    {
        return backupDaily;
    }

    public boolean isBackupHourly()
    {
        return backupHourly;
    }

    public boolean isWipeBackups()
    {
        return wipeBackups;
    }

    public int getWipeDay()
    {
        return wipeDay;
    }

    public int getWipeHour()
    {
        return wipeHour;
    }

    public String getBackupLocation()
    {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation)
    {
        this.backupLocation = backupLocation;
    }

}

