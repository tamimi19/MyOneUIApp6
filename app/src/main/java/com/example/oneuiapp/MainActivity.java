package com.example.oneuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import de.dlyt.yanndroid.samsung.layout.DrawerLayout;
import de.dlyt.yanndroid.samsung.drawer.OptionButton;
import de.dlyt.yanndroid.samsung.drawer.OptionGroup;


import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;
import com.example.oneuiapp.utils.CrashHandler;

public class MainActivity extends AppCompatActivity {
    
    private DrawerLayout drawerLayout;
    private OptionGroup optionGroup;
    private OptionButton homeOption;
    private OptionButton scrollListOption;
    private OptionButton settingsOption;
    private NestedScrollView mainScrollView;
    
    private boolean isToolbarCollapsed = false;
    private static final int SCROLL_THRESHOLD = 200; // Pixels to trigger collapse
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup crash handler first
        CrashHandler.setupCrashHandler(this);
        
        // Apply language and theme before creating
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupDrawerOptions();
        setupScrollBehavior();
    }
    
    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        optionGroup = findViewById(R.id.option_group);
        homeOption = findViewById(R.id.option_home);
        scrollListOption = findViewById(R.id.option_scroll_list);
        settingsOption = findViewById(R.id.option_settings);
        mainScrollView = findViewById(R.id.main_scroll_view);
        
        // Set toolbar initial state
        drawerLayout.setToolbarExpanded(false, false);
    }
    
    private void setupDrawerOptions() {
        // Remove icon assignments as they're causing build errors
        // The Samsung OneUI library will handle styling automatically
        
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
    
    private void setupScrollBehavior() {
        mainScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                handleToolbarCollapse(scrollY);
            }
        });
    }
    
    private void handleToolbarCollapse(int scrollY) {
        if (scrollY > SCROLL_THRESHOLD && !isToolbarCollapsed) {
            // Collapse toolbar - show title on side
            drawerLayout.setToolbarExpanded(false, true);
            isToolbarCollapsed = true;
        } else if (scrollY <= SCROLL_THRESHOLD && isToolbarCollapsed) {
            // Expand toolbar - show full header
            drawerLayout.setToolbarExpanded(false, true);
            isToolbarCollapsed = false;
        }
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
        
        // Ensure home option is selected when returning to MainActivity
        if (optionGroup != null && homeOption != null) {
            optionGroup.setSelectedOptionButton(homeOption);
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
