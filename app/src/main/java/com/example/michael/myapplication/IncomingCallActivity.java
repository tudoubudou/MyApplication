package com.example.michael.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.mail.R;

/**
 * Created by Michael on 16/4/20.
 */
public class IncomingCallActivity extends AppCompatActivity {
    @Bind(R.id.answer)
    Button answer;
    @Bind(R.id.hangup)
    Button hangup;
    private final static String TAG = IncomingCallActivity.class.getSimpleName();
    public final static ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private InComingCallPresenter inComingCallPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.incoming_call);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        inComingCallPresenter= new InComingCallPresenter(this);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inComingCallPresenter.answerCall();
                finish();
            }
        });
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inComingCallPresenter.killCall();
                finish();
            }
        });
        mExecutorService.execute(new CheckRunnable());
    }
    private class CheckRunnable implements Runnable {
        @Override
        public void run() {
            while (isTelephonyCalling()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!IncomingCallActivity.this.isFinishing()) {
                IncomingCallActivity.this.finish();
                Log.i(TAG,"finish self");
            }
        }
    }

    public boolean isTelephonyCalling(){
        boolean calling = false;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(TelephonyManager.CALL_STATE_OFFHOOK==telephonyManager.getCallState()||TelephonyManager.CALL_STATE_RINGING==telephonyManager.getCallState()){
            calling = true;
        }
        return calling;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
