package com.framework;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;


public abstract class FragmentController<T extends Fragment> {
    private T fragment;

    public T getFragment() {
        return fragment;
    }

    public void setFragment(T fragment) {
        this.fragment = fragment;
    }

    protected Activity getActivity() { return getFragment().getActivity(); }

    public abstract void fragmentViewCreated(View view, Bundle savedInstanceState);

    public void dispose() {
        SuspensionManager.deleteFragmentController(this);
        fragment = null;
    }

    public boolean handleBack() {
        return false;
    }

    public interface FragmentControllerCallback {
        void finished(FragmentController controller);
    }
}
