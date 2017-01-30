package com.ps.touchcounter.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ps.touchcounter.BaseActivity;
import com.ps.touchcounter.R;
import com.ps.touchcounter.services.UpdateActivityService;

public class TouchCountActivity extends BaseActivity {
    TextView mTextView;
    public static int nTouch = 0;
    String msgTouchesIn100ms;
    String msgTouchRate;
    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_count);

        // Register receiver with appropriate intent filter
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);


        mTextView = (TextView) findViewById(R.id.touchId);
        msgTouchesIn100ms ="Touches in 100ms :";
        msgTouchRate = "Touch Rate per second :";

        // Start the service that will continusly calculate touches per second from
        // the queue like map (10 entries) becasue 10 times update per second.
        Intent msgIntent = new Intent(this, UpdateActivityService.class);
        msgIntent.putExtra(UpdateActivityService.PARAM_IN_MSG, msgTouchRate);
        startService(msgIntent);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTextView.setText(msgTouchesIn100ms + " :" + String.valueOf(updateTouch(1)) + " Touch time :" + event.getEventTime());


        }
        return false;
    }

    synchronized public static int updateTouch(int flag){
        if(flag==1 ) {
            nTouch++;
        } else if (flag==0){
            nTouch=0;
        }
        return nTouch;
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "com.ps.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            TextView mResult = (TextView) findViewById(R.id.resultViewId);
            String text = intent.getStringExtra(UpdateActivityService.PARAM_OUT_MSG);
            mResult.setText(msgTouchRate+text);
            if (Integer.parseInt(text)==0) {
                mTextView.setText(msgTouchesIn100ms + " :0" + " Touch time :");
            }
          }
    }
}