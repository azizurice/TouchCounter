package com.ps.touchcounter.ui.touch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ps.touchcounter.R;
import com.ps.touchcounter.device.services.UpdateService;
import com.ps.touchcounter.ui.BaseActivity;

public class TouchCounterActivity extends BaseActivity implements ITouchView {
    TextView mTouchesInRange;
    TextView mTouchRate;
    public static int nTouch = 0;
    String msgTouchesIn100ms = "Touches in 100ms :";
    String msgTouchRate = "Touch Rate per second :";
    ITouchPresenter iTouchPresenter;
    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_count);

        mTouchesInRange = (TextView) findViewById(R.id.touchId);
        mTouchRate = (TextView) findViewById(R.id.resultViewId);

        iTouchPresenter = new TouchPresenterImp(this);

        // Register receiver with appropriate intent filter
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        // Start the service that will continusly calculate touches per second from
        // the queue like map (10 entries) becasue 10 times update per second.
        Intent msgIntent = new Intent(this, UpdateService.class);
        msgIntent.putExtra(UpdateService.PARAM_IN_MSG, msgTouchRate);
        startService(msgIntent);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iTouchPresenter.doShowTouchesInRange(updateTouch(1));

        }
        return false;
    }

    synchronized public static int updateTouch(int flag) {
        if (flag == 1) {
            nTouch++;
        } else if (flag == 0) {
            nTouch = 0;
        }
        return nTouch;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showTouchesInRange(int touches) {
        mTouchesInRange.setText(msgTouchesIn100ms + " :" + String.valueOf(touches));
    }

    @Override
    public void showTouchRatePerSecond(int touchRate) {
        mTouchRate.setText(msgTouchRate + touchRate);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "com.ps.touchcounter.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            int touchRate = Integer.parseInt(intent.getStringExtra(UpdateService.PARAM_OUT_MSG));
            iTouchPresenter.doShowTouchRatePerSecond(touchRate);
            if (touchRate == 0) {
                iTouchPresenter.doShowTouchesInRange(0);
            }
        }
    }
}