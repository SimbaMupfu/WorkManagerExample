package com.sims.workmanagerexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder()
                .putString(MyWorker.TASK_DESC, "The task data has been passed from MainActivity")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        final OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(MyWorker.class)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .build();

        findViewById(R.id.buttonEnqueue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().enqueue(workRequest);
            }
        });

        //Getting the TextView
        final TextView textView = findViewById(R.id.textViewStatus);

        //Listening to the work status
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //receiving back the data
                        if(workInfo != null && workInfo.getState().isFinished())
                            textView.append(workInfo.getOutputData().getString(MyWorker.TASK_DESC) + "\n");

                        textView.append(workInfo.getState().name() + "\n");
                    }
                });
    }
}