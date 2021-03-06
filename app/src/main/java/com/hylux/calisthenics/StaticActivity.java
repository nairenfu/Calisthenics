package com.hylux.calisthenics;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StaticActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private FragmentManager fm;
    private TimerFragment timerFragment;

    private TextView repsView, setsView, totalRepsView;
    private Button finishSetView;

    private StaticExercise exercise;

    private boolean DOWN, UP, PAUSED;
    private int currentSet, numSets;
    //0 sets, 1 reps, 2 time
    private List<Integer> config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);

        config = new ArrayList<>();
        Bundle args = getIntent().getExtras();
        if (args != null) {
            config = args.getIntegerArrayList("EXTRA_CONFIG");
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        fm = getSupportFragmentManager();
        //timerFragment = TimerFragment.newInstance(TimerFragment.REPS, 5000, 1000);

        repsView = findViewById(R.id.reps);
        setsView = findViewById(R.id.sets);
        totalRepsView = findViewById(R.id.totalReps);
        finishSetView = findViewById(R.id.finishSet);
        finishSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PAUSED) {
                    currentSet = config.get(1);
                    repsView.setText("Reps: " + currentSet + " out of " + config.get(1));
                    if (currentSet == config.get(1)) {
                        exercise.addReps(10);
                        currentSet = 0;
                        numSets += 1;
                        setsView.setText("Sets: " + numSets + " out of " + config.get(0));

                        //TODO pause the sensor or ignore changes while timer is running
                        PAUSED = !PAUSED;

                        if (numSets == config.get(0)) {
                            final TextView totalReps = new TextView(getApplicationContext());
                            totalReps.setText(String.valueOf(exercise.getReps()));
                            LinearLayout parentLayout = findViewById(R.id.parentLayout);
                            parentLayout.addView(totalReps);
                        } else {
                            timerFragment = TimerFragment.newInstance(TimerFragment.TIME, config.get(2), 1000);
                            timerFragment.setOnTimerEndedListener(new TimerFragment.OnTimerEndedListener() {
                                @Override
                                public void onTimerEnded() {
                                    fm.beginTransaction().remove(timerFragment).commit();
                                    PAUSED = !PAUSED;
                                }
                            });
                            fm.beginTransaction().add(R.id.parentLayout, timerFragment).commit();
                        }
                    }
                }
            }
        });

        repsView.setText("Reps completed: 0 out of " + config.get(1));
        setsView.setText("Sets completed: 0 out of " + config.get(0));
        totalRepsView.setText("Total reps: ");

        exercise = new StaticExercise();
        Log.d("START", String.valueOf(exercise.getStartTime()));

        DOWN = false;
        UP = false;
        PAUSED = false;
        currentSet = 0;
        numSets = 0;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        fm.executePendingTransactions();
        //Use this to prevent IllegalStateException regarding calling commit() after onSaveInstanceState()
        sensorManager.unregisterListener(this, proximitySensor);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!PAUSED) {
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
                repsView.setText("Reps: " + currentSet + " out of " + config.get(1));
                Log.d("CURRENT Reps", String.valueOf(currentSet));
                DOWN = false;
                UP = false;

                if (currentSet == config.get(1)) {
                    exercise.addReps(10);
                    currentSet = 0;
                    numSets += 1;
                    setsView.setText("Sets: " + numSets + " out of " + config.get(0));

                    //TODO pause the sensor or ignore changes while timer is running
                    PAUSED = !PAUSED;

                    if (numSets == config.get(0)) {
                        final TextView totalReps = new TextView(getApplicationContext());
                        totalReps.setText(String.valueOf(exercise.getReps()));
                        LinearLayout parentLayout = findViewById(R.id.parentLayout);
                        parentLayout.addView(totalReps);
                    } else {
                        timerFragment = TimerFragment.newInstance(TimerFragment.TIME, config.get(2), 1000);
                        timerFragment.setOnTimerEndedListener(new TimerFragment.OnTimerEndedListener() {
                            @Override
                            public void onTimerEnded() {
                                fm.beginTransaction().remove(timerFragment).commit();
                                PAUSED = !PAUSED;
                            }
                        });
                        fm.beginTransaction().add(R.id.parentLayout, timerFragment).commit();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
