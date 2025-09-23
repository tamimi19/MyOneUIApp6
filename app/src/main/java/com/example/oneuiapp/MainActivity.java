package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;

import de.dlyt.yanndroid.samsung.layout.DrawerLayout;
import de.dlyt.yanndroid.samsung.drawer.OptionButton;
import de.dlyt.yanndroid.samsung.drawer.OptionGroup;
import de.dlyt.yanndroid.samsung.drawer.Divider;

import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;

public class MainActivity extends AppCompatActivity {
    
    private DrawerLayout drawerLayout;
    private OptionGroup optionGroup;
    private OptionButton homeOption;
    private OptionButton scrollListOption;
    private OptionButton settingsOption;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply language and theme before creating
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
        // Set icons for drawer options
        homeOption.setIcon(getDrawable(R.drawable.ic_home));
        scrollListOption.setIcon(getDrawable(R.drawable.ic_scroll_list));
        settingsOption.setIcon(getDrawable(R.drawable.ic_settings));
        
        // Set default selection
        optionGroup.setSelectedOptionButton(homeOption);
        
        // Setup click listeners
        optionGroup.setOnOptionButtonClickListener(new OptionGroup.OnOptionButtonClickListener() {
            @Override
            public void onOptionButtonClick(OptionButton optionButton, int position, int id) {
                handleDrawerSelection(id);
            }
        });
        
        // Setup drawer icon click listener
        drawerLayout.setDrawerIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.setDrawerOpen(false, true);
            }
        });
    }
    
    private void handleDrawerSelection(int id) {
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
        // Refresh UI in case language or theme changed
        if (ThemeHelper.hasThemeChanged(this) || LanguageHelper.hasLanguageChanged(this)) {
            recreate();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
