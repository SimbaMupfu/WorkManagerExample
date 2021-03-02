package com.sims.workmanagerexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public static final String TASK_DESC = "task_desc";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        String taskDesc = getInputData().getString(TASK_DESC);
        displayNotification("My Worker", taskDesc);

        Data data = new Data.Builder()
                .putString(TASK_DESC, "The conclusion of the task")
                .build();
        return Result.success(data);
    }

    private void displayNotification(String title, String task){
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("myworkerchannel", "myworkerchannel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "myworkerchannel")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }
}
