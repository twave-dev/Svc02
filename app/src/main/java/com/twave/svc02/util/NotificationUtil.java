package com.twave.svc02.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;

import com.twave.svc02.MainActivity;
import com.twave.svc02.R;
import com.twave.svc02.sync.CounterIntentService;
import com.twave.svc02.sync.CounterTasks;

/**
 * TODO 01-00 : Notification 처리를 위한 클래스 생성
 */
/*
widget 같은 경우 만들어 놓으면 우리가 app 을 실행할 때 실행하는 것이 아니기 때문에 자신이 만들어 놓은 app 이 동작할 때 control 할 수 있는게 아니다.
widget 은 system 의 통제를 받게 된다. 그래서 click listener 를 assign 할 수 없는데, 그래서 PendingIntent 를 assign 한다고 한다.
그러면, user 가 widget 을 touch 했을 때 이 PendingIntent 가 실행된다. 이렇게 나중에 실행되기 때문에 pending 이라 불린다고 하는 듯 하다.

Intent 의 경우는 Intent 를 받아서 수행하는 app 의 permission 을 이용하게 되지만, PendingIntent 의 경우는 intent 를 보내는 app 의 permission 을 가지고 Intent 를 받는 app 에서 Intent 를 실행하게 된다고 한다.
이것은 widget 에서는 명확하다. 우리가 widget 을 만들 때 app 의 permission 을 갖기 때문에 widget 도 같은 permission 을 갖게 된다.
하지만 위에서 얘기처럼 widget 이 system 의 control 안으로 들어가면 permission 이 달라지게 되는데, 이 상황에서도 같은 permission 을 유지하기 위해서 PendingIntent 에 permission 을 같이 전달하는 듯 하다.
 */
public class NotificationUtil {

    private static final int ID_SCH_COUNT_NOTIFICATION = 1000;
    private static final int ID_SCH_COUNT_PENDING = 2000;

    private static final int ID_NO_COUNT_PENDING = 3000;
    private static final int ID_COUNT_PENDING = 4000;


    // TODO 01-01 : Notification 에서 사용할 ContentIntent 를 리턴해주는 함수
    private static PendingIntent getContentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                ID_SCH_COUNT_PENDING,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // TODO 01-02 : Notification 을 생성한다.
    public static void showNotiSchCount(Context context) {
        // android.support.v7.app.NotificationCompat 에서는 NotificationCompat.Builder 대신 Notification. 그리고 마지막에 .build();
        Notification notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("카운터 스케즐러에 의한 공지")
                .setContentText("카운터를 증가시킬지 여쭈어 봅니다. 아래 작업중 선택하세요.")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(getContentIntent(context))
                .setAutoCancel(true)
                // TODO 02-04 : Notication 에 Count 증가시키거나 증가시키지 않는 Action 두개를 추가한다
                .addAction(countAction(context))
                .addAction(noCountAction(context))
                .build();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.priority = Notification.PRIORITY_HIGH;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(ID_SCH_COUNT_NOTIFICATION, notificationBuilder);
    }

    // TODO 02-01 : 모든 Notification 을 제거하는 함수를 만든다.
    public static void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    // TODO 02-03 : Notification 에서 사용할 두가지 Action 처리를 위한 함수를 만든다. noCountAction, countAction
    private static Action noCountAction(Context context) {
        Intent noCountIntent = new Intent(context, CounterIntentService.class);
        noCountIntent.setAction(CounterTasks.ACTION_DISMISS_NOTI);

        PendingIntent noCountPendingIntent = PendingIntent.getService(
                context,
                ID_NO_COUNT_PENDING,
                noCountIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return new Action(
                R.mipmap.ic_launcher,
                "NO COUNT",
                noCountPendingIntent);
    }

    private static Action countAction(Context context) {
        Intent countIntent = new Intent(context, CounterIntentService.class);
        countIntent.setAction(CounterTasks.ACTION_INC_USR_COUNT);

        PendingIntent countPendingIntent = PendingIntent.getService(
                context,
                ID_COUNT_PENDING,
                countIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return new Action(
                R.mipmap.ic_launcher,
                "COUNT",
                countPendingIntent);
    }

}
