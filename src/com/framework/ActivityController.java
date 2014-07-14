package com.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.ParameterizedType;

public class ActivityController<T extends Activity> {
    private T activity;

    public T getActivity() {
        return activity;
    }

    public void setActivity(T activity) {
        this.activity = activity;
    }

    public void startActivity(Context context) {
        Intent intent = new Intent(context, (Class<?>) (((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
        SuspensionManager.memorizeActivityController(this, intent);
        context.startActivity(intent);
    }
}
