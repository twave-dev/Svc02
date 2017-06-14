package com.twave.svc02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twave.svc02.sync.CounterIntentService;
import com.twave.svc02.sync.CounterTasks;
import com.twave.svc02.sync.CounterUtils;
import com.twave.svc02.util.NotificationUtil;
import com.twave.svc02.util.PreferenceUtil;

// TODO 00-10 : Preference 값이 변경되는 것을 감지한다.
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    TextView mTvBtnCount;
    TextView mTvSchCount;
    ImageView mIvPower;

    Toast mToast;

    ChargingBroadcastReceiver mChargingReceiver;
    IntentFilter mChargingIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO 00-08 : TextView,ImageView 객체 참조를 생성한다.
        mTvBtnCount = (TextView) findViewById(R.id.tv_btnCount);
        mTvSchCount = (TextView) findViewById(R.id.tv_schCount);
        mIvPower = (ImageView) findViewById(R.id.iv_power);

        // TODO 00-09 : Preference 에서 Count 값을 읽어서 TextView 에 표시한다.
        displayBtnCount();
        displaySvcCount();

        // TODO 00-10-02
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        // TODO 03-09 : 스케쥴을 등록한다.
        CounterUtils.scheduleCounter(this);

        // TODO 04-03 : Broadcasg Receiver 등록
        mChargingIntentFilter = new IntentFilter();
        mChargingReceiver = new ChargingBroadcastReceiver();

        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    private void displayBtnCount() {
        int count = PreferenceUtil.getBtnCount(this);
        mTvBtnCount.setText(Integer.toString(count));
    }

    private void displaySvcCount() {
        int count = PreferenceUtil.getSchCount(this);
        mTvSchCount.setText(Integer.toString(count));
    }

    // TODO 00-10-03
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    // TODO 04-05 : Broadcast Receiver 등록
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mChargingReceiver, mChargingIntentFilter);
    }

    // TODO 04-06 : Broadcasdt Receiver 등록 해제
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingReceiver);
    }

    // TODO 00-10-01
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(PreferenceUtil.KEY_BTN_COUNT.equals(key)) {
            displayBtnCount();
        } else if(PreferenceUtil.KEY_SCH_COUNT.equals(key)) {
            displaySvcCount();
        }
    }

    // TODO 00-12 : 버튼이 클릭되면 서비스에 Counter 를 증가시키라고 지시한다.
    public void incUsrCount(View view) {
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, "Button Click : User Count increase", Toast.LENGTH_SHORT);
        mToast.show();

        Intent intent = new Intent(this, CounterIntentService.class);
        intent.setAction(CounterTasks.ACTION_INC_USR_COUNT);
        startService(intent);
    }

    // TODO 01-03-01 : 테스트를 위한 Notification 을 생성한다.
    public void testNotification(View view) {
        NotificationUtil.showNotiSchCount(this);
    }

    // TODO 04-01 : 전원의 연결여부에 따라 이미지를 변경한다.
    private void showCharging(boolean isCharging) {
        if(isCharging) {
            mIvPower.setImageResource(R.drawable.ic_star_red_24dp);
        } else {
            mIvPower.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    // TODO 04-02 : 전원연결에 대한 정보를 받기 위해 BroadCast Receiver 클래스를 생성한다.
    private class ChargingBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean bCharging = action.equals(Intent.ACTION_POWER_CONNECTED);

            showCharging(bCharging);
        }
    }
}
