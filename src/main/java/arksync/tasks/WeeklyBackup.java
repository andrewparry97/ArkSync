package arksync.tasks;

import arksync.Main;
import arksync.utilities.Backup;
import arksync.utilities.Install;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class WeeklyBackup implements Job
{

    public void execute(JobExecutionContext context)
    {
        String backupDirectoryPath = Backup.createBackupDirectory(Main.getSyncProperties().getBackupLocation());
        backupDirectoryPath = Backup.createBackupDirectory(backupDirectoryPath + "\\weekly_backups");
        backupDirectoryPath += "\\" + Backup.generateBackupName();
        Install.uploadServer(Main.getSyncProperties().getLocalLocation(), backupDirectoryPath);
        if(Main.getSyncProperties().isWipeBackups())
        {
            Backup.tidyDirectory(Main.getSyncProperties().getBackupLocation() + "\\weekly_backups",
                    Main.getSyncProperties().getWipeWeek());
        }
    }

}
