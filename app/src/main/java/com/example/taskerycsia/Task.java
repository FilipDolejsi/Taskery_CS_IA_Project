package com.example.taskerycsia;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Task {
    private final String displayName;
    private int color;
    private final int taskID;
    private Time duration;
    private int date;
    private String extDate;
    private boolean available = true;
    private double startTime;
    private double endTime;
    private String description;

    public Task(String displayName, Time duration, String description) {
        this.displayName = displayName;
        this.taskID = hashCode();
        this.description = description;
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        int result;
        result = displayName.hashCode();
        return result;
    }

    public Time getDur() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public boolean isEligible() {
        return available;
    }

    public double getStart() {
        return startTime;
    }

    public void setStart(double d) {
        startTime = d;
    }

    public Time getStartTime() {
        double t1 = Math.floor(startTime);
        double t2 = ((startTime - ((int) startTime)) * 60);
        return new Time(t1, t2);
    }

    public Time getEndTime() {
        double t1 = Math.floor(endTime);
        double t2 = ((endTime - ((int) endTime)) * 60);
        return new Time(t1, t2);
    }

    public double getEnd() {
        return endTime;
    }

    public void setEnd(double i) {
        endTime = i;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
        calendar.setTime(now);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        extDate = formatter.format(new Date(now.getTime() - 24 * 60 * 60 * 1000 * (week + 5 - date))); //maybe week-1
        setAvailable(false);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Task reduce(double reduceBy) {
        Task newTask = new Task(this.displayName,
                this.duration, this.description);
        double newDur = this.getDur().timeToHours() - reduceBy;
        newTask.setDuration(new Time((int) reduceBy, getDecimalsTime(reduceBy)));
        this.setDuration(new Time((int) newDur, getDecimalsTime(newDur)));
        return newTask;
    }

    public Task split() {
        Task newTask = new Task(this.displayName,
                this.duration, this.description);
        double newDur = this.getDur().timeToHours() / 2;
        newTask.setDuration(new Time((int) newDur, getDecimalsTime(newDur)));
        this.setDuration(new Time((int) newDur, getDecimalsTime(newDur)));
        return newTask;
    }

    public String timeToString() {
        String minStart = String.valueOf(getDecimalsTime(startTime));
        String minEnd = String.valueOf(getDecimalsTime(endTime));
        if (getDecimalsTime(startTime) <= 10) {
            minStart = "0" + minStart;
        }
        if (getDecimalsTime(endTime) <= 10) {
            minEnd = "0" + minEnd;
        }
        return (int) startTime + ":" + minStart + "-" + (int) endTime + ":" + minEnd;
    }

    public int getDecimalsTime(double time) {
        double decimals = time - (int) time;
        decimals = decimals * 100;
        double TIME60_COEFFICIENT = 1.6666;
        double rightTime = decimals / TIME60_COEFFICIENT;
        return (int) rightTime;
    }

    public boolean isSameOrLess() {
        return startTime >= endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getExtDate() {
        return extDate;
    }
}