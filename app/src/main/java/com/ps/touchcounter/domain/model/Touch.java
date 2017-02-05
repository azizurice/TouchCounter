package com.ps.touchcounter.domain.model;

/**
 * Created by Azizur on 02/02/2017.
 */

public class Touch implements ITouch{
    int touchesInRange;
    int touchRatePerSecond;

    public Touch(){

    }

    public Touch(int touchesInRange, int touchRatePerSecond) {
        this.touchesInRange = touchesInRange;
        this.touchRatePerSecond = touchRatePerSecond;
    }

    @Override
    public int getTouchesInRange() {
        return 0;
    }

    @Override
    public int getTouchRatePerSecond() {
        return 0;
    }

    public void setTouchesInRange(int touchesInRange) {
        this.touchesInRange = touchesInRange;
    }

    public void setTouchRatePerSecond(int touchRatePerSecond) {
        this.touchRatePerSecond = touchRatePerSecond;
    }
}
