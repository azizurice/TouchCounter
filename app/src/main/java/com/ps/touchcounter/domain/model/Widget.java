package com.ps.touchcounter.domain.model;


public class Widget implements IWidget {
    String touchRate;
    String counter;
    String time;
    public  Widget(){

    }

    public Widget(String touchRate, String counter, String time) {
        this.touchRate = touchRate;
        this.counter = counter;
        this.time = time;
    }

    @Override
    public String getTouchRate() {
        return touchRate;
    }

    @Override
    public String getCounter() {
        return counter;
    }

    @Override
    public String getTime() {
        return time;
    }
}
