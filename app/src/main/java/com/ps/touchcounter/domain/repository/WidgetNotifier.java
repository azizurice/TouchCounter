package com.ps.touchcounter.domain.repository;

/**
 * Created by Azizur on 03/02/2017.
 */

public interface WidgetNotifier {
    void sendTouchRate();
    void sendCounter();
    void sendClock();
}
