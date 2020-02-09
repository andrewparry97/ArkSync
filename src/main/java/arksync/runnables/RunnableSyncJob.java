package arksync.runnables;

import arksync.Main;
import arksync.tasks.MapUpdate;
import arksync.tasks.ObeliskUpdate;
import arksync.tasks.PlayerDataUpdate;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class RunnableSyncJob implements Runnable
{

    public void run()
    {
        try
        {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            if(Main.getSyncProperties().isObeliskSync())
            {
                JobDetail syncObelisk = JobBuilder.newJob(ObeliskUpdate.class)
                        .withIdentity("syncObelisk", "group1").build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group1")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + Main.getSyncProperties().getObeliskPeriod()
                                + " * * * ?")).build();
                scheduler.scheduleJob(syncObelisk, trigger);
            }
            if(Main.getSyncProperties().isMapsSync())
            {
                JobDetail syncMap = JobBuilder.newJob(MapUpdate.class)
                        .withIdentity("syncMap", "group2").build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group2")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + Main.getSyncProperties().getMapsPeriod()
                                + " * * * ?")).build();
                scheduler.scheduleJob(syncMap, trigger);
            }
            if(Main.getSyncProperties().isPlayerSync())
            {
                JobDetail syncPlayerData = JobBuilder.newJob(PlayerDataUpdate.class)
                        .withIdentity("syncPlayerData", "group3").build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group3")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + Main.getSyncProperties().getPlayerPeriod()
                                + " * * * ?")).build();
                scheduler.scheduleJob(syncPlayerData, trigger);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
