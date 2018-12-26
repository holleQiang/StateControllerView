package com.zq.widget.state;

import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

/**
 * Author：zhangqiang
 * Date：2018/12/26 13:11:16
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public abstract class ViewAdapter {

    private InternalObservable mObservable = new InternalObservable();
    abstract View getView(ViewGroup parent);

    void registerObserver(Observer observer){
        mObservable.addObserver(observer);
    }

    void unRegisterObserver(Observer observer){
        mObservable.deleteObserver(observer);
    }

    public void notifyChange(){
        mObservable.setChanged();
        mObservable.notifyObservers();
    }

    private class InternalObservable extends Observable{

        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
    }
}
