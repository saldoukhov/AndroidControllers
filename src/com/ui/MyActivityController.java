package com.ui;

import android.view.View;
import android.widget.TextView;
import com.framework.ActivityController;

public class MyActivityController extends ActivityController<MyActivity> implements MyFragmentController.MyFragmentCallback {

    private TextView textView;
    private String text;

    @Override
    public void setActivity(MyActivity activity) {
        super.setActivity(activity);
        textView = (TextView) activity.findViewById(R.id.text);
        if (text != null)
            textView.setText(text);
        activity.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClicked();
            }
        });
    }

    private void startClicked() {
        getActivity()
                .getFragmentManager()
                .beginTransaction()
                .add(R.id.content, new MyFragmentController(this).getFragment())
                .commit();
    }

    @Override
    public void execute() {
        if (text == null)
            text = "Click";
        else
            text = text + "Click";
        textView.setText(text);
    }
}
