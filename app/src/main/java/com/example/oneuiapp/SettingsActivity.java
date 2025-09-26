package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// استيراد الحزم الصحيحة للمكتبة المحدثة
import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.widget.RelatedCard;

import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;
import com.google.android.material.textview.MaterialTextView;

public class SettingsActivity extends AppCompatActivity {
    
    // متغيرات المكونات الرئيسية
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
        // تطبيق اللغة والثيم قبل الإنشاء
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // تهيئة المكونات وإعداد شريط الأدوات وخيارات الإعدادات
        initializeViews();
        setupToolbar();
        setupSettingsOptions();
    }
    
    /**
     * تهيئة المتغيرات الخاصة بالمكونات المرئية
     */
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        languageCard = findViewById(R.id.language_card);
        themeCard = findViewById(R.id.theme_card);
        notificationCard = findViewById(R.id.notification_card);
        languageOption = findViewById(R.id.language_option);
        themeOption = findViewById(R.id.theme_option);
        notificationOption = findViewById(R.id.notification_option);
    }
    
    /**
     * إعداد شريط الأدوات والتنقل
     */
    private void setupToolbar() {
        if (toolbarLayout != null) {
            // تعيين شريط الأدوات
            setSupportActionBar(toolbarLayout.getToolbar());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            // إعداد مستمع النقر على زر التنقل
            toolbarLayout.setOnNavigationClickListener(new ToolbarLayout.OnNavigationClickListener() {
                @Override
                public void onClick() {
                    onBackPressed();
                }
            });
        }
    }
    
    /**
     * إعداد خيارات الإعدادات ومعالجات الأحداث
     */
    private void setupSettingsOptions() {
        // إعداد خيار اللغة
        updateLanguageText();
        if (languageOption != null) {
            languageOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLanguageChanging) {
                        toggleLanguage();
                    }
                }
            });
        }
        
        // إعداد خيار الثيم
        updateThemeText();
        if (themeOption != null) {
            themeOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isThemeChanging) {
                        toggleTheme();
                    }
                }
            });
        }
        
        // إعداد خيار الإشعارات
        if (notificationOption != null) {
            notificationOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNotificationSettings();
                }
            });
        }
        
        // تعيين عناوين البطاقات باستخدام البيانات من strings.xml
        setCardTitles();
    }
    
    /**
     * تعيين عناوين البطاقات
     */
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
    
    /**
     * تحديث نص اللغة الحالية
     */
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
    
    /**
     * تحديث نص الثيم الحالي
     */
    private void updateThemeText() {
        if (themeOption != null) {
            if (ThemeHelper.isDarkTheme(this)) {
                themeOption.setText(getString(R.string.current_theme_dark));
            } else {
                themeOption.setText(getString(R.string.current_theme_light));
            }
        }
    }
    
    /**
     * تبديل لغة التطبيق
     */
    private void toggleLanguage() {
        isLanguageChanging = true;
        String currentLang = LanguageHelper.getLanguage(this);
        String newLang = currentLang.equals("ar") ? "en" : "ar";
        
        // حفظ اللغة الجديدة
        LanguageHelper.setLanguage(this, newLang);
        LanguageHelper.setLocale(this, newLang);
        
        // إعادة إنشاء النشاط لتطبيق التغييرات فوراً
        recreate();
    }
    
    /**
     * تبديل ثيم التطبيق
     */
    private void toggleTheme() {
        isThemeChanging = true;
        boolean isDark = ThemeHelper.isDarkTheme(this);
        
        // تبديل الثيم
        ThemeHelper.setDarkTheme(this, !isDark);
        ThemeHelper.applyTheme(this);
        
        // إعادة إنشاء النشاط لتطبيق التغييرات فوراً
        recreate();
    }
    
    /**
     * فتح إعدادات الإشعارات
     */
    private void openNotificationSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        } catch (Exception e) {
            // الرجوع إلى إعدادات الإشعارات العامة
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // معالجة النقر على زر الرجوع في شريط الأدوات
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // تطبيق انتقال مخصص عند الرجوع
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // إعادة تعيين حالة التغيير
        isLanguageChanging = false;
        isThemeChanging = false;
        
        // تحديث النصوص في حالة تغييرها
        updateLanguageText();
        updateThemeText();
    }
}
