package com.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framework.SuspensionManager;


public class MyFragment extends Fragment {

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        SuspensionManager.fragmentViewCreated(this, view, savedInstanceState);
        return view;
    }

}