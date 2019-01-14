package com.hylux.calisthenics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Exercise {

    private Date startTime;
    private double duration;
    private List<Integer> reps;

    public Exercise() {
        this.startTime = Calendar.getInstance().getTime();
        reps = new ArrayList<>();
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
        return reps.size();
    }

    public int getReps() {
        int total = 0;
        for (int rep : reps) {
            total += rep;
        }
        return total;
    }

    public int getReps(int set) {
        return reps.get(set);
    }

    public void setReps(int set, int rep) {
        reps.set(set, rep);
    }

    public void addReps(int rep) {
        reps.add(rep);
    }
}
