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
import com.ps.touchcounter.services.UpdateActivityService;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class TouchCounterWidget extends AppWidgetProvider {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.ps.touchcounter.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AppWidgetService.class);
        context.startService(i);
    }

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
//
////        context.startService(new Intent(UpdateTimeService.UPDATE_TIME));
//        //  context.startService(new Intent(UpdateTimeService.UPDATE_TIME).setPackage("com.ps.digitalclock"));
//        context.startService(new Intent(AppWidgetService.UPDATE_TOUCH_RATE).setPackage("com.ps.touchcounter"));
//        //context.startService(new Intent(context,AppWidgetService.class));
//
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        super.onDisabled(context);
//
////        context.stopService(new Intent(context, UpdateTimeService.class));
//        context.stopService(new Intent(context,AppWidgetService.class));
//    }

    public static  final class AppWidgetService extends Service {
        private static final String TAG = AppWidgetService.class.getSimpleName();
       // AlarmManager alarmManager;
        static final String UPDATE_TOUCH_RATE = "com.ps.touchcounter.widgets.action.UPDATE_TOUCH_RATE";
        private final static IntentFilter mIntentFilter = new IntentFilter();
        static int counter=1;
        private Calendar mCalendar;

//        public AppWidgetService(){
//            super("UpdateActivityService");
//        }

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
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            if (intent != null) {
              //  if (UPDATE_TOUCH_RATE.equals(intent.getAction())) {
                    //updateTime();
                    useHandlerForFrequentCall();
                //}
            }

            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }


//        private final BroadcastReceiver mTouchRateChangedReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                updateTime();
//            }
//        };



        private void updateTime() {
            Log.d(TAG, "Running update  "+ "time");
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            counter = counter + 1;
            RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.touch_counter_widget);
            mRemoteViews.setTextViewText(R.id.timeId,
                    DateFormat.format(getString(R.string.time_format), mCalendar));
            if (isMyServiceRunning(UpdateActivityService.class)) {
                mRemoteViews.setTextViewText(R.id.appwidget_text, UpdateActivityService.resultTxt + " " + counter);
            } else {
                mRemoteViews.setTextViewText(R.id.appwidget_text, "Test " + ": " + counter);
            }
            ComponentName mComponentName = new ComponentName(this, TouchCounterWidget.class);
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
            mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
        }

        private boolean isMyServiceRunning(Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            //        Intent i = new Intent(context, AppWidgetService.class);
////        context.startService(i);
//            Log.d(TAG, "Service Running "+intent.getAction());
//            useHandlerForFrequentCall();
//        }

        private void useHandlerForFrequentCall(){
            Handler handler = new Handler();
            for(int i=0; i<600; i++) {
                handler.postDelayed(runnableCode, 100);

            }
        }

        private Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                updateTime();

            }
        };

    }
}

