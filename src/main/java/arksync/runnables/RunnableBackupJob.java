package arksync.runnables;

import arksync.Main;
import arksync.tasks.DailyBackup;
import arksync.tasks.HourlyBackup;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class RunnableBackupJob implements Runnable
{

    public void run()
    {
        try
        {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            if(Main.getSyncProperties().isBackupHourly())
            {
                JobDetail backupHourly = JobBuilder.newJob(HourlyBackup.class)
                        .withIdentity("backupHourly", "group1").build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group1")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?")).build();
                scheduler.scheduleJob(backupHourly, trigger);
            }
            if(Main.getSyncProperties().isBackupDaily())
            {
                JobDetail backupDaily = JobBuilder.newJob(DailyBackup.class)
                        .withIdentity("backupDaily", "group2").build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group2")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
                scheduler.scheduleJob(backupDaily, trigger);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
