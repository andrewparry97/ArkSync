package arksync.tasks;

import arksync.Main;
import arksync.dto.ArkPlayerData;
import arksync.utilities.Synchronisation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataUpdate implements Job
{

    public void execute(JobExecutionContext context)
    {
        ArkPlayerData arkPlayerData = Main.getArkPlayerData();
        List<File> localSaveLocations = new ArrayList<>(Main.getCloudToLocalMapDirectoryMapping().values());
        for(int i = 0; i < 1; i++)
        {
            for (File directory : localSaveLocations)
            {
                Synchronisation.synchroniseDirectories(arkPlayerData.getArkProfile(), directory, ".arkprofile");
                Synchronisation.synchroniseDirectories(arkPlayerData.getArkTribe(), directory, ".arktribe");
                Synchronisation.synchroniseDirectories(arkPlayerData.getArkTributeTribe(), directory, ".arktributetribe");
                Synchronisation.synchroniseDirectories(arkPlayerData.getProfileBak(), directory, ".profilebak");
                Synchronisation.synchroniseDirectories(arkPlayerData.getTribeBak(), directory, ".tribebak");
            }
        }
    }

}
