package com.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityController<T extends Activity> {
    private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

    private T activity;

    public T getActivity() {
        return activity;
    }

    public void setActivity(T activity, Bundle savedInstanceState) {
        this.activity = activity;
    }

    public void startActivity(Context context, Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        SuspensionManager.memorizeActivityController(this, intent);
        context.startActivity(intent);
    }

    public void dispose() {
        logger.info("Disposing " + getClass().getSimpleName());
        SuspensionManager.deleteActivityController(this);
        this.activity = null;
    }

    public void resurrected() { }

    public boolean handleBack() {
        return false;
    }

    public interface ActivityControllerCallback {
        void finished(ActivityController controller);
    }
}
