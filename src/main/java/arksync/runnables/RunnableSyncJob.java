package arksync.runnables;

import arksync.tasks.MapUpdate;
import arksync.tasks.ObeliskUpdate;
import arksync.tasks.PlayerDataUpdate;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class RunnableSyncJob implements Runnable
{

    public void run()
    {
        final int obeliskSync = 1;
        final int mapSync = 5;
        final int playerDataSync = 2;

        try
        {
            JobDetail syncObelisk = JobBuilder.newJob(ObeliskUpdate.class).withIdentity("syncObelisk", "group1").build();
            JobDetail syncMap = JobBuilder.newJob(MapUpdate.class).withIdentity("syncMap", "group2").build();
            JobDetail syncPlayerData = JobBuilder.newJob(PlayerDataUpdate.class).withIdentity("syncPlayerData", "group3").build();

            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + obeliskSync + " * * * ?")).build();
            Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group2")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + mapSync + " * * * ?")).build();
            Trigger trigger3 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group3")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + playerDataSync + " * * * ?")).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(syncObelisk, trigger1);
            scheduler.scheduleJob(syncMap, trigger2);
            scheduler.scheduleJob(syncPlayerData, trigger3);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
