package com.ps.touchcounter.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import com.ps.touchcounter.activities.TouchCountActivity;
import com.ps.touchcounter.widgets.TouchCounterWidget;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateActivityService extends IntentService {

    public static final String PARAM_IN_MSG = "in_msg";
    public static final String PARAM_OUT_MSG = "out_msg";
    private long key = 1;
    static String touchesPerSecond;

    // Build a circular queue where only 10 elements can be there. whenever new elements pushes,
    // the oldest elements will removed.
    public static Map<Long, Integer> touchesInIntervals = new LinkedHashMap<Long, Integer>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Integer> eldest) {
            return this.size() > 10;
        }
    };

    public UpdateActivityService() {
        super("UpdateActivityService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String msg = intent.getStringExtra(PARAM_IN_MSG);
            while (true) {
                 // At  every 100ms interval, count how many touches are there and put as one entry. So in 1 second,
                // it will have 10 entries which fill ups the circular queue.

                SystemClock.sleep(100); // 100 milliseconds
                touchesInIntervals.put(key++, TouchCountActivity.updateTouch(-1));
                TouchCountActivity.updateTouch(0);
                int sum = 0;

                // the Sum of the circular queue always gives touche rate per second.
                for (Map.Entry entry : touchesInIntervals.entrySet()) {
                    sum = sum + (int) entry.getValue();
                }

                touchesPerSecond = String.valueOf(sum);

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(TouchCountActivity.ResponseReceiver.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_OUT_MSG, touchesPerSecond);
                sendBroadcast(broadcastIntent);


                // Send update msg at every 100 ms
                Intent widgetIntent = new Intent(this, TouchCounterWidget.AppWidgetService.class);
                widgetIntent.putExtra(TouchCounterWidget.AppWidgetService.UPDATE_TOUCH_RATE, msg + " "+ touchesPerSecond);
                startService(widgetIntent);
            }
        }

    }
}
