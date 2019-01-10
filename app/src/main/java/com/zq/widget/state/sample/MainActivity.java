package com.zq.widget.state.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zq.widget.state.SampleStateChangeListener;
import com.zq.widget.state.StateFrameLayout;


public class MainActivity extends AppCompatActivity {

    private StateFrameLayout stateFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        stateFrameLayout = (StateFrameLayout) findViewById(R.id.m_state_frame_layout);
        stateFrameLayout.setOnStateChangeListener(new SampleStateChangeListener());
        stateFrameLayout.setCurrentState(StateFrameLayout.STATE_CONTENT);
        stateFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentState = stateFrameLayout.getCurrentState();
                if (currentState == StateFrameLayout.STATE_CONTENT) {
                    stateFrameLayout.setCurrentState(StateFrameLayout.STATE_EMPTY);
                }else if (currentState == StateFrameLayout.STATE_EMPTY) {
                    stateFrameLayout.setCurrentState(StateFrameLayout.STATE_ERROR);
                }else if (currentState == StateFrameLayout.STATE_ERROR) {
                    stateFrameLayout.setCurrentState(StateFrameLayout.STATE_LOADING);
                }else if (currentState == StateFrameLayout.STATE_LOADING) {
                    stateFrameLayout.setCurrentState(StateFrameLayout.STATE_CONTENT);
                }
            }
        });
    }


}
