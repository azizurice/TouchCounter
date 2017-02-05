package com.ps.touchcounter.ui.touch;



public interface ITouchView {
    void showMessage(String message);
    void showTouchesInRange(int touches);
    void showTouchRatePerSecond(int touchRate);
}
