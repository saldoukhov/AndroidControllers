package com.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.framework.ActivityController;

public class ResultActivityController extends ActivityController<ResultActivity> {

    private EditText editText;
    private ResultActivityControllerCallback callback;
    private String initValue;

    public ResultActivityController(ResultActivityControllerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void setActivity(ResultActivity activity) {
        super.setActivity(activity);
        editText = (EditText) activity.findViewById(R.id.entry_field);
        editText.setText(initValue);
        activity.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneClicked();
            }
        });
    }

    private void doneClicked() {
        getActivity().finish();
        callback.executeActivity(editText.getText().toString());
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public interface ResultActivityControllerCallback {
        void executeActivity(String value);
    }
}
