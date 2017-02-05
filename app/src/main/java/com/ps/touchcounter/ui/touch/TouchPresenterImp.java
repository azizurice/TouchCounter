package com.ps.touchcounter.ui.touch;

/**
 * Created by Azizur on 02/02/2017.
 */

public class TouchPresenterImp implements ITouchPresenter{
    ITouchView iTouchView;

    public TouchPresenterImp(ITouchView iTouchView){
        this.iTouchView=iTouchView;
    }

    @Override
    public void doShowTouchesInRange(int touches) {
        iTouchView.showTouchesInRange(touches);
    }

    @Override
    public void doShowTouchRatePerSecond(int touchRate) {
       iTouchView.showTouchRatePerSecond(touchRate);
    }
}
