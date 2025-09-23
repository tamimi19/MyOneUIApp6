package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import de.dlyt.yanndroid.samsung.layout.ToolbarLayout;
import de.dlyt.yanndroid.samsung.RelatedCard;
import de.dlyt.yanndroid.samsung.SwitchBar;

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
        // Apply language and theme before creating
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
        toolbarLayout.setTitle(getString(R.string.settings_title));
        toolbarLayout.setSubtitle(getString(R.string.settings_subtitle));
        toolbarLayout.setNavigationIcon(getDrawable(R.drawable.ic_back));
        
        // Enable action bar
        setSupportActionBar(toolbarLayout.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Setup navigation click listener
        toolbarLayout.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }
    
    private void setupSettingsOptions() {
        // Setup language option
        updateLanguageText();
        languageOption.setOnClickListener(v -> {
            if (!isLanguageChanging) {
                toggleLanguage();
            }
        });
        
        // Setup theme option  
        updateThemeText();
        themeOption.setOnClickListener(v -> {
            if (!isThemeChanging) {
                toggleTheme();
            }
        });
        
        // Setup notification option
        notificationOption.setOnClickListener(v -> {
            openNotificationSettings();
        });
        
        // Set card titles
        languageCard.setTitle(getString(R.string.language_setting));
        themeCard.setTitle(getString(R.string.theme_setting));
        notificationCard.setTitle(getString(R.string.notification_setting));
    }
    
    private void updateLanguageText() {
        String currentLang = LanguageHelper.getLanguage(this);
        if (currentLang.equals("ar")) {
            languageOption.setText(getString(R.string.current_language_arabic));
        } else {
            languageOption.setText(getString(R.string.current_language_english));
        }
    }
    
    private void updateThemeText() {
        if (ThemeHelper.isDarkTheme(this)) {
            themeOption.setText(getString(R.string.current_theme_dark));
        } else {
            themeOption.setText(getString(R.string.current_theme_light));
        }
    }
    
    private void toggleLanguage() {
        isLanguageChanging = true;
        String currentLang = LanguageHelper.getLanguage(this);
        String newLang = currentLang.equals("ar") ? "en" : "ar";
        
        // Save new language
        LanguageHelper.setLanguage(this, newLang);
        LanguageHelper.setLocale(this, newLang);
        
        // Recreate activity to apply changes immediately
        recreate();
    }
    
    private void toggleTheme() {
        isThemeChanging = true;
        boolean isDark = ThemeHelper.isDarkTheme(this);
        
        // Toggle theme
        ThemeHelper.setDarkTheme(this, !isDark);
        ThemeHelper.applyTheme(this);
        
        // Recreate activity to apply changes immediately
        recreate();
    }
    
    private void openNotificationSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        } catch (Exception e) {
            // Fallback to general notification settings
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        
        // Update texts in case they changed
        updateLanguageText();
        updateThemeText();
    }
}
