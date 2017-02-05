package com.ps.touchcounter.ui.touch;



public interface ITouchPresenter {
    void doShowTouchesInRange(int touches);
    void doShowTouchRatePerSecond(int touchRate);
}
