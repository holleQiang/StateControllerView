package com.zq.widget.state;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author：zhangqiang
 * Date：2018/12/26 13:11:28
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public class StateFrameLayout extends FrameLayout{

    public static final int STATE_CONTENT = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_EMPTY  = 4;
    private int mCurrentState;
    private static final int INVALID_LAYOUT_RES = -1;
    private List<View> mContentViews = new ArrayList<>();
    private int mContentLayoutId;
    private int mErrorLayoutId;
    private int mLoadingLayoutId;
    private int mEmptyLayoutId;
    private View mErrorView;
    private View mLoadingView;
    private View mEmptyView;
    private OnStateChangeListener onStateChangeListener;

    public StateFrameLayout(@NonNull Context context) {
        super(context);
        init(context,null,0);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        if (attrs != null) {
            TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.StateFrameLayout, R.attr.StateFrameLayoutStyle, defStyleAttr);
            mContentLayoutId = t.getResourceId(R.styleable.StateFrameLayout_contentLayoutId, INVALID_LAYOUT_RES);
            mErrorLayoutId = t.getResourceId(R.styleable.StateFrameLayout_errorLayoutId, INVALID_LAYOUT_RES);
            mLoadingLayoutId = t.getResourceId(R.styleable.StateFrameLayout_loadingLayoutId, INVALID_LAYOUT_RES);
            mEmptyLayoutId = t.getResourceId(R.styleable.StateFrameLayout_emptyLayoutId, INVALID_LAYOUT_RES);
            t.recycle();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            mContentViews.add(child);
        }
        if (mContentLayoutId != INVALID_LAYOUT_RES) {
            mContentViews.clear();
            View contentView = LayoutInflater.from(getContext()).inflate(mContentLayoutId, this, false);
            addView(contentView);
            mContentViews.add(contentView);
        }
        if (mErrorLayoutId != INVALID_LAYOUT_RES) {
            mErrorView = LayoutInflater.from(getContext()).inflate(mErrorLayoutId, this, false);
            addView(mErrorView);
        }
        if (mLoadingLayoutId != INVALID_LAYOUT_RES) {
            mLoadingView = LayoutInflater.from(getContext()).inflate(mLoadingLayoutId, this, false);
            addView(mLoadingView);
        }
        if (mEmptyLayoutId != INVALID_LAYOUT_RES) {
            mEmptyView = LayoutInflater.from(getContext()).inflate(mEmptyLayoutId, this, false);
            addView(mEmptyView);
        }
        showContent();
    }

    public void showContent(){
        for (View mContentView : mContentViews) {
            mContentView.setVisibility(View.VISIBLE);
        }
        hideEmpty();
        hideError();
        hideLoading();
        final int previousState = mCurrentState;
        mCurrentState = STATE_CONTENT;
        notifyStateChange(previousState,mCurrentState);
    }

    public void showError(){
        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
        }
        hideEmpty();
        hideContent();
        hideLoading();
        final int previousState = mCurrentState;
        mCurrentState = STATE_ERROR;
        notifyStateChange(previousState,mCurrentState);
    }

    public void showLoading(){
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        hideEmpty();
        hideError();
        hideContent();
        final int previousState = mCurrentState;
        mCurrentState = STATE_LOADING;
        notifyStateChange(previousState,mCurrentState);
    }

    public void showEmpty(){
        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
        }
        hideContent();
        hideError();
        hideLoading();
        final int previousState = mCurrentState;
        mCurrentState = STATE_EMPTY;
        notifyStateChange(previousState,mCurrentState);
    }

    private void hideContent(){
        for (View mContentView : mContentViews) {
            mContentView.setVisibility(View.GONE);
        }
    }

    private void hideLoading(){
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmpty(){
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
    private void hideError(){
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public StateFrameLayout setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
        return this;
    }

    private void notifyStateChange(int preState,int currState){
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(this,preState,currState);
        }
    }

    public List<View> getContentViews() {
        return mContentViews;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public List<View> getViewByState(int state) {
        switch (state) {
            case StateFrameLayout.STATE_CONTENT:
                return getContentViews();
            case StateFrameLayout.STATE_LOADING:
                View loadingView = getLoadingView();
                if (loadingView != null) {
                    return Collections.singletonList(loadingView);
                }
            case StateFrameLayout.STATE_EMPTY:
                View emptyView = getEmptyView();
                if (emptyView != null) {
                    return Collections.singletonList(emptyView);
                }
            case StateFrameLayout.STATE_ERROR:
                View errorView = getErrorView();
                if (errorView != null) {
                    return Collections.singletonList(errorView);
                }
        }
        return null;
    }
}
