package com.twave.svc02.sync;

import android.content.Context;

import com.twave.svc02.util.NotificationUtil;
import com.twave.svc02.util.PreferenceUtil;

/**
 * TODO 00-02 : 카운터를 증가시키는 클래스 생성
 */

public class CounterTasks {
    // TODO 00-06 : 서비스에서 호출해서 사용할 메소드를 작성한다.(카운터를 증가시키는 ACTION 정의)
    public static final String ACTION_INC_USR_COUNT = "increment-usr-count";

    // TODO 02-02 : Notification 을 제거하는 ACTION 정의
    public static final String ACTION_DISMISS_NOTI = "dismiss-notification";

    // TODO 03-02 : Scheduler 에 의해 Counting 하는 ACTION 정의
    public static final String ACTION_INC_SCH_COUNT = "increment-sch-count";

    public static void executeTask(Context context, String action) {
        if(ACTION_INC_USR_COUNT.equals(action)) {
            PreferenceUtil.incBtnCount(context);
            NotificationUtil.clearAllNotification(context);
        } else if(ACTION_DISMISS_NOTI.equals(action)) {
            NotificationUtil.clearAllNotification(context);
        } else if(ACTION_INC_SCH_COUNT.equals(action)) {
            PreferenceUtil.incSchCount(context);
            NotificationUtil.showNotiSchCount(context);
        }
    }

    /*
    public static void incSchCount(Context context) {
        PreferenceUtil.incSchCount(context);
        NotificationUtil.clearAllNotification(context);
    }
    */
}
