package arksync.tasks;

import arksync.Main;
import arksync.utilities.Backup;
import arksync.utilities.Install;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class DailyBackup implements Job
{

    public void execute(JobExecutionContext context)
    {
        String backupDirectoryPath = Backup.createBackupDirectory(Main.getSyncProperties().getBackupLocation());
        backupDirectoryPath = Backup.createBackupDirectory(backupDirectoryPath + "\\daily_backups");
        backupDirectoryPath += "\\" + Backup.generateBackupName();
        Install.uploadServer(Main.getSyncProperties().getLocalLocation(), backupDirectoryPath);
        if(Main.getSyncProperties().isWipeBackups())
        {
            Backup.tidyDirectory(Main.getSyncProperties().getBackupLocation() + "\\daily_backups",
                    Main.getSyncProperties().getWipeDay());
        }
    }

}
