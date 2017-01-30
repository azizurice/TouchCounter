//package com.ps.touchcounter.services;
//
//import android.app.ActivityManager;
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.Service;
//import android.appwidget.AppWidgetManager;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.IBinder;
//import android.text.format.DateFormat;
//import android.widget.RemoteViews;
//
//import com.ps.touchcounter.R;
//import com.ps.touchcounter.widgets.TouchCounterWidget;
//
//import java.util.Calendar;
//
//
//public class AppWidgetService extends Service {
//    AlarmManager alarmManager;
//    public static final String UPDATE_TOUCH_RATE = "com.ps.touchcounter.action.UPDATE_TOUCH_RATE";
//    private final static IntentFilter mIntentFilter = new IntentFilter();
//    static int counter=1;
//    private Calendar mCalendar;
//
//
//    static {
//       // mIntentFilter.addAction(UPDATE_TOUCH_RATE);
//        mIntentFilter.addAction(Intent.ACTION_TIME_TICK);
//        mIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
//        mIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
//
//    }
//
//
//    @Override
//    public void onCreate(){
//        super.onCreate();
//        mCalendar = Calendar.getInstance();
//        registerReceiver(mTouchRateChangedReceiver, mIntentFilter);
//     //  alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mTouchRateChangedReceiver);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//        if (intent != null) {
//            if (UPDATE_TOUCH_RATE.equals(intent.getAction())) {
//                updateTime();
//            }
//        }
//
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    private final BroadcastReceiver mTouchRateChangedReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            updateTime();
//        }
//    };
//
//
//
//    private void updateTime() {
//        mCalendar.setTimeInMillis(System.currentTimeMillis());
//        counter = counter + 1;
//        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.touch_counter_widget);
//        mRemoteViews.setTextViewText(R.id.timeId,
//                DateFormat.format(getString(R.string.time_format), mCalendar));
//        if (isMyServiceRunning(UpdateActivityService.class)) {
//          mRemoteViews.setTextViewText(R.id.appwidget_text, UpdateActivityService.resultTxt + " " + counter);
//        } else {
//            mRemoteViews.setTextViewText(R.id.appwidget_text, "Test " + ": " + counter);
//        }
//        ComponentName mComponentName = new ComponentName(this, TouchCounterWidget.class);
//        AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
//        mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
//    }
//
//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//}
//
