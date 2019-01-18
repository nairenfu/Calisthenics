package com.hylux.calisthenics;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TimerFragment extends Fragment {

    public static final int TIME = 5000;
    public static final int REPS = 5010;

    private int style;

    private TextView timeRemainingView;

    private long duration, interval;

    private CountDownTimer countDownTimer;

    private OnTimerEndedListener callback;

    public TimerFragment() {
        Log.d("TIMER", "STARTED");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            switch (getArguments().getInt("STYLE")) {
                case TIME:
                    style = TIME;
                    duration = getArguments().getLong("DURATION");
                    interval = getArguments().getLong("INTERVAL");
                    break;

                case REPS:
                    style = REPS;
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_timer, container, false);

        timeRemainingView = parent.findViewById(R.id.timeRemaining);

        switch (style) {
            case TIME:
                countDownTimer = new CountDownTimer(duration, interval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeRemainingView.setText(String.valueOf(millisUntilFinished/1000));
                    }

                    @Override
                    public void onFinish() {
                        timeRemainingView.setText("STOP");
                        callback.onTimerEnded();
                    }
                }.start();
                break;
        }

        return parent;
    }

    public static final TimerFragment newInstance(int style, long duration, long interval) {
        TimerFragment timerFragment = new TimerFragment();
        final Bundle args = new Bundle();
        args.putInt("STYLE", style);
        args.putLong("DURATION", duration);
        args.putLong("INTERVAL", interval);
        timerFragment.setArguments(args);
        return timerFragment;
    }

    public CountDownTimer startTimer(long duration, long interval) {
        return new CountDownTimer(duration, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                timeRemainingView.setText("START");
                callback.onTimerEnded();
            }
        }.start();
    }

    public interface OnTimerEndedListener {
        void onTimerEnded();
    }

    public void setOnTimerEndedListener(OnTimerEndedListener listener) {
        callback = listener;
    }
}