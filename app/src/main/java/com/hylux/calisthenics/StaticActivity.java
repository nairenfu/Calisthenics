package com.hylux.calisthenics;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StaticActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private FragmentManager fm;
    private TimerFragment timerFragment;

    private StaticExercise exercise;

    private boolean DOWN, UP;
    private int currentSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        fm = getSupportFragmentManager();
        //timerFragment = TimerFragment.newInstance(TimerFragment.REPS, 5000, 1000);

        exercise = new StaticExercise();
        Log.d("START", String.valueOf(exercise.getStartTime()));

        DOWN = false;
        UP = false;
        currentSet = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0) {
            DOWN = true;
            Log.d("DOWN", "TRUE");
        }
        if (event.values[0] == 8) {
            UP = true;
            Log.d("UP", "TRUE");
        }
        if (DOWN && UP) {
            currentSet += 1;
            Log.d("CURRENT SET", String.valueOf(currentSet));
            DOWN = false;
            UP = false;

            if (currentSet == 10) {
                exercise.addReps(10);
                currentSet = 0;
                timerFragment = TimerFragment.newInstance(TimerFragment.TIME, 5000, 1000);
                fm.beginTransaction().add(R.id.parentLayout, timerFragment).commit();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
