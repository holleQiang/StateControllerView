package com.zq.widget.state;

/**
 * 状态变化监听
 * Author：zhangqiang
 * Date：2018/12/26 19:01:42
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public interface OnStateChangeListener {

    void onStateChange(StateFrameLayout layout,int previousState,int currentState);
}
