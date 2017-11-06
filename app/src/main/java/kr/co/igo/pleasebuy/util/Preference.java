package kr.co.igo.pleasebuy.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference get/set
 */
public class Preference {

    public static String TRUE = "true";
    public static String FALSE = "false";


    public enum PREFS_KEY {
        DEVICE_ID,
        PUSH_DEVICE_ID,
        ENC_USER_ID,
        IS_LIST_VISIBLE,
        USER_ID,
        CNT_PRODUCT_IN_CART,
        USER_NAME
    }

    public static void setStringPreference(PREFS_KEY key, String value) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key.toString(), value);
        editor.commit();
    }

    //get string value
    public static String getStringPreference(PREFS_KEY key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String result = pref.getString(key.toString(), null);
        return result;
    }

    //get string value
    public static String getStringPreference(PREFS_KEY key, String def) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String result = pref.getString(key.toString(), def);
        return result;
    }

    //set int value
    public static void setIntPreference(PREFS_KEY key, int value) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key.toString(), value);
        editor.commit();
    }

    //get int value
    public static int getIntPreference(PREFS_KEY key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int result = pref.getInt(key.toString(), -1);
        return result;
    }

    //set boolean value
    public static void setBooleanPreference(PREFS_KEY key, boolean value) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key.toString(), value);
        editor.commit();
    }

    //set boolean value
    public static void setBooleanPreference(String key, boolean value) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //get boolean value
    public static boolean getBooleanPreference(PREFS_KEY key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean result = pref.getBoolean(key.toString(), false);
        return result;
    }

    //get boolean value
    public static boolean getBooleanPreference(String key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean result = pref.getBoolean(key, false);
        return result;
    }

    //특정 값을 삭제 시
    public static void removeValuePreference(PREFS_KEY key){
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key.toString());
        editor.commit();
    }

    //특정 값을 삭제 시
    public static void removeValuePreference(String key){
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    //모든 값 삭제시
    public static void removeAllPreference() {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    // 값 있는지 확인
    public boolean isContains(String key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean result = pref.contains(key);
        return result;
    }
    // 값 있는지 확인
    public boolean isContains(PREFS_KEY key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean result = pref.contains(key.toString());
        return result;
    }

    //set int value
    public static void setIntPreference(String key, int value) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //get int value
    public static int getIntPreference(String key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int result = pref.getInt(key, -1);
        return result;
    }

    //get int value
    public static int getIntPreference(String key, int def) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int result = pref.getInt(key, def);
        return result;
    }

    public static void setStringPreference(String key, String value) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //get string value
    public static String getStringPreference(String key) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String result = pref.getString(key, null);
        return result;
    }

    //get string value
    public static String getStringPreference(String key, String def) {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String result = pref.getString(key, def);
        return result;
    }

    public static int getBadgeCountPreference() {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int badgeCount = pref.getInt("badgeCount", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("badgeCount", badgeCount+1);
        editor.commit();
        return badgeCount+1;

    }

    public static void clearBadgeCountPreference() {
        SharedPreferences pref = AppController.getInstance().getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("badgeCount", 0);
        editor.commit();
    }

}