package com.hylux.calisthenics;

import java.util.ArrayList;
import java.util.List;

public class StaticExerciseHandler {

    private List<StaticExercise> staticExercises;

    public StaticExerciseHandler(ArrayList<StaticExercise> staticExercises) {
        this.staticExercises = staticExercises;
    }

    public StaticExerciseHandler() {
        this.staticExercises = new ArrayList<>();
    }

    public void addExercise(StaticExercise exercise) {
        staticExercises.add(exercise);
    }
}
