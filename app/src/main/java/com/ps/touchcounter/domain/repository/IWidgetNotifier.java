package com.ps.touchcounter.domain.repository;


import android.content.Context;

import java.util.Calendar;

public interface IWidgetNotifier {
    void updateWidget(Context context, String msgWithTouchRate, int counter, Calendar calendar);
}
