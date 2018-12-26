package com.zq.widget.state;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：zhangqiang
 * Date：2018/12/26 14:18:12
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public abstract class StateViewAdapter extends ViewAdapter {

    private SparseArray<View> stateViewCache = new SparseArray<>();

    private int state;

    @Override
    View getView(ViewGroup parent) {
        View viewWithState = getViewWithState(parent, state, stateViewCache.get(state));
        stateViewCache.put(state,viewWithState);
        return viewWithState;
    }

    public abstract View getViewWithState(ViewGroup parent, int state,View convertView);

    public int getState() {
        return state;
    }

    public StateViewAdapter setState(int state) {
        if (this.state != state) {
            this.state = state;
            notifyChange();
        }
        return this;
    }

}
