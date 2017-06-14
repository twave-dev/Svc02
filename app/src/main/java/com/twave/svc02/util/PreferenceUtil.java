package com.twave.svc02.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * TODO 00-03 : 프리퍼런스 값을 입력/조회 하는 클래스 생성
 */

public class PreferenceUtil {
    // TODO 00-04 : 프리퍼런스 키 정의
    public static final String KEY_BTN_COUNT = "BTN_COUNT";
    public static final String KEY_SCH_COUNT = "SCH_COUNT";

    public static final int DEFAULT_COUNT = 0;

    // TODO 00-05 : 프리퍼런스 에서 count 값 조회/증가 함수 생성
    public static int getBtnCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_BTN_COUNT, DEFAULT_COUNT);
    }

    synchronized public static void incBtnCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int count = getBtnCount(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_BTN_COUNT, ++count);
        editor.apply();
    }

    public static int getSchCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_SCH_COUNT, DEFAULT_COUNT);
    }

    synchronized public static void incSchCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int count = getSchCount(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SCH_COUNT, ++count);
        editor.apply();
    }
}
