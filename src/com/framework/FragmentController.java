package com.framework;

import android.app.Fragment;
import android.view.View;

public class FragmentController<T extends Fragment> {
    private T fragment;

    public T getFragment() {
        return fragment;
    }

    public void setFragment(T fragment) {
        this.fragment = fragment;
    }

    public void fragmentViewCreated(View view) {
    }
}
