package com.twave.svc02.sync;

import android.content.Context;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * TODO 03-06 : Firebase Job Service 에 스케쥴러를 등록하는 클래스
 */

public class CounterUtils {

    // TODO 03-07 : 스케쥴 간격을 정의한다.
    private static final int COUNT_INTERVAL_MINUTES = 1;
    private static final int COUNT_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(COUNT_INTERVAL_MINUTES);
    private static final int SYNC_FLEXTIME_SECONDS = COUNT_INTERVAL_SECONDS;

    private static final String COUNTER_JOB_TAG = "counter_tag";
    private static boolean sInitialized;

    // TODO 03-08 : 스케쥴을 등록하는 함수 정의
    synchronized public static void scheduleCounter(@Nullable final Context context) {
        if(sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job counterJob = dispatcher.newJobBuilder()
                .setService(CounterFirebaseJobService.class)
                .setTag(COUNTER_JOB_TAG)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(COUNT_INTERVAL_SECONDS, COUNT_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(counterJob);

        sInitialized = true;
    }
}
