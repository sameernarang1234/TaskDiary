package com.sameer.taskdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private firebaseRestAPI firebaseData;
    private boolean isTasksAvailable;
    private int taskNumber = 0;
    private int nextTaskNumber = 0;
    private int prevTaskNumber = 0;

    public void getPrevTask(View view) {
        if (prevTaskNumber <= taskNumber && prevTaskNumber > 1) {
            prevTaskNumber--;
            nextTaskNumber = prevTaskNumber;
            String prevTaskPath = "data" + Integer.toString(prevTaskNumber);
            Call<tasksDetails> callData = firebaseData.getTasks(prevTaskPath);
            callData.enqueue(new Callback<tasksDetails>() {
                @Override
                public void onResponse(Call<tasksDetails> call, Response<tasksDetails> response) {
                    String tasks = response.body().getTasks();
                    String startDate = response.body().getStart_date();
                    String endDate = response.body().getEnd_date();

                    TextView taskOutput = (TextView) findViewById(R.id.tasksView);
                    TextView startDateOutput = (TextView) findViewById(R.id.startDateView);
                    TextView endDateOutput = (TextView) findViewById(R.id.endDateView);

                    taskOutput.setText(tasks);
                    startDateOutput.setText(startDate);
                    endDateOutput.setText(endDate);
                }

                @Override
                public void onFailure(Call<tasksDetails> call, Throwable t) {

                }
            });
        }
    }

    public void getNextTask(View view) {
        if (nextTaskNumber < taskNumber) {
            nextTaskNumber++;
            prevTaskNumber = nextTaskNumber;
            String nextTaskPath = "data" + Integer.toString(nextTaskNumber);
            Call<tasksDetails> callData = firebaseData.getTasks(nextTaskPath);
            callData.enqueue(new Callback<tasksDetails>() {
                @Override
                public void onResponse(Call<tasksDetails> call, Response<tasksDetails> response) {
                    String tasks = response.body().getTasks();
                    String startDate = response.body().getStart_date();
                    String endDate = response.body().getEnd_date();

                    TextView taskOutput = (TextView) findViewById(R.id.tasksView);
                    TextView startDateOutput = (TextView) findViewById(R.id.startDateView);
                    TextView endDateOutput = (TextView) findViewById(R.id.endDateView);

                    taskOutput.setText(tasks);
                    startDateOutput.setText(startDate);
                    endDateOutput.setText(endDate);
                }

                @Override
                public void onFailure(Call<tasksDetails> call, Throwable t) {

                }
            });
        }
    }

    public void navigateToAddTaskIntent(View view) {
        Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(addTaskIntent);
    }

    public void getCurrentTask(View view) {
        Call<Integer> dataCounter = firebaseData.getDataCounter();
        dataCounter.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == 0) {
                    Toast.makeText(MainActivity.this, "No current task assigned", Toast.LENGTH_SHORT).show();
                    isTasksAvailable = false;
                }
                else {
                    isTasksAvailable = true;
                    taskNumber = response.body();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error" + t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

        if (isTasksAvailable) {
            String path = "data" + Integer.toString(taskNumber);
            Call<tasksDetails> callData = firebaseData.getTasks(path);
            callData.enqueue(new Callback<tasksDetails>() {
                @Override
                public void onResponse(Call<tasksDetails> call, Response<tasksDetails> response) {
                    String tasks = response.body().getTasks();
                    String startDate = response.body().getStart_date();
                    String endDate = response.body().getEnd_date();

                    TextView taskOutput = (TextView) findViewById(R.id.tasksView);
                    TextView startDateOutput = (TextView) findViewById(R.id.startDateView);
                    TextView endDateOutput = (TextView) findViewById(R.id.endDateView);

                    taskOutput.setText(tasks);
                    startDateOutput.setText(startDate);
                    endDateOutput.setText(endDate);
                }

                @Override
                public void onFailure(Call<tasksDetails> call, Throwable t) {

                }
            });
        }
        nextTaskNumber = taskNumber;
        prevTaskNumber =taskNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://taskdiary-56ae9-default-rtdb.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        firebaseData = retrofit.create(firebaseRestAPI.class);
    }
}