package com.ps.touchcounter.ui.touch;

/**
 * Created by Azizur on 02/02/2017.
 */

public interface ITouchPresenter {
    void doShowTouchesInRange(int touches);
    void doShowTouchRatePerSecond(int touchRate);
}
