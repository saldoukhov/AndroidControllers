package com.ui;

import android.app.Activity;
import android.os.Bundle;
import com.framework.SuspensionManager;

public class ResultActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        SuspensionManager.activityCreated(ResultActivityController.class, this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuspensionManager.activitySavingInstanceState(this, outState);
    }
}
