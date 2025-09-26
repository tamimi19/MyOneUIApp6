package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

// استيراد الحزم الصحيحة للمكتبة المحدثة
import de.dlyt.yanndroid.oneui.layout.DrawerLayout;
import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.drawer.OptionButton;
import de.dlyt.yanndroid.oneui.drawer.OptionGroup;

import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;
import com.example.oneuiapp.utils.CrashHandler;

public class MainActivity extends AppCompatActivity {
    // متغيرات المكونات الرئيسية
    private DrawerLayout drawerLayout;
    private ToolbarLayout toolbarLayout;
    private OptionGroup optionGroup;
    private OptionButton homeOption;
    private OptionButton scrollListOption;
    private OptionButton settingsOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // إعداد معالج الأعطال واللغة والثيم قبل إنشاء النشاط
        CrashHandler.setupCrashHandler(this);
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة المتغيرات وإعداد خيارات اللوحة الجانبية
        initializeViews();
        setupToolbar();
        setupDrawerOptions();
    }

    /**
     * تهيئة المتغيرات الخاصة بالمكونات المرئية
     */
    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        optionGroup = findViewById(R.id.option_group);
        homeOption = findViewById(R.id.option_home);
        scrollListOption = findViewById(R.id.option_scroll_list);
        settingsOption = findViewById(R.id.option_settings);
    }

    /**
     * إعداد شريط الأدوات
     */
    private void setupToolbar() {
        // ربط شريط الأدوات بـ DrawerLayout
        if (toolbarLayout != null && drawerLayout != null) {
            drawerLayout.setToolbar(toolbarLayout);
        }
    }

    /**
     * إعداد خيارات اللوحة الجانبية ومعالجات الأحداث
     */
    private void setupDrawerOptions() {
        // تعيين الخيار الافتراضي المحدد
        if (optionGroup != null && homeOption != null) {
            optionGroup.setSelectedOptionButton(homeOption);
        }

        // إعداد مستمع النقر على الخيارات
        if (optionGroup != null) {
            optionGroup.setOnOptionButtonClickListener(new OptionGroup.OnOptionButtonClickListener() {
                @Override
                public void onOptionButtonClick(OptionButton optionButton, int position, int id) {
                    handleDrawerSelection(id);
                }
            });
        }

        // مستمع لإغلاق القائمة عند النقر على الأيقونة
        if (drawerLayout != null) {
            drawerLayout.setOnDrawerListener(new DrawerLayout.OnDrawerListener() {
                @Override
                public void onDrawerOpened() {
                    // يمكن إضافة منطق عند فتح الدرج
                }

                @Override
                public void onDrawerClosed() {
                    // يمكن إضافة منطق عند إغلاق الدرج
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
            // المستخدم في الصفحة الرئيسية بالفعل - لا نحتاج لفعل شيء
        } else if (id == R.id.option_scroll_list) {
            // الانتقال إلى نشاط قائمة التمرير
            Intent intent = new Intent(this, ScrollListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.option_settings) {
            // الانتقال إلى نشاط الإعدادات
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
        
        // التأكد من اختيار خيار الصفحة الرئيسية عند العودة للنشاط
        if (optionGroup != null && homeOption != null) {
            optionGroup.setSelectedOptionButton(homeOption);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen()) {
            drawerLayout.setDrawerOpen(false, true);
        } else {
            super.onBackPressed();
        }
    }
}
