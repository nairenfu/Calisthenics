package com.hylux.calisthenics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private RadioGroup exercisesView;
    private RadioButton startRunView, startStaticView;
    private Button chooseRadioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        break;

                    case R.id.startStatic:
                        intent = new Intent(getApplicationContext(), StaticActivity.class);
                        break;

                    default:
                        intent = new Intent(getApplicationContext(), StaticActivity.class);
                        break;
                }

                startActivity(intent);
            }
        });
    }
}
