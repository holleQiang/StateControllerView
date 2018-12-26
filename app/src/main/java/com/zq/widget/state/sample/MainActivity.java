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
        stateFrameLayout.showError();
        stateFrameLayout.getErrorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateFrameLayout.showContent();
            }
        });
        for (View view : stateFrameLayout.getContentViews()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stateFrameLayout.showError();
                }
            });
        }
    }


}
