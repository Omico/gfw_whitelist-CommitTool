package me.omico.gfw_whitelist;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommitTool {

    private static final int MADE_YEAR = 2019;

    private static final String GIT_DIR = ".git";
    private static final String GFW_WHITELIST = "gfw_whitelist.txt";

    private static final File currentPath = new File(System.getProperty("user.dir"));
    private static final File gitDir = new File(currentPath, GIT_DIR);
    private static final File gfwWhitelistFile = new File(currentPath, GFW_WHITELIST);

    public static void main(String[] args) {
        printlnCopyright();
        checkFile();
        commitGfwWhitelist();
    }

    private static void printlnCopyright() {
        int currentYear = getCurrentYear();
        String copyrightYear;
        if (MADE_YEAR == currentYear) {
            copyrightYear = String.valueOf(currentYear);
        } else {
            copyrightYear = MADE_YEAR + "-" + currentYear;
        }
        System.out.println();
        System.out.println("Copyright " + copyrightYear + " Neko Dev");
        System.out.println();
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println();
    }

    private static void checkFile() {
        if (!gitDir.exists()) {
            System.out.println("Couldn't find .git folder");
            System.exit(1);
        }
        if (!gfwWhitelistFile.exists()) {
            System.out.println("Couldn't find gfw_whitelist.txt");
            System.exit(1);
        }
    }

    private static void commitGfwWhitelist() {
        runCommand("git", "add", "gfw_whitelist.txt");
        runCommand("git", "commit", "-a", "-m", getDate());
    }

    private static int getCurrentYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return Integer.parseInt(simpleDateFormat.format(date));
    }

    private static String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CTT"));
        return simpleDateFormat.format(date);
    }

    private static void runCommand(String... command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = inputBufferedReader.readLine()) != null) System.out.println(line);
            while ((line = errorBufferedReader.readLine()) != null) System.out.println(line);
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
