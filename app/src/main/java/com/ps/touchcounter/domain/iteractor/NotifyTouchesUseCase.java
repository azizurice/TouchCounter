package com.ps.touchcounter.domain.iteractor;


import com.ps.touchcounter.domain.repository.IWidgetNotifier;

/**
 * Created by Azizur on 04/02/2017.
 */

public class NotifyTouchesUseCase {
    IWidgetNotifier widgetNotifier;
    public NotifyTouchesUseCase(IWidgetNotifier widgetNotifier){
        this.widgetNotifier=widgetNotifier;
    }


}
