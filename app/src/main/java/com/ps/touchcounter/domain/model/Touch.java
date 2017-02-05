package com.ps.touchcounter.domain.model;


// For this app, we can ignore it. we don't have so many business rule here.
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
        return touchesInRange;
    }

    @Override
    public int getTouchRatePerSecond() {
        return touchRatePerSecond;
    }

    public void setTouchesInRange(int touchesInRange) {
        this.touchesInRange = touchesInRange;
    }

    public void setTouchRatePerSecond(int touchRatePerSecond) {
        this.touchRatePerSecond = touchRatePerSecond;
    }
}
