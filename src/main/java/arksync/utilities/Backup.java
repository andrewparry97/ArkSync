package arksync.utilities;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class Backup
{

    public static String createBackupDirectory(String path)
    {
        File backupDirectory = new File(path);
        if(!backupDirectory.exists())
        {
            if(backupDirectory.mkdir())
            {
                System.out.println("HourlyBackup directory created at: " + backupDirectory.getAbsolutePath());
            } else {
                System.out.println("Failed to create backup directory at: " + backupDirectory.getAbsolutePath());
            }
        }
        return path;
    }

    public static String generateBackupName()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm.ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        return formatter.format(currentDateTime);
    }

    public static void tidyDirectory(String path, int period)
    {
        File backupDirectory = new File(path);
        File[] directories = backupDirectory.listFiles();
        assert directories != null;
        Arrays.sort(directories, Comparator.comparingLong(File::lastModified).reversed());
        int counter = 0;
        for(File directory : directories)
        {
            if(counter >= period)
            {
                try
                {
                    FileUtils.deleteDirectory(directory);
                    System.out.println("Removed backup: " + directory.getName());
                }
                catch (IOException ioe)
                {
                    System.out.println("Failed to remove backup: " + directory.getName());
                }
            }
            counter++;
        }
    }

}
