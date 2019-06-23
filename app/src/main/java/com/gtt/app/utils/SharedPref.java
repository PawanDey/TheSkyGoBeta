package com.gtt.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    public static SharedPreferences createPreference(Context context){
        preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        editor = preferences.edit();
        return preferences;
    }

    public static void write(String key, String value){
        editor.putString(key,value);
        editor.apply();
    }


    public static void write(String key, int value){
        editor.putInt(key,value);
        editor.apply();
    }

    public static void write(String key, boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }

    public static String readString(String key ){
       return preferences.getString(key,"");
    }

    public static int readInt(String key ){
       return preferences.getInt(key,0);
    }

    public static boolean readBoolean(String key ){
       return preferences.getBoolean(key,false);
    }

    public static void clearPreferences() {
        editor.clear();
        editor.apply();
    }
}
