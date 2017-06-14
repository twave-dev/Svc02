package com.twave.svc02.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

// TODO 00-07 : Count 를 증가시키는 Intent Service 생성
/*
서비스의 콜백메소드들에 대해서 디폴트 구현을 제공하기 때문에 우리가 별도로 콜백 메소드를 구현할 필요가 없다는 것입니다.
단지 onHandleIntent() 메소드만 구현하면 되고 이 메소드 안에서 클라이언트에 의해 제공되는 작업을 하면 됩니다.
또 요청한 작업이 완료되면 자동적으로 서비스를 중단하기 때문에 stopSelf() 또는 stopService()를 호출할 필요가 없다는 것이 IntentService의 장점입니다.
 */
public class CounterIntentService extends IntentService {

    public CounterIntentService() {
        super(CounterIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        CounterTasks.executeTask(this, action);
    }
}
