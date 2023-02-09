package com.cookandroid.big9team3movieapp;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferences {

    static final String nickname = "nickname";

    static android.content.SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserName(Context ctx, String nickname) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(nickname, nickname);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getPreferences(Context ctx) {
        return getSharedPreferences(ctx).getString(nickname, "");
    }

    // 로그아웃
    public static void clearUserName(Context ctx) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
