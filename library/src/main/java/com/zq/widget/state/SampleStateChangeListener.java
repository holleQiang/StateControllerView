package com.zq.widget.state;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author：zhangqiang
 * Date：2018/12/26 19:17:12
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public class SampleStateChangeListener implements OnStateChangeListener {
    @Override
    public void onStateChange(StateFrameLayout layout, int previousState, int currentState) {
        List<View> outViews = layout.getViewByState(previousState);
        List<View> inViews = layout.getViewByState(currentState);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        List<Animator> animators = new ArrayList<>();
        if (outViews != null) {
            for (View outView : outViews) {
               animators.add(ObjectAnimator.ofFloat(outView,"alpha",0));
            }
        }
        if (inViews != null) {
            for (View inView : inViews) {
                animators.add(ObjectAnimator.ofFloat(inView,"alpha",1));
            }
        }
        animatorSet.playTogether(animators);
        animatorSet.start();
    }
}
