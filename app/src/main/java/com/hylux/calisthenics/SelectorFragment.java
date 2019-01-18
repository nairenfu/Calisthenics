package com.hylux.calisthenics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectorFragment extends Fragment {

    private OnConfirmConfigListener callback;

    private RadioGroup staticStyle;
    private RadioButton repsView, timeView;
    private EditText editSetsView, editRepsView, editTimeView;
    private Button startExerciseView;

    private int style, sets, reps, time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_selector, container, false);

        staticStyle = parent.findViewById(R.id.staticStyle);
        repsView = parent.findViewById(R.id.styleReps);
        timeView = parent.findViewById(R.id.styleTime);
        editSetsView = parent.findViewById(R.id.configureSetsEdit);
        editRepsView = parent.findViewById(R.id.configureRepsEdit);
        editTimeView = parent.findViewById(R.id.configureTimeEdit);
        startExerciseView = parent.findViewById(R.id.selectorStart);

        style = staticStyle.getCheckedRadioButtonId();

        startExerciseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editSetsView.getText().toString().equals("")) {
                    sets = Integer.valueOf(editSetsView.getText().toString());
                } else {
                    sets = 6;
                }
                if (!editRepsView.getText().toString().equals("")) {
                    reps = Integer.valueOf(editRepsView.getText().toString());
                } else {
                    reps = 15;
                }
                if (!editTimeView.getText().toString().equals("")) {
                    time = Integer.valueOf(editTimeView.getText().toString());
                } else {
                    time = 15000;
                }

                callback.getArgs(sets, reps, time);
            }
        });

        return parent;
    }

    public interface OnConfirmConfigListener {

        void getArgs(int sets, int reps, int time);
    }

    public void setOnConfirmConfigListener(OnConfirmConfigListener listener) {
        callback = listener;
    }
}