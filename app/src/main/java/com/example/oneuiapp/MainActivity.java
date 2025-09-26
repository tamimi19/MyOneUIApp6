package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

// استيراد الحزم الأساسية فقط
import de.dlyt.yanndroid.oneui.layout.DrawerLayout;
import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;

import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;
import com.example.oneuiapp.utils.CrashHandler;

public class MainActivity extends AppCompatActivity {
    // متغيرات المكونات الرئيسية
    private DrawerLayout drawerLayout;
    private ToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // إعداد معالج الأعطال واللغة والثيم قبل إنشاء النشاط
        CrashHandler.setupCrashHandler(this);
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة المتغيرات وإعداد الواجهة
        initializeViews();
        setupToolbar();
        setupDrawer();
    }

    /**
     * تهيئة المتغيرات الخاصة بالمكونات المرئية
     */
    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbarLayout = findViewById(R.id.toolbar_layout);
    }

    /**
     * إعداد شريط الأدوات
     */
    private void setupToolbar() {
        if (toolbarLayout != null) {
            setSupportActionBar(toolbarLayout.getToolbar());
        }
    }

    /**
     * إعداد الدرج الجانبي
     */
    private void setupDrawer() {
        if (drawerLayout != null) {
            // إعداد النقر على الأزرار يدوياً من خلال findViewById
            setupDrawerButtons();
        }
    }

    /**
     * إعداد أزرار الدرج الجانبي يدوياً
     */
    private void setupDrawerButtons() {
        View homeButton = findViewById(R.id.option_home);
        View scrollListButton = findViewById(R.id.option_scroll_list);
        View settingsButton = findViewById(R.id.option_settings);

        if (homeButton != null) {
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDrawerSelection(R.id.option_home);
                }
            });
        }

        if (scrollListButton != null) {
            scrollListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDrawerSelection(R.id.option_scroll_list);
                }
            });
        }

        if (settingsButton != null) {
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDrawerSelection(R.id.option_settings);
                }
            });
        }
    }

    /**
     * معالجة اختيار خيارات اللوحة الجانبية
     * @param id معرف الخيار المحدد
     */
    private void handleDrawerSelection(int id) {
        // إغلاق القائمة الجانبية أولاً
        if (drawerLayout != null) {
            drawerLayout.setDrawerOpen(false, true);
        }

        if (id == R.id.option_home) {
            // المستخدم في الصفحة الرئيسية بالفعل
        } else if (id == R.id.option_scroll_list) {
            Intent intent = new Intent(this, ScrollListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.option_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // إعادة إنشاء النشاط في حالة تغيير الثيم أو اللغة
        if (ThemeHelper.hasThemeChanged(this) || LanguageHelper.hasLanguageChanged(this)) {
            recreate();
        }
    }

    @Override
    public void onBackPressed() {
        // إغلاق الدرج الجانبي إذا كان مفتوحاً (أو محاولة إغلاقه دائماً)
        if (drawerLayout != null) {
            drawerLayout.setDrawerOpen(false, true);
        }
        // ثم المتابعة بالرجوع الافتراضي
        super.onBackPressed();
    }
}
