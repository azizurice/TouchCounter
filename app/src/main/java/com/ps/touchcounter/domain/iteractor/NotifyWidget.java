
package com.ps.touchcounter.domain.iteractor;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.ps.touchcounter.R;
import com.ps.touchcounter.device.services.UpdateService;
import com.ps.touchcounter.device.widget.WidgetNotifierImp;
import com.ps.touchcounter.domain.repository.IWidgetNotifier;


import java.util.Calendar;


/**
 * Implementation of App Widget functionality.
 */
public class NotifyWidget extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msgTouchRate = "WakeUpAtUserPresence";
        Intent msgIntent = new Intent(context, UpdateService.class);
        msgIntent.putExtra(UpdateService.PARAM_IN_MSG, msgTouchRate);
        context.startService(msgIntent);

    }

    public static final class AppWidgetService extends IntentService {
        private static final String TAG = AppWidgetService.class.getSimpleName();
        public static final String UPDATE_TOUCH_RATE = "com.ps.touchcounter.domain.iteractor.action.UPDATE_TOUCH_RATE";
        static int counter = 1;
        private Calendar mCalendar;
        IWidgetNotifier iWidgetNotifier;
        public AppWidgetService() {
            super("UpdateService");
            iWidgetNotifier=new WidgetNotifierImp();
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

            iWidgetNotifier.updateWidget(this,touchesPerSecond,counter,mCalendar);

        }
    }
}

