package com.ps.touchcounter.widgets;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.ps.touchcounter.R;
import com.ps.touchcounter.activities.LoginActivity;
//import com.ps.touchcounter.services.UpdateActivityService;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class TouchCounterWidget extends AppWidgetProvider {

    //public static final int REQUEST_CODE = 12345;
    // Triggered by the Alarm periodically but we are not using AlarmManager, so comment it
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent i = new Intent(context, AppWidgetService.class);
//        context.startService(i);
//    }

//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        context.startService(new Intent(AppWidgetService.UPDATE_TOUCH_RATE).setPackage("com.ps.touchcounter"));
//        //context.startService(new Intent(context,AppWidgetService.class));
//    }
//
//
//    @Override
//    public void onEnabled(Context context) {
//        super.onEnabled(context);
 //        context.startService(new Intent(AppWidgetService.UPDATE_TOUCH_RATE).setPackage("com.ps.touchcounter"));
//        //context.startService(new Intent(context,AppWidgetService.class));
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        super.onDisabled(context);
//
////        context.stopService(new Intent(context, UpdateTimeService.class));
//        context.stopService(new Intent(context,AppWidgetService.class));
//    }

    public static  final class AppWidgetService extends IntentService {
        private static final String TAG = AppWidgetService.class.getSimpleName();
       // AlarmManager alarmManager;
        public static final String UPDATE_TOUCH_RATE = "com.ps.touchcounter.widgets.action.UPDATE_TOUCH_RATE";
        private final static IntentFilter mIntentFilter = new IntentFilter();
        static int counter=1;
        private Calendar mCalendar;

        public AppWidgetService(){
            super("UpdateActivityService");
        }

//        static {
//            // mIntentFilter.addAction(UPDATE_TOUCH_RATE);
//            mIntentFilter.addAction(Intent.ACTION_TIME_TICK);
//            mIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
//            mIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
//
//        }


        @Override
        public void onCreate(){
            super.onCreate();
            mCalendar = Calendar.getInstance();
  //          registerReceiver(mTouchRateChangedReceiver, mIntentFilter);

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
         //   unregisterReceiver(mTouchRateChangedReceiver);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            updateTouchRate(intent.getStringExtra(UPDATE_TOUCH_RATE));
        }

        private void updateTouchRate(String touchRate) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            counter = counter + 1;
            if (counter>500000) counter=0;

            RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.touch_counter_widget);
            mRemoteViews.setTextViewText(R.id.timeId,
                    DateFormat.format(getString(R.string.time_format), mCalendar));
            mRemoteViews.setTextViewText(R.id.appwidget_text, touchRate + " Test Counter : " + counter);

            ComponentName mComponentName = new ComponentName(this, TouchCounterWidget.class);
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
            mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
        }
    }
}

