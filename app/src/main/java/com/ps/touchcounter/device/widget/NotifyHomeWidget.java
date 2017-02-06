package com.ps.touchcounter.device.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.ps.touchcounter.R;
import com.ps.touchcounter.domain.iteractor.NotifyWidget;
import com.ps.touchcounter.domain.repository.IWidgetNotifier;

import java.util.Calendar;

/**
 * Created by Azizur on 05/02/2017.
 */

public class NotifyHomeWidget implements IWidgetNotifier {
    @Override
    public void updateWidget(Context context, String msgWithTouchRate, int counter, Calendar calendar) {
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.touch_counter_widget);
        mRemoteViews.setTextViewText(R.id.timeId,
                DateFormat.format(context.getString(R.string.time_format), calendar));
        mRemoteViews.setTextViewText(R.id.appwidget_text, msgWithTouchRate + " Test Counter : " + counter);

        ComponentName mComponentName = new ComponentName(context, NotifyWidget.class);
        AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);

    }
}
