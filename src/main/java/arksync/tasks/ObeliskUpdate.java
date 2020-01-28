package arksync.tasks;

import arksync.Main;
import arksync.utilities.Synchronisation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class ObeliskUpdate implements Job
{

    public void execute(JobExecutionContext context)
    {
        Synchronisation.synchroniseDirectories(Main.getCloudObelisk(), Main.getLocalObelisk(), null);
    }

}
