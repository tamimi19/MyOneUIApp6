package com.example.oneuiapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.dlyt.yanndroid.samsung.layout.ToolbarLayout;
import com.google.android.material.appbar.AppBarLayout;

import com.example.oneuiapp.adapters.ScrollListAdapter;
import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollListActivity extends AppCompatActivity {
    
    private ToolbarLayout toolbarLayout;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private ScrollListAdapter adapter;
    private List<String> itemList;
    
    // Variables to control scroll behavior
    private boolean isToolbarCollapsed = true;
    private boolean snapToCollapsed = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply language and theme before creating
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);
        
        initializeViews();
        setupToolbar();
        setupScrollBehavior();
        setupRecyclerView();
        generateScrollItems();
    }
    
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.app_bar);
        recyclerView = findViewById(R.id.recycler_view);
    }
    
    private void setupToolbar() {
        toolbarLayout.setTitle(getString(R.string.scroll_list_title));
        toolbarLayout.setSubtitle(getString(R.string.scroll_list_subtitle));
        toolbarLayout.setNavigationIcon(getDrawable(R.drawable.ic_back));
        
        // Enable action bar
        setSupportActionBar(toolbarLayout.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Setup navigation click listener
        toolbarLayout.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }
    
    private void setupScrollBehavior() {
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    int totalScrollRange = appBarLayout.getTotalScrollRange();
                    
                    // Calculate collapse ratio (0 = fully expanded, 1 = fully collapsed)
                    float collapseRatio = (float) Math.abs(verticalOffset) / (float) totalScrollRange;
                    
                    // Check if toolbar is approaching collapsed state (80% collapsed)
                    if (collapseRatio >= 0.8f && !isToolbarCollapsed && !snapToCollapsed) {
                        snapToCollapsed = true;
                        iSnap to collapsed statesToolbarCollapsed = true;
                        
                        // Temporarily disable nested scrolling to prevent over-scroll
                        recyclerView.setNestedScrollingEnabled(true);
                        
                        // 
                        appBarLayout.setExpanded(false, true);
                        
                        // Re-enable scrolling after animation
                        recyclerView.postDelayed(() -> {
                            recyclerView.setNestedScrollingEnabled(true);
                            snapToCollapsed = false;
                        }, 300);
                        
                    } else if (collapseRatio < 0.1f) {
                        // Toolbar is expanded
                        isToolbarCollapsed = false;
                    }
                }
            });
        }
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        
        // Enable nested scrolling for toolbar collapse
        recyclerView.setNestedScrollingEnabled(true);
        
        // Add scroll listener for additional control
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                // If scrolling up and toolbar is collapsed, allow expansion only with deliberate scroll
                if (dy < 0 && isToolbarCollapsed) {
                    // Only expand if scroll is significant (more than 50 pixels)
                    if (Math.abs(dy) > 50) {
                        appBarLayout.setExpanded(true, true);
                        isToolbarCollapsed = false;
                    }
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
