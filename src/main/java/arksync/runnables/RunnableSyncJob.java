package arksync.runnables;

import arksync.Main;
import arksync.tasks.*;
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
            if(Main.getSyncProperties().isSyncServer())
            {
                if (Main.getSyncProperties().isObeliskSync())
                {
                    JobDetail syncObelisk = JobBuilder.newJob(ObeliskUpdate.class)
                            .withIdentity("syncObelisk", "group1").build();
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group1")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + Main.getSyncProperties().getObeliskPeriod()
                                    + " * * * ?")).build();
                    scheduler.scheduleJob(syncObelisk, trigger);
                }
                if (Main.getSyncProperties().isMapsSync())
                {
                    JobDetail syncMap = JobBuilder.newJob(MapUpdate.class)
                            .withIdentity("syncMap", "group2").build();
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group2")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + Main.getSyncProperties().getMapsPeriod()
                                    + " * * * ?")).build();
                    scheduler.scheduleJob(syncMap, trigger);
                }
                if (Main.getSyncProperties().isPlayerSync())
                {
                    JobDetail syncPlayerData = JobBuilder.newJob(PlayerDataUpdate.class)
                            .withIdentity("syncPlayerData", "group3").build();
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group3")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" + Main.getSyncProperties().getPlayerPeriod()
                                    + " * * * ?")).build();
                    scheduler.scheduleJob(syncPlayerData, trigger);
                }
            }
            if(Main.getSyncProperties().isBackupHourly() || Main.getSyncProperties().isBackupDaily())
            {
                if (Main.getSyncProperties().isBackupHourly())
                {
                    JobDetail backupHourly = JobBuilder.newJob(HourlyBackup.class)
                            .withIdentity("backupHourly", "group4").build();
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group4")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?")).build();
                    scheduler.scheduleJob(backupHourly, trigger);
                }
                if (Main.getSyncProperties().isBackupDaily())
                {
                    JobDetail backupDaily = JobBuilder.newJob(DailyBackup.class)
                            .withIdentity("backupDaily", "group5").build();
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group5")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
                    scheduler.scheduleJob(backupDaily, trigger);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
