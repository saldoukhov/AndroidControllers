package com.framework;

import android.app.Activity;


public class ActivityController<T extends Activity> {
    private T activity;

    public T getActivity() {
        return activity;
    }

    public void setActivity(T activity) {
        this.activity = activity;
    }
}
