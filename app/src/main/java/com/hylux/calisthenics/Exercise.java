package com.hylux.calisthenics;

import java.util.Calendar;
import java.util.Date;

public class Exercise {

    private Date startTime;
    private double duration;
    private int sets, reps;

    public Exercise() {
        this.startTime = Calendar.getInstance().getTime();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
