package com.ps.touchcounter.ui.touch;

/**
 * Created by Azizur on 02/02/2017.
 */

public interface ITouchView {
    void showMessage(String message);
    void showTouchesInRange(int touches);
    void showTouchRatePerSecond(int touchRate);
}
