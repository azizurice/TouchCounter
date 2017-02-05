package com.ps.touchcounter.domain.iteractor;


import com.ps.touchcounter.domain.repository.WidgetNotifier;

/**
 * Created by Azizur on 04/02/2017.
 */

public class NotifyTouchesUseCase {
    WidgetNotifier widgetNotifier;
    public NotifyTouchesUseCase(WidgetNotifier widgetNotifier){
        this.widgetNotifier=widgetNotifier;
    }


}
