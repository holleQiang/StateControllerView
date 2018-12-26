package com.zq.widget.state;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Author：zhangqiang
 * Date：2018/12/26 13:11:28
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public class StateFrameLayout extends FrameLayout{

    private ViewAdapter mStateAdapter;
    private View mChildViewFromXml;

    public StateFrameLayout(@NonNull Context context) {
        super(context);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 1) {
            mChildViewFromXml = getChildAt(0);
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("state frame layout can only has one child");
        }
    }

    public void setAdapter(ViewAdapter adapter){
        if (mStateAdapter != null) {
            mStateAdapter.unRegisterObserver(mObserver);
        }
        mStateAdapter = adapter;
        if (mStateAdapter != null) {
            mStateAdapter.registerObserver(mObserver);
            mStateAdapter.notifyChange();
        }
    }

    private Observer mObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {

            if (getChildCount() > 0) {
                removeAllViews();
            }
            View view = mStateAdapter.getView(StateFrameLayout.this);
            if (view != null) {
                addView(view);
            }else if(mChildViewFromXml != null){
                addView(mChildViewFromXml);
            }
        }
    };

    public ViewAdapter getAdapter() {
        return mStateAdapter;
    }
}
