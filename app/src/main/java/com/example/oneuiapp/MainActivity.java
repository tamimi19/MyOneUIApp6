package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import de.dlyt.yanndroid.oneui.widget.DrawerLayout;
import de.dlyt.yanndroid.oneui.widget.OptionButton;
import de.dlyt.yanndroid.oneui.widget.OptionGroup;

import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;
import com.example.oneuiapp.utils.CrashHandler;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
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

        initializeViews();
        setupDrawerOptions();
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        optionGroup = findViewById(R.id.option_group);
        homeOption = findViewById(R.id.option_home);
        scrollListOption = findViewById(R.id.option_scroll_list);
        settingsOption = findViewById(R.id.option_settings);
    }

    private void setupDrawerOptions() {
        // Set default selection
        optionGroup.setSelectedOptionButton(homeOption);

        // Setup click listeners
        optionGroup.setOnOptionButtonClickListener((optionButton, position, id) -> {
            handleDrawerSelection(id);
        });

        // Listener لإغلاق القائمة عند النقر على الأيقونة
        drawerLayout.setDrawerIconOnClickListener(v -> {
            drawerLayout.setDrawerOpen(false, true);
        });
    }

    private void handleDrawerSelection(int id) {
        // يغلق القائمة أولاً
        drawerLayout.setDrawerOpen(false, true);

        if (id == R.id.option_home) {
            // Already on home, do nothing
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
        // إعادة إنشاء النشاط إذا تغير الثيم أو اللغة
        if (ThemeHelper.hasThemeChanged(this) || LanguageHelper.hasLanguageChanged(this)) {
            recreate();
        }
        // تأكد من اختيار خيار الرئيسية
        if (optionGroup != null && homeOption != null) {
            optionGroup.setSelectedOptionButton(homeOption);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
