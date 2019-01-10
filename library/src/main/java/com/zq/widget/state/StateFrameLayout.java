package com.zq.widget.state;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
    @State
    private int mCurrentState = STATE_CONTENT;
    private static final int INVALID_LAYOUT_RES = -1;
    private int mErrorLayoutId;
    private int mLoadingLayoutId;
    private int mEmptyLayoutId;
    private List<View> mContentViews = new ArrayList<>();
    private View mErrorView;
    private View mLoadingView;
    private View mEmptyView;
    private OnStateChangeListener onStateChangeListener;

    @IntDef({STATE_CONTENT,STATE_LOADING,STATE_ERROR,STATE_EMPTY})
    @interface State{

    }

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
            mErrorLayoutId = t.getResourceId(R.styleable.StateFrameLayout_errorLayoutId, INVALID_LAYOUT_RES);
            mLoadingLayoutId = t.getResourceId(R.styleable.StateFrameLayout_loadingLayoutId, INVALID_LAYOUT_RES);
            mEmptyLayoutId = t.getResourceId(R.styleable.StateFrameLayout_emptyLayoutId, INVALID_LAYOUT_RES);
            mCurrentState = t.getInt(R.styleable.StateFrameLayout_currentState,STATE_CONTENT);
            t.recycle();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        List<View> contentViews = new ArrayList<>();
        View emptyView = null;
        View errorView = null;
        View loadingView = null;

        if (mErrorLayoutId != INVALID_LAYOUT_RES) {
            errorView = LayoutInflater.from(getContext()).inflate(mErrorLayoutId, this, false);
        }
        if (mLoadingLayoutId != INVALID_LAYOUT_RES) {
            loadingView = LayoutInflater.from(getContext()).inflate(mLoadingLayoutId, this, false);
        }
        if (mEmptyLayoutId != INVALID_LAYOUT_RES) {
            emptyView = LayoutInflater.from(getContext()).inflate(mEmptyLayoutId, this, false);
        }

        int emptyViewCount = 0;
        int errorViewCount = 0;
        int loadingViewCount = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            int viewType = layoutParams.getViewType();
            if (viewType == LayoutParams.VIEW_TYPE_CONTENT) {
                contentViews.add(child);
            }else if(viewType == LayoutParams.VIEW_TYPE_EMPTY){
                if (emptyViewCount >= 1) {
                    throw new IllegalArgumentException("StateFrameLayout can only has one emptyView");
                }
                emptyView = child;
                emptyViewCount ++;
            }else if(viewType == LayoutParams.VIEW_TYPE_ERROR){
                if (errorViewCount >= 1) {
                    throw new IllegalArgumentException("StateFrameLayout can only has one emptyView");
                }
                errorView = child;
                errorViewCount ++;
            }else if(viewType == LayoutParams.VIEW_TYPE_LOADING){
                if (loadingViewCount >= 1) {
                    throw new IllegalArgumentException("StateFrameLayout can only has one emptyView");
                }
                loadingView = child;
                loadingViewCount ++;
            }else {
                throw new IllegalArgumentException("known view type : " + viewType);
            }
        }
        setContentViews(contentViews);
        setEmptyView(emptyView);
        setLoadingView(loadingView);
        setErrorView(errorView);
        setCurrentState(getCurrentState());
    }

    public void showContent(){
        for (View mContentView : mContentViews) {
            mContentView.setVisibility(View.VISIBLE);
        }
        hideEmpty();
        hideError();
        hideLoading();
        notifyStateChange(STATE_CONTENT);
    }

    public void showError(){
        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
        }
        hideEmpty();
        hideContent();
        hideLoading();
        notifyStateChange(STATE_ERROR);
    }

    public void showLoading(){
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        hideEmpty();
        hideError();
        hideContent();
        notifyStateChange(STATE_LOADING);
    }

    public void showEmpty(){
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        hideContent();
        hideError();
        hideLoading();
        notifyStateChange(STATE_EMPTY);
    }

    private void hideContent(){
        for (View mContentView : mContentViews) {
            mContentView.setVisibility(View.GONE);
        }
    }

    private void hideLoading(){
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    private void hideEmpty(){
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
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

    private void notifyStateChange(int currState){
        final int previousState = mCurrentState;
        mCurrentState = currState;
        boolean changed = previousState != mCurrentState;
        if (changed && onStateChangeListener != null) {
            onStateChangeListener.onStateChange(this,previousState,mCurrentState);
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

    @State
    public int getCurrentState() {
        return mCurrentState;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(super.generateLayoutParams(lp));
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(),attrs);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams{

        public static final int VIEW_TYPE_CONTENT = 0;
        public static final int VIEW_TYPE_LOADING = 1;
        public static final int VIEW_TYPE_ERROR = 2;
        public static final int VIEW_TYPE_EMPTY = 3;

        @Documented
        @Retention(RetentionPolicy.SOURCE)
        @IntDef({VIEW_TYPE_CONTENT,VIEW_TYPE_LOADING,VIEW_TYPE_ERROR,VIEW_TYPE_EMPTY})
        @interface ViewType{ }

        @ViewType
        private int viewType = VIEW_TYPE_CONTENT;

        public LayoutParams( @NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.StateFrameLayout_Layout);
            if (typedArray != null) {
                viewType = typedArray.getInt(R.styleable.StateFrameLayout_Layout_viewType,VIEW_TYPE_CONTENT);
                typedArray.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams( @NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams( @NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public LayoutParams(@NonNull FrameLayout.LayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(@NonNull LayoutParams source) {
            super(source);
            viewType = source.getViewType();
        }

        @ViewType
        public int getViewType() {
            return viewType;
        }

        public LayoutParams setViewType(@ViewType int viewType) {
            this.viewType = viewType;
            return this;
        }
    }



    public StateFrameLayout setContentViews(List<View> contentViews) {
        final List<View> prevContentViews = mContentViews;
        if (prevContentViews != null && !prevContentViews.isEmpty()) {
            for (View child : prevContentViews) {
                removeView(child);
            }
        }
        this.mContentViews = contentViews;
        if (mContentViews != null && !mContentViews.isEmpty()) {
            for (View child : mContentViews) {
                if (isChild(child)) {
                    continue;
                }
                addView(child);
            }
        }
        return this;
    }

    public StateFrameLayout setContentView(View contentView) {
        return setContentViews(Collections.singletonList(contentView));
    }

    public StateFrameLayout setErrorView(View errorView) {
        final View prevView = mErrorView;
        if (prevView != null) {
            removeView(prevView);
        }
        this.mErrorView = errorView;
        if (mErrorView != null&& !isChild(mErrorView)) {
            addView(mErrorView);
        }
        return this;
    }


    public StateFrameLayout setLoadingView(View loadingView) {
        final View prevView = mLoadingView;
        if (prevView != null) {
            removeView(prevView);
        }
        this.mLoadingView = loadingView;
        if (mLoadingView != null && !isChild(mLoadingView)) {
            addView(mLoadingView);
        }
        return this;
    }


    public StateFrameLayout setEmptyView(View emptyView) {
        final View prevView = mEmptyView;
        if (prevView != null) {
            removeView(prevView);
        }
        this.mEmptyView = emptyView;
        if (mEmptyView != null && !isChild(mEmptyView)) {
            addView(mEmptyView);
        }
        return this;
    }

    private boolean isChild(View child){
        return child != null && StateFrameLayout.this.equals(child.getParent());
    }

    public StateFrameLayout setCurrentState(@State int currentState) {
        this.mCurrentState = currentState;
        switch (mCurrentState){
            case STATE_CONTENT:
                showContent();
                break;
            case STATE_EMPTY:
                showEmpty();
                break;
            case STATE_ERROR:
                showError();
                break;
            case STATE_LOADING:
                showLoading();
                break;
        }
        return this;
    }
}
