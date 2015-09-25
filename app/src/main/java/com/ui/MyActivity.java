package com.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import com.framework.SuspensionManager;


public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SuspensionManager.activityCreated(MyActivityController.class, this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuspensionManager.activitySavingInstanceState(this, outState);
    }
}
