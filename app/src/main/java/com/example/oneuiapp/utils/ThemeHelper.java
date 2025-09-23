package com.example.oneuiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    
    private static final String THEME_PREFS = "theme_preferences";
    private static final String KEY_IS_DARK_THEME = "is_dark_theme";
    private static final String KEY_THEME_CHANGED = "theme_changed";
    
    public static void applyTheme(Context context) {
        if (isDarkTheme(context)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        markThemeAsApplied(context);
    }
    
    public static boolean isDarkTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_DARK_THEME, false);
    }
    
    public static void setDarkTheme(Context context, boolean isDark) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_DARK_THEME, isDark);
        editor.putBoolean(KEY_THEME_CHANGED, true);
        editor.apply();
    }
    
    public static boolean hasThemeChanged(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_THEME_CHANGED, false);
    }
    
    private static void markThemeAsApplied(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_THEME_CHANGED, false);
        editor.apply();
    }
}
