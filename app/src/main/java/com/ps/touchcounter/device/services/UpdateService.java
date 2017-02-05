package com.ps.touchcounter.device.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;


import com.ps.touchcounter.device.widget.TouchCounterWidget;
import com.ps.touchcounter.ui.touch.TouchCounterActivity;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateService extends IntentService {

    public static final String PARAM_IN_MSG = "in_msg";
    public static final String PARAM_OUT_MSG = "out_msg";
    private long key = 1;
    int touchesPerSecond;

    // Build a circular queue where only 10 elements can be there. whenever new elements pushes,
    // the oldest elements will removed.
    public static Map<Long, Integer> touchesInIntervals = new LinkedHashMap<Long, Integer>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Integer> eldest) {
            return this.size() > 10;
        }
    };

    public UpdateService() {
        super("UpdateService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String msg = intent.getStringExtra(PARAM_IN_MSG);
            while (true) {
                // At  every 100ms interval, count how many touches are there and put as one entry. So in 1 second,
                // it will have 10 entries which fill ups the circular queue.

                SystemClock.sleep(100); // 100 milliseconds
                if (isActivityRunning(TouchCounterActivity.class) && !msg.equalsIgnoreCase("WakeUpAtUserPresence")) {
                    touchesInIntervals.put(key++, TouchCounterActivity.updateTouch(-1));
                    TouchCounterActivity.updateTouch(0);
                    int sum = 0;

                    // the Sum of the circular queue always gives touche rate per second.
                    for (Map.Entry entry : touchesInIntervals.entrySet()) {
                        sum = sum + (int) entry.getValue();
                    }

                    touchesPerSecond = sum;

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(TouchCounterActivity.ResponseReceiver.ACTION_RESP);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putExtra(PARAM_OUT_MSG, String.valueOf(touchesPerSecond));
                    sendBroadcast(broadcastIntent);
                } else if (msg.equalsIgnoreCase("WakeUpAtUserPresence")) {
                    msg = "Touch Rate per second :";
                }

                // Send update msg at every 100 ms
                Intent widgetIntent = new Intent(this, TouchCounterWidget.AppWidgetService.class);
                widgetIntent.putExtra(TouchCounterWidget.AppWidgetService.UPDATE_TOUCH_RATE, msg + " " + touchesPerSecond);
                startService(widgetIntent);
            }
        }

    }

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
}
