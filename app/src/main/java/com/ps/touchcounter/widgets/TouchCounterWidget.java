package com.ps.touchcounter.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.ps.touchcounter.R;
import com.ps.touchcounter.activities.TouchCountActivity;
import com.ps.touchcounter.services.UpdateActivityService;

import java.util.Calendar;


/**
 * Implementation of App Widget functionality.
 */
public class TouchCounterWidget extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msgTouchRate = "WakeUpAtUserPresence";
        Intent msgIntent = new Intent(context, UpdateActivityService.class);
        msgIntent.putExtra(UpdateActivityService.PARAM_IN_MSG, msgTouchRate);
        context.startService(msgIntent);

    }

    public static final class AppWidgetService extends IntentService {
        private static final String TAG = AppWidgetService.class.getSimpleName();
        public static final String UPDATE_TOUCH_RATE = "com.ps.touchcounter.widgets.action.UPDATE_TOUCH_RATE";
        static int counter = 1;
        private Calendar mCalendar;

        public AppWidgetService() {
            super("UpdateActivityService");
        }


        @Override
        public void onCreate() {
            super.onCreate();
            mCalendar = Calendar.getInstance();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            updateTouchRate(intent.getStringExtra(UPDATE_TOUCH_RATE));
        }


        private void updateTouchRate(String touchesPerSecond) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            counter = counter + 1;
            if (counter > 500000) counter = 0;

            RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.touch_counter_widget);
            mRemoteViews.setTextViewText(R.id.timeId,
                    DateFormat.format(getString(R.string.time_format), mCalendar));
            mRemoteViews.setTextViewText(R.id.appwidget_text, touchesPerSecond + " Test Counter : " + counter);

            ComponentName mComponentName = new ComponentName(this, TouchCounterWidget.class);
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
            mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
        }
    }
}

