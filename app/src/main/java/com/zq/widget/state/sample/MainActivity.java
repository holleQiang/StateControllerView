package com.zq.widget.state.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zq.widget.state.StateFrameLayout;
import com.zq.widget.state.StateViewAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StateFrameLayout stateFrameLayout = (StateFrameLayout) findViewById(R.id.m_state_frame_layout);
        StateViewAdapter adapter = new StateViewAdapter() {

            @Override
            public View getViewWithState(ViewGroup parent, int state, View convertView) {
                if (state == 1) {
                    if (convertView != null) {
                        return convertView;
                    }
                    return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_error, parent, false);
                }else if(state == 2){

                }
                return null;
            }
        };
        adapter.setState(21);
        stateFrameLayout.setAdapter(adapter);
    }


}
