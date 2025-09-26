package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.widget.RelatedCard;

import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;
import com.google.android.material.textview.MaterialTextView;

public class SettingsActivity extends AppCompatActivity {
    
    private ToolbarLayout toolbarLayout;
    private RelatedCard languageCard;
    private RelatedCard themeCard;
    private RelatedCard notificationCard;
    private MaterialTextView languageOption;
    private MaterialTextView themeOption;
    private MaterialTextView notificationOption;
    private boolean isLanguageChanging = false;
    private boolean isThemeChanging = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initializeViews();
        setupToolbar();
        setupSettingsOptions();
    }
    
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        languageCard = findViewById(R.id.language_card);
        themeCard = findViewById(R.id.theme_card);
        notificationCard = findViewById(R.id.notification_card);
        languageOption = findViewById(R.id.language_option);
        themeOption = findViewById(R.id.theme_option);
        notificationOption = findViewById(R.id.notification_option);
    }
    
    private void setupToolbar() {
        if (toolbarLayout != null) {
            setSupportActionBar(toolbarLayout.getToolbar());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            toolbarLayout.setOnNavigationClickListener(new ToolbarLayout.OnNavigationClickListener() {
                @Override
                public void onClick() {
                    onBackPressed();
                }
            });
        }
    }
    
    private void setupSettingsOptions() {
        updateLanguageText();
        if (languageOption != null) {
            languageOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLanguageChanging) {
                        toggleLanguage(); // سيستدعي recreate() لتطبيق التغييرات78
                    }
                }
            });
        }
        
        updateThemeText();
        if (themeOption != null) {
            themeOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isThemeChanging) {
                        toggleTheme(); // سيستدعي recreate() لتطبيق التغييرات910
                    }
                }
            });
        }
        
        if (notificationOption != null) {
            notificationOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNotificationSettings();
                }
            });
        }
        
        setCardTitles();
    }
    
    private void setCardTitles() {
        if (languageCard != null) {
            languageCard.setTitle(getString(R.string.language_setting));
        }
        if (themeCard != null) {
            themeCard.setTitle(getString(R.string.theme_setting));
        }
        if (notificationCard != null) {
            notificationCard.setTitle(getString(R.string.notification_setting));
        }
    }
    
    private void updateLanguageText() {
        if (languageOption != null) {
            String currentLang = LanguageHelper.getLanguage(this);
            if (currentLang.equals("ar")) {
                languageOption.setText(getString(R.string.current_language_arabic));
            } else {
                languageOption.setText(getString(R.string.current_language_english));
            }
        }
    }
    
    private void updateThemeText() {
        if (themeOption != null) {
            if (ThemeHelper.isDarkTheme(this)) {
                themeOption.setText(getString(R.string.current_theme_dark));
            } else {
                themeOption.setText(getString(R.string.current_theme_light));
            }
        }
    }
    
    private void toggleLanguage() {
        isLanguageChanging = true;
        String currentLang = LanguageHelper.getLanguage(this);
        String newLang = currentLang.equals("ar") ? "en" : "ar";
        LanguageHelper.setLanguage(this, newLang);
        LanguageHelper.setLocale(this, newLang);
        recreate(); // إعادة إنشاء النشاط لتحديث الواجهة باللغة الجديدة
    }
    
    private void toggleTheme() {
        isThemeChanging = true;
        boolean isDark = ThemeHelper.isDarkTheme(this);
        ThemeHelper.setDarkTheme(this, !isDark);
        ThemeHelper.applyTheme(this);
        recreate(); // إعادة إنشاء النشاط لتحديث الثيم الجديد
    }
    
    private void openNotificationSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // الاستجابة لزر الرجوع في شريط الأدوات
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        isLanguageChanging = false;
        isThemeChanging = false;
        updateLanguageText();
        updateThemeText();
    }
            }
