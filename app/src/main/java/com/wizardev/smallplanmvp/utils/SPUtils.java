package com.wizardev.smallplanmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wizardev on 17-2-28.
 */

public class SPUtils {

    public static void setString(Context context, String key,String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cropPicturePath",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key,String defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cropPicturePath",
                Context.MODE_PRIVATE);

        return sharedPreferences.getString(key,defValue);
    }

    public static void setBoolean(Context context, String key,boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cropPicturePath",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key,boolean defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cropPicturePath",
                Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(key,defValue);
    }

}
