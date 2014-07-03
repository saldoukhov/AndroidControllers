package com.ui;

import android.view.View;
import com.framework.FragmentController;
import com.framework.SuspensionManager;


public class MyFragmentController extends FragmentController<MyFragment> {

    private MyFragmentCallback callback;

    public MyFragmentController(MyFragmentCallback callback) {
        this.callback = callback;
        SuspensionManager.memorizeFragmentController(this, MyFragment.newInstance());
    }

    @Override
    public void fragmentViewCreated(View view) {
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentController.this.callback.execute();
            }
        });
    }

    public interface MyFragmentCallback {
        void execute();
    }
}
