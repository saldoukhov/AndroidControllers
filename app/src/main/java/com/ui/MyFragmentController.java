package com.ui;

import android.os.Bundle;
import android.view.View;
import com.framework.FragmentController;
import com.framework.SuspensionManager;


public class MyFragmentController extends FragmentController<MyFragment> implements ResultActivityController.ResultActivityControllerCallback {

    private MyFragmentCallback callback;

    public MyFragmentController(MyFragmentCallback callback) {
        this.callback = callback;
        SuspensionManager.memorizeFragmentController(this, MyFragment.newInstance());
    }

    @Override
    public void fragmentViewCreated(View view, final Bundle savedInstanceState) {
        view.findViewById(R.id.button_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentController.this.callback.executeClick();
            }
        });

        view.findViewById(R.id.button_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivityController controller = new ResultActivityController(MyFragmentController.this);
                controller.setInitValue("InitValue");
                controller.startActivity(getFragment().getActivity(), ResultActivity.class);
            }
        });
    }

    @Override
    public void executeActivity(String value) {
        callback.executeActivity(value);
    }

    public interface MyFragmentCallback {
        void executeClick();
        void executeActivity(String value);
    }
}
