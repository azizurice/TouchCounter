package com.ps.touchcounter.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ps.touchcounter.BaseActivity;
import com.ps.touchcounter.R;
import com.ps.touchcounter.services.UpdateActivityService;
import com.ps.touchcounter.widgets.TouchCounterWidget;

import java.util.LinkedHashMap;
import java.util.Map;

public class TouchCountActivity extends BaseActivity {
    TextView mTextView;
    public static int nTouch = 0;
    String msg;
    private ResponseReceiver receiver;
    public static Map<Long, Integer> touchesInIntervals = new LinkedHashMap<Long, Integer>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Integer> eldest) {
            return this.size() > 10;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_count);

        // Register receiver with appropriate intent filter
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);


        mTextView = (TextView) findViewById(R.id.touchId);
        msg = "Touch Rate per second :";

        // Start the service that will continusly calculate touches per second from
        // the queue like map (10 entries) becasue 10 times update per second.
        Intent msgIntent = new Intent(this, UpdateActivityService.class);
        msgIntent.putExtra(UpdateActivityService.PARAM_IN_MSG, msg);
        startService(msgIntent);

        scheduleAlarm();
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), TouchCounterWidget.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, TouchCounterWidget.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                1000, pIntent);  // force it delayed 1 minute or 60000ms


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nTouch++;
                mTextView.setText(msg + String.valueOf(nTouch) + "  " + event.getEventTime());


        }
        return false;
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "com.ps.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            TextView mResult = (TextView) findViewById(R.id.resultViewId);
            String text = intent.getStringExtra(UpdateActivityService.PARAM_OUT_MSG);
            mResult.setText(text);
        }
    }
}