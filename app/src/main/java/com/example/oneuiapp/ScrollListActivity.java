package com.example.oneuiapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.dlyt.yanndroid.samsung.layout.ToolbarLayout;

import com.example.oneuiapp.adapters.ScrollListAdapter;
import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollListActivity extends AppCompatActivity {
    
    private ToolbarLayout toolbarLayout;
    private RecyclerView recyclerView;
    private ScrollListAdapter adapter;
    private List<String> itemList;
    
    // Variables to control scroll behavior
    private boolean isToolbarCollapsed = false;
    private boolean snapToCollapsed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply language and theme before creating
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);
        
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        generateScrollItems();
    }
    
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        recyclerView = findViewById(R.id.recycler_view);
    }
    
    private void setupToolbar() {
        toolbarLayout.setTitle(getString(R.string.scroll_list_title));
        toolbarLayout.setSubtitle(getString(R.string.scroll_list_subtitle));
        toolbarLayout.setNavigationIcon(getDrawable(R.drawable.ic_samsung_back));
        
        // Make toolbar expandable with default expanded state
        toolbarLayout.setExpandable(true);
        toolbarLayout.setExpanded(true, false);
        
        // Enable action bar
        setSupportActionBar(toolbarLayout.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Setup navigation click listener
        toolbarLayout.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        
        // Enable nested scrolling for toolbar collapse behavior
        recyclerView.setNestedScrollingEnabled(true);
        
        // Add scroll listener for enhanced control
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollDy = 0;
            private static final int SCROLL_THRESHOLD = 20;
            
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                scrollDy += dy;
                
                // Scroll down - collapse toolbar
                if (dy > SCROLL_THRESHOLD && !isToolbarCollapsed) {
                    toolbarLayout.setExpanded(false, true);
                    isToolbarCollapsed = true;
                }
                // Scroll up - expand toolbar  
                else if (dy < -SCROLL_THRESHOLD && isToolbarCollapsed) {
                    toolbarLayout.setExpanded(true, true);
                    isToolbarCollapsed = false;
                }
            }
            
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                
                // Reset scroll accumulator when scroll stops
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollDy = 0;
                }
            }
        });
        
        itemList = new ArrayList<>();
        adapter = new ScrollListAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
    
    private void generateScrollItems() {
        // Generate 200 items as requested
        for (int i = 1; i <= 200; i++) {
            String itemText;
            if (LanguageHelper.getLanguage(this).equals("ar")) {
                itemText = "العنصر رقم " + i;
            } else {
                itemText = "Item " + i;
            }
            itemList.add(itemText);
        }
        adapter.notifyDataSetChanged();
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
        // Refresh UI in case language or theme changed
        if (ThemeHelper.hasThemeChanged(this) || LanguageHelper.hasLanguageChanged(this)) {
            recreate();
        }
    }
}
