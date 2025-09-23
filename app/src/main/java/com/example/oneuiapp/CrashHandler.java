package com.example.oneuiapp.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private final Context context;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public CrashHandler(Context context) {
        this.context = context;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveCrashLog(ex);
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, ex);
        }
    }

    private void saveCrashLog(Throwable ex) {
        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File logFile = new File(downloadDir, "OneUIApp_Crash_" + getTimeStamp() + ".txt");

            FileWriter writer = new FileWriter(logFile);
            writer.write("=== OneUI App Crash Report ===\n");
            writer.write("Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()) + "\n");
            writer.write("Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\n");
            writer.write("Android Version: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n");
            writer.write("App Version: 1.0\n\n");
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            writer.write("Stack Trace:\n" + sw.toString());
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public static void setupCrashHandler(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(context));
    }
}
