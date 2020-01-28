package arksync.dto;

import java.io.File;

public class ArkPlayerData
{

    private File arkProfile;
    private File arkTribe;
    private File arkTributeTribe;
    private File profileBak;
    private File tribeBak;

    public ArkPlayerData(String cloudDirectoryPath)
    {
        String cloudPlayerDataPath = cloudDirectoryPath + "\\player_data";
        arkProfile = new File(cloudPlayerDataPath + "\\arkprofile");
        arkTribe = new File(cloudPlayerDataPath + "\\arktribe");
        arkTributeTribe = new File(cloudPlayerDataPath + "\\arktributetribe");
        profileBak = new File(cloudPlayerDataPath + "\\profilebak");
        tribeBak = new File(cloudPlayerDataPath + "\\tribebak");
    }

    public File getArkProfile()
    {
        return arkProfile;
    }

    public File getArkTribe()
    {
        return arkTribe;
    }

    public File getArkTributeTribe()
    {
        return arkTributeTribe;
    }

    public File getProfileBak()
    {
        return profileBak;
    }

    public File getTribeBak()
    {
        return tribeBak;
    }

}
