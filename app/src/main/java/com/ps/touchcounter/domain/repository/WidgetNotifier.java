package com.ps.touchcounter.domain.repository;



public interface WidgetNotifier {
    void sendTouchRate();
    void sendCounter();
    void sendClock();
}
