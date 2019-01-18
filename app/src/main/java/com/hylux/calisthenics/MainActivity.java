package com.hylux.calisthenics;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;

    private RadioGroup exercisesView;
    private RadioButton startRunView, startStaticView;
    private Button chooseRadioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        exercisesView = findViewById(R.id.exercises);
        startRunView = findViewById(R.id.startRun);
        startStaticView = findViewById(R.id.startStatic);
        chooseRadioView = findViewById(R.id.chooseRadio);

        chooseRadioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = exercisesView.getCheckedRadioButtonId();
                Intent intent;

                switch (selectedId) {
                    case R.id.startRun:
                        intent = new Intent(getApplicationContext(), LocationActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.startStatic:
                        //intent = new Intent(getApplicationContext(), StaticActivity.class);
                        SelectorFragment selectorFragment = new SelectorFragment();
                        fm.beginTransaction().add(R.id.extraFragment, selectorFragment).commit();

                        selectorFragment.setOnConfirmConfigListener(new SelectorFragment.OnConfirmConfigListener() {
                            @Override
                            public void getArgs(int sets, int reps, int time) {
                                Intent intent = new Intent(getApplicationContext(), StaticActivity.class);
                                Log.d("ARGS", sets + ", " + reps + ", " + time);
                                ArrayList<Integer> argsList = new ArrayList<>();
                                argsList.add(sets);
                                argsList.add(reps);
                                argsList.add(time);
                                final Bundle args = new Bundle();
                                args.putIntegerArrayList("EXTRA_CONFIG", argsList);
                                intent.putExtras(args);
                                startActivity(intent);
                                finish();
                            }
                        });
                        break;

                    default:
                        //intent = new Intent(getApplicationContext(), StaticActivity.class);
                        selectorFragment = new SelectorFragment();
                        fm.beginTransaction().add(R.id.extraFragment, selectorFragment).commit();
                        break;
                }
            }
        });
    }
}
