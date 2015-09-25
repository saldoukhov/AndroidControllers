package com.framework;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuspensionManager {

    private static Logger logger = LoggerFactory.getLogger(SuspensionManager.class);

    private static final String CONTROLLER_ID = "controllerId";
    private static HashMap<Long, ActivityController> activityControllers = new HashMap<>();
    private static HashMap<Long, FragmentController> fragmentControllers = new HashMap<>();
    private static ActivityResultHandler activityResultHandler;

    public static void activityCreated(Class<? extends ActivityController> controllerClass, Activity activity, Bundle savedInstanceState) {
        try {
            ActivityController controller;
            long controllerId;
            if (savedInstanceState == null) {
                controller = activityControllers.get(activity.getIntent().getLongExtra(CONTROLLER_ID, 0));
            } else {
                controller = activityControllers.get(savedInstanceState.getLong(CONTROLLER_ID));
            }
            if (controller == null)
                controller = findActivityControllerByClass(controllerClass);
            boolean beenResurrected = false;
            if (controller == null) {
                if (savedInstanceState != null)
                    beenResurrected = true;
                controller = controllerClass.newInstance();
                controllerId = getControllerId();
                activityControllers.put(controllerId, controller);
            }
            controller.setActivity(activity, savedInstanceState);
            if (beenResurrected) {
                logger.info("resurrected activity " + activity.getClass().getSimpleName());
                controller.resurrected();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static ActivityController findActivityControllerByClass(Class<? extends ActivityController> controllerClass) {
        for (Map.Entry<Long, ActivityController> ctrlEntry : activityControllers.entrySet()) {
            ActivityController controller = ctrlEntry.getValue();
            if (controller.getClass() == controllerClass) {
                return controller;
            }
        }
        return null;
    }

    public static FragmentController findFragmentControllerByClass(Class<? extends FragmentController> controllerClass) {
        for (Map.Entry<Long, FragmentController> ctrlEntry : fragmentControllers.entrySet()) {
            FragmentController controller = ctrlEntry.getValue();
            if (controller.getClass() == controllerClass) {
                return controller;
            }
        }
        return null;
    }

    public static void activitySavingInstanceState(Activity activity, Bundle outState) {
        for (Map.Entry<Long, ActivityController> ctrlEntry : activityControllers.entrySet()) {
            if (ctrlEntry.getValue().getActivity() == activity) {
                outState.putLong(CONTROLLER_ID, ctrlEntry.getKey());
                break;
            }
        }
    }

    public static void activityDestroying(Activity activity) {
        if (!activity.isFinishing())
            return;
        for (Map.Entry<Long, ActivityController> ctrlEntry : activityControllers.entrySet()) {
            ActivityController controller = ctrlEntry.getValue();
            if (controller.getActivity() == activity) {
                controller.dispose();
                break;
            }
        }
    }

    public static boolean handleBack(Activity activity) {
        for (Map.Entry<Long, ActivityController> ctrlEntry : activityControllers.entrySet()) {
            ActivityController controller = ctrlEntry.getValue();
            if (controller.getActivity() == activity)
                return controller.handleBack();
        }
        return false;
    }

    public static void memorizeActivityController(ActivityController controller, Intent intent) {
        ActivityController existing = findActivityControllerByClass(controller.getClass());
        if (existing != null)
            existing.dispose();
        long controllerId = getControllerId();
        activityControllers.put(controllerId, controller);
        intent.putExtra(CONTROLLER_ID, controllerId);
    }

    public static void deleteActivityController(ActivityController controller) {
        for (Map.Entry<Long, ActivityController> ctrlEntry : activityControllers.entrySet()) {
            if (ctrlEntry.getValue() == controller) {
                activityControllers.remove(ctrlEntry.getKey());
                break;
            }
        }
    }

    public static void fragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
        Bundle args = fragment.getArguments();
        FragmentController fragmentController = fragmentControllers.get(args.getLong(CONTROLLER_ID));
        if (fragmentController == null) {
            logger.error("Error restoring controller for " + fragment.getClass());
            return;
        }
        fragmentController.setFragment(fragment);
        fragmentController.fragmentViewCreated(view, savedInstanceState);
    }

    public static void memorizeFragmentController(FragmentController controller, Fragment fragment) {
        Bundle bundle = new Bundle();
        long controllerId = getControllerId();
        fragmentControllers.put(controllerId, controller);
        bundle.putLong(CONTROLLER_ID, controllerId);
        fragment.setArguments(bundle);
        controller.setFragment(fragment);
    }

    private static long getControllerId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    public static <T extends Fragment> void deleteFragmentController(FragmentController<T> controller) {
        for (Map.Entry<Long, FragmentController> ctrlEntry : fragmentControllers.entrySet()) {
            if (ctrlEntry.getValue() == controller) {
                fragmentControllers.remove(ctrlEntry.getKey());
                break;
            }
        }
    }

    public static void activityResultReceived(int requestCode, int resultCode, Intent data) {
        logger.info(String.format("Activity result %d received on request %d", resultCode, requestCode));
        if (activityResultHandler != null)
            activityResultHandler.receiveResult(requestCode, resultCode, data);
    }

    public static void setActivityResultHandler(ActivityResultHandler activityResultHandler) {
        SuspensionManager.activityResultHandler = activityResultHandler;
    }

    public interface ActivityResultHandler {
        void receiveResult(int requestCode, int resultCode, Intent data);
    }
}
