package com.sameer.taskdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTaskActivity extends AppCompatActivity {

    private int taskNumber;
    private firebaseRestAPI firebaseData;
    private Retrofit retrofit;

    private void navigateToMainScreen() {
        Intent mainActivityIntent = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    public void cancelTaskUpdation(View view) {
        navigateToMainScreen();
    }

    public void addCurrentTask(View view) {
        Call<Integer> callDataCounter = firebaseData.getDataCounter();
        callDataCounter.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                taskNumber = response.body();

                EditText startDateInput = (EditText) findViewById(R.id.startDate);
                EditText endDateInput = (EditText) findViewById(R.id.endDate);
                EditText tasksInput = (EditText) findViewById(R.id.tasks);

                tasksDetails task = new tasksDetails(
                        startDateInput.getText().toString(),
                        endDateInput.getText().toString(),
                        tasksInput.getText().toString()
                );
                String taskNumberString = new String("data" + Integer.toString(++taskNumber));

                Call<Integer> callUpdateDataCounter = firebaseData.putDataCounter(taskNumber);
                callUpdateDataCounter.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {

                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(AddTaskActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

                Call<tasksDetails> callAddTasks = firebaseData.putTasks(taskNumberString, task);
                callAddTasks.enqueue(new Callback<tasksDetails>() {
                    @Override
                    public void onResponse(Call<tasksDetails> call, Response<tasksDetails> response) {
                        Toast.makeText(AddTaskActivity.this, "TASK UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<tasksDetails> call, Throwable t) {
                        Toast.makeText(AddTaskActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        navigateToMainScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://taskdiary-56ae9-default-rtdb.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        firebaseData = retrofit.create(firebaseRestAPI.class);
    }
}