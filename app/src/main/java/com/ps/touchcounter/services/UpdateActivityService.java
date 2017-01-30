package com.ps.touchcounter.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import com.ps.touchcounter.activities.TouchCountActivity;
import com.ps.touchcounter.widgets.TouchCounterWidget;

import java.util.Map;

public class UpdateActivityService extends IntentService {

    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
    private Handler mHandler = new Handler();
    private long key=1;
    public static String resultTxt;
    public UpdateActivityService() {
        super("UpdateActivityService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String msg = intent.getStringExtra(PARAM_IN_MSG);
            while(true){
                //mHandler.postDelayed(mResetCounter, 10000);
                SystemClock.sleep(100); // 100 milliseconds
                TouchCountActivity.touchesInIntervals.put(key++,TouchCountActivity.nTouch);
                TouchCountActivity.nTouch=0;
                int sum=0;

                for (Map.Entry entry : TouchCountActivity.touchesInIntervals.entrySet()) {
                    sum=sum+(int)entry.getValue();
                }
                resultTxt = msg + " " + String.valueOf(sum);

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(TouchCountActivity.ResponseReceiver.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_OUT_MSG, resultTxt);
                sendBroadcast(broadcastIntent);


//                Intent intent1=new Intent(this,TouchCounterWidget.AppWidgetService.class);
//                intent1.putExtra(TouchCounterWidget.AppWidgetService.UPDATE_TOUCH_RATE,resultTxt);
//                startService(intent1);
            }
        }


    }
//    private Runnable mResetCounter = new Runnable() {
//        @Override
//        public void run() {
//            //mCounterOneRange = 0;
//            TouchCountActivity.nTouch=0;
//        }
//    };

}
