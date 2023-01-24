package com.example.taskerycsia;

import androidx.annotation.NonNull;

public class Time {
    private double hours;
    private double minutes;

    public Time(double hours, double minutes) {
        setTime(hours, minutes);
    }

    @NonNull
    @Override
    public String toString() {
        String hoursStr = String.valueOf((int) hours);
        String minutesStr = String.valueOf((int) minutes);
        if (minutes <= 10) {
            minutesStr = "0" + minutesStr;
        }
        return hoursStr + ":" + minutesStr;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }

    public void setTime(double h, double m) {
        this.hours = h;
        this.minutes = m;
    }

    public double timeToHours() {
        double m = this.minutes / 60;
        return this.hours + m;
    }

    public static float findMinute(float time){
        float t = time - (int) time;
        return t*60;
    }

    public static float minuteToHour(float min){
        return min/60;
    }

    public int timeToHInt() {
        int m = (int) this.minutes / 60;
        return (int) this.hours + m;
    }

    public double timeToMinutes() {
        double h = this.hours * 60;
        return h + this.minutes;
    }

    public boolean equals(Time t) {
        if (this == t) return true;
        if (t == null || getClass() != t.getClass()) return false;

        if (Double.compare(t.hours, hours) != 0) return false;
        return Double.compare(t.minutes, minutes) == 0;
    }

    //todo:find what hashcode does and why it is useful
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(hours);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minutes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Time add(Time dur) {
        double addMinutes = this.minutes + dur.minutes;
        double addHours = this.hours + dur.hours;
        if (addMinutes >= 60) {
            addMinutes -= 60;
            addHours += 1;
        }
        return new Time(addHours, addMinutes);
    }
}
