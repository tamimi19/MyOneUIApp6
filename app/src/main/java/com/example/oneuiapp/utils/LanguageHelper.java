package com.example.oneuiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import java.util.Locale;

public class LanguageHelper {
    
    private static final String LANGUAGE_PREFS = "language_preferences";
    private static final String KEY_LANGUAGE = "selected_language";
    private static final String KEY_LANGUAGE_CHANGED = "language_changed";
    
    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        markLanguageAsApplied(context);
    }
    
    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LANGUAGE_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "en");
    }
    
    public static void setLanguage(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(LANGUAGE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.putBoolean(KEY_LANGUAGE_CHANGED, true);
        editor.apply();
    }
    
    public static boolean hasLanguageChanged(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LANGUAGE_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_LANGUAGE_CHANGED, false);
    }
    
    private static void markLanguageAsApplied(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LANGUAGE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_LANGUAGE_CHANGED, false);
        editor.apply();
    }
    
    public static boolean isRTL(Context context) {
        String language = getLanguage(context);
        return language.equals("ar");
    }
}
