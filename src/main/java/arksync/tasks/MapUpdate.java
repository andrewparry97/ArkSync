package arksync.tasks;

import arksync.Main;
import arksync.utilities.Synchronisation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.File;
import java.util.Map;

public class MapUpdate implements Job
{

    public void execute(JobExecutionContext context)
    {
        Map<File, File> saveLocations = Main.getCloudToLocalMapDirectoryMapping();
        for(File file : saveLocations.keySet())
        {
            Synchronisation.synchroniseDirectories(file, saveLocations.get(file), ".ark");
        }
    }

}
