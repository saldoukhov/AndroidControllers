package com.framework;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuspensionManager {

    private static final String CONTROLLER_ID = "controllerId";
    private static HashMap<Long, ActivityController> activityControllers = new HashMap<Long, ActivityController>();
    private static HashMap<Long, FragmentController> fragmentControllers = new HashMap<Long, FragmentController>();

    public static void activityCreated(Class<? extends ActivityController> controllerClass, Activity activity, Bundle savedInstanceState) {
        try {
            ActivityController controller;
            if (savedInstanceState == null) {
                controller = controllerClass.newInstance();
                long controllerId = getControllerId();
                activityControllers.put(controllerId, controller);
            } else {
                controller = activityControllers.get(savedInstanceState.getLong(CONTROLLER_ID));
            }
            controller.setActivity(activity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static void activitySavingInstanceState(Activity activity, Bundle outState) {
        for (Map.Entry<Long, ActivityController> ctrlEntry : activityControllers.entrySet()) {
            if (ctrlEntry.getValue().getActivity() == activity) {
                outState.putLong(CONTROLLER_ID, ctrlEntry.getKey());
                break;
            }
        }
    }

    public static void fragmentViewCreated(Fragment fragment, View view) {
        FragmentController fragmentController = fragmentControllers.get(fragment.getArguments().getLong(CONTROLLER_ID));
        fragmentController.setFragment(fragment);
        fragmentController.fragmentViewCreated(view);
    }

    public static void memorizeFragmentController(FragmentController fragmentController, Fragment fragment) {
        Bundle bundle = new Bundle();
        long controllerId = getControllerId();
        fragmentControllers.put(controllerId, fragmentController);
        bundle.putLong(CONTROLLER_ID, controllerId);
        fragment.setArguments(bundle);
        fragmentController.setFragment(fragment);
    }

    private static long getControllerId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }
}
