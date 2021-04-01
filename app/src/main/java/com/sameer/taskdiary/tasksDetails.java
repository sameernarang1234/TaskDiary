package com.sameer.taskdiary;

import com.google.gson.annotations.SerializedName;

public class tasksDetails {
    @SerializedName("body")
    private String tasks;

    private String start_date;
    private String end_date;

    public tasksDetails(String start_date, String end_date, String tasks) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.tasks = tasks;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getTasks() {
        return tasks;
    }
}
