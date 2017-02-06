package com.ps.touchcounter.domain.model;


// For bigger project, defining model in this way and making use of it is better.
// as we don't have that many business rule, I have just used Interactor. However, I have kept it
// for having a reference for future project.
public class Touch implements ITouch {
    int touchesInRange;
    int touchRatePerSecond;

    public Touch() {

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
