package com.baselib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;


import com.baselib.crash.ACUncaughtExceptionHandler;
import com.baselib.log.NLog;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by wenbiao.xie on 2015/9/15.
 */
public class ActivityLifecycleLogger implements Application.ActivityLifecycleCallbacks {

    private static class ActivityReference extends WeakReference<Activity> {
        private ActivityReference(Activity r, ReferenceQueue<? super Activity> q) {
            super(r, q);
        }
    }

    private static final int STATE_BACKGROUND = 0;
    private static final int STATE_SUSPENDED = 1;
    private static final int STATE_FOREGROUND = 2;

    private final static int BACKGROUND_TICK_DELAY = 1000 * 60; // 1 minute
    private final static String TAG = "ActivityLifecycleLogger";

    /**
     * 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中）
     */
    private final ReferenceQueue<Activity> sReferenceQueue = new ReferenceQueue<Activity>();
    private final LinkedList<ActivityReference> sStack = new LinkedList<ActivityReference>();

    private int aliveActivities;
    private int paused;
    private int resumed;

    private int state = STATE_BACKGROUND;
    private Handler handler = new Handler();
    private int delay = BACKGROUND_TICK_DELAY;
    private final ApplicationLifecycleDelegate applicationLifecycleDelegate;
    private Context applicationContext;

    public ActivityLifecycleLogger() {
        this(null);
    }

    public ActivityLifecycleLogger(ApplicationLifecycleDelegate delegate) {
        this.applicationLifecycleDelegate = delegate == null ? ApplicationLifecycleDelegate.DEFAULT : delegate;

        delay = applicationLifecycleDelegate.backgroundTickDelay();
        if (delay == 0)
            delay = BACKGROUND_TICK_DELAY;

    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        NLog.v(activity.getClass().getSimpleName(), "onCreated");
        ACUncaughtExceptionHandler.setContextActivity(this.getClass());
        ActivityStackHelper.push(activity);
        aliveActivities++;
        if (applicationContext == null)
            applicationContext = activity.getApplicationContext();

    }

    @Override
    public void onActivityStarted(Activity activity) {
        NLog.v(activity.getClass().getSimpleName(), "onStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        NLog.v(activity.getClass().getSimpleName(), "onResumed");
        resume(activity);
        maybeEnterForeground();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        NLog.v(activity.getClass().getSimpleName(), "onPaused");
        pause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        NLog.v(activity.getClass().getSimpleName(), "onStopped");
        if (aliveActivities == pausedActivities()) {
            maybeEnterBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        NLog.v(activity.getClass().getSimpleName(), "onSaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        NLog.v(activity.getClass().getSimpleName(), "onDestroyed");
        ActivityStackHelper.remove(activity);
        boolean removed = remove(sStack, activity);
        if (removed) {
            paused--;
        }
        aliveActivities--;

    }

    ActivityReference push(LinkedList<ActivityReference> stack, Activity activity) {
        ActivityReference ref = new ActivityReference(activity, sReferenceQueue);
        stack.push(ref);
        return ref;
    }

    private void pause(Activity activity) {
        resumed--;
        paused++;
        push(sStack, activity);
    }

    private void resume(Activity activity) {
        boolean removed = remove(sStack, activity);
        if (removed) {
            paused--;
        }

        resumed++;
    }

    boolean remove(LinkedList<ActivityReference> stack, Activity activity) {
        Iterator<ActivityReference> it = stack.iterator();
        while (it.hasNext()) {
            ActivityReference ref = it.next();
            if (ref != null && ref.get() == activity) {
                it.remove();
                return true;
            }
        }

        return false;
    }

    private int pausedActivities() {
        return Math.max(0, paused);
    }

    private void maybeEnterBackground() {
        NLog.v(TAG, "maybeEnterBackground");
        if (state == STATE_SUSPENDED)
            return;

        state = STATE_SUSPENDED;
        handler.removeCallbacks(delayAction);
        handler.postDelayed(delayAction, BACKGROUND_TICK_DELAY);
        willEnterBackground();
    }

    private void maybeEnterForeground() {
        NLog.v(TAG, "maybeEnterForeground");
        if (state == STATE_FOREGROUND)
            return;

        int old = state;
        if (old == STATE_SUSPENDED) {
            handler.removeCallbacks(delayAction);
        }

        state = STATE_FOREGROUND;
        if (old == STATE_BACKGROUND) {
            enterForeground();
        } else {
            becomeActiveFromSuspend();
        }
    }

    private Runnable delayAction = new Runnable() {
        @Override
        public void run() {
            state = STATE_BACKGROUND;
            enterBackground();
        }
    };

    protected void enterBackground() {
        NLog.v(TAG, "enterBackground");
        applicationLifecycleDelegate.enterBackground(applicationContext);

    }

    protected void enterForeground() {
        NLog.v(TAG, "enterForeground");
        applicationLifecycleDelegate.enterForeground(applicationContext);
    }

    protected void willEnterBackground() {
        NLog.v(TAG, "willEnterBackground");
        applicationLifecycleDelegate.willEnterBackground(applicationContext);
    }

    protected void becomeActiveFromSuspend() {
        NLog.v(TAG, "becomeActiveFromSuspend");
        applicationLifecycleDelegate.becomeActiveFromSuspend(applicationContext);
    }

    public interface ApplicationLifecycleDelegate {
        int backgroundTickDelay();

        void willEnterBackground(Context context);

        void enterBackground(Context context);

        void becomeActiveFromSuspend(Context context);

        void enterForeground(Context context);

        ApplicationLifecycleDelegate DEFAULT = new ApplicationLifecycleDelegate() {
            @Override
            public int backgroundTickDelay() {return 0;}

            @Override
            public void willEnterBackground(Context context) {}

            @Override
            public void enterBackground(Context context) {}

            @Override
            public void becomeActiveFromSuspend(Context context) {}

            @Override
            public void enterForeground(Context context) {}
        };
    }


}
