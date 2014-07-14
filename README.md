AndroidControllers
==================

Because Android tries to kill/restore activities and fragments, it is sometimes difficult and bug prone to recover from such events. This is an experiment to make the development easier by implementing the following strategy:

*We will separate a major portion of code from activity to a controller. Controller will "survive" UI recreation events, so it can retain both internal state and references to peer controllers without serialization/deserialization*

Let's consider the scenario when an activity has a "Start" button which will show some shared ui ("Click" button), implemented as a fragment, and will receive an event from this fragment to update the Activity's UI. The fragment, being a "shared", cannot reference to the activity type it sits within and should react by calling a callback passed from the creator. 

If we implement this the "standard" Android way, we would have to overwrite the onAttachFragment method, check the type of the fragment and re-wire the callback when the activity goes through kill/restore phase. It would be better it we could just create a fragment controller passing the callback once and not to worry about kill/restore.

This can be achieved by using SuspensionManager. SuspensionManager is like a "shock absorber" between ever-changing Android UI stack and more stable world of controllers. It is an agent who takes care of re-connecting controllers to their activities when activities undergo kill/restore.

**View-First vs. Controller-First**

When you separate controller code from "view" (not the Android View, but view in a wider sense - activity or fragment) you need to be aware what gets created first - view or controller. Generally, Controller-First approach is better, since it allows you to specify dependencies upfront, but Android's way to start activities from intents sometimes makes View-First for activities a requirement. Using SuspensionManager, you can use have your activities both ways - View-First by starting via intent or Controller-First by instantiating ActivityController and calling .startActivity. Fragments for now are always using Controller-First approach.

There are four main calls activities and fragments should execute to have a suspension, two for activity and two for fragment.

**Activity suspension**

- In onCreate, call 
`SuspensionManager.activityCreated(MyActivityController.class, this, savedInstanceState);` 
This will either create an instance of MyActivityController if the activity is not assigned to the controller yet or look up a controller instance previously assigned to this activity and re-attach the activity to the controller.
  
- In onSaveInstanceState, call 
`SuspensionManager.activitySavingInstanceState(this, outState);` 
This will save the controller's id in the outState bundle.

**Fragment suspension**

- In fragment's controller constructor, call 
`SuspensionManager.memorizeFragmentController(this, MyFragment.newInstance());`
This will save the controller's id in the fragment's parameters and assign the fragment to the controller.

- In fragment's onCreateView, call 
`SuspensionManager.fragmentViewCreated(this, view);`
This will re-attach fragment's view to the controller.

Now, you're suspended, and your controller code can be free of kill/restore worries. Look at MyActivityController/MyFragmentController code to see how you pass the callback to the child controller only once, upon creation.              