package arksync;

import java.util.Properties;

public class SyncProperties
{

    private String cloudLocation;
    private String localLocation;
    private boolean playerSync;
    private boolean obeliskSync;
    private boolean mapsSync;
    private int playerPeriod;
    private int obeliskPeriod;
    private int mapsPeriod;
    private boolean downloadServer;
    private boolean syncServer;

    public SyncProperties(Properties properties)
    {
        cloudLocation = properties.getProperty("loc.cloud");
        localLocation = properties.getProperty("loc.local");
        playerSync = Boolean.valueOf(properties.getProperty("sync.player"));
        obeliskSync = Boolean.valueOf(properties.getProperty("sync.obelisk"));
        mapsSync = Boolean.valueOf(properties.getProperty("sync.maps"));
        playerPeriod = Integer.valueOf(properties.getProperty("period.player"));
        obeliskPeriod = Integer.valueOf(properties.getProperty("period.obelisk"));
        mapsPeriod = Integer.valueOf(properties.getProperty("period.maps"));
        downloadServer = Boolean.valueOf(properties.getProperty("task.download"));
        syncServer = Boolean.valueOf(properties.getProperty("task.sync"));
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

}

