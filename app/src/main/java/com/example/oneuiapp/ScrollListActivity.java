package com.example.oneuiapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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
    
    // Variables for custom over-scroll behavior
    private boolean isAtTop = true;
    private boolean hasStoppedAtCollapsed = false;
    private float lastTouchY = 0;
    private boolean isTracking = false;
    
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
        setupCustomScrollBehavior();
    }
    
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        recyclerView = findViewById(R.id.recycler_view);
    }
    
    private void setupToolbar() {
        toolbarLayout.setTitle(getString(R.string.scroll_list_title));
        toolbarLayout.setSubtitle(getString(R.string.scroll_list_subtitle));
        
        // Set toolbar to collapsed state by default
        toolbarLayout.setExpandable(true);
        toolbarLayout.setExpanded(false, false);
        
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
        
        // Initially disable nested scrolling to control behavior manually
        recyclerView.setNestedScrollingEnabled(false);
        
        // Add scroll listener to track position
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                View firstVisibleView = layoutManager.findViewByPosition(firstVisiblePosition);
                
                // Check if we're at the top
                isAtTop = (firstVisiblePosition == 0 && 
                          (firstVisibleView == null || firstVisibleView.getTop() >= 0));
                
                // If scrolling down and not at top, collapse toolbar
                if (dy > 0 && !isAtTop) {
                    toolbarLayout.setExpanded(false, true);
                    hasStoppedAtCollapsed = false;
                    recyclerView.setNestedScrollingEnabled(true);
                }
            }
            
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Reset when scroll stops
                    if (isAtTop) {
                        recyclerView.setNestedScrollingEnabled(false);
                    }
                }
            }
        });
        
        itemList = new ArrayList<>();
        adapter = new ScrollListAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
    
    private void setupCustomScrollBehavior() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchY = event.getY();
                        isTracking = true;
                        break;
                        
                    case MotionEvent.ACTION_MOVE:
                        if (isTracking && isAtTop) {
                            float currentY = event.getY();
                            float deltaY = currentY - lastTouchY;
                            
                            // If pulling down while at top
                            if (deltaY > 0) {
                                if (!hasStoppedAtCollapsed) {
                                    // First pull down - stop at collapsed state
                                    hasStoppedAtCollapsed = true;
                                    toolbarLayout.setExpanded(false, false);
                                    return true; // Consume the event to prevent default scroll
                                } else {
                                    // Second pull down - allow expansion
                                    if (deltaY > 100) { // Threshold for expansion
                                        toolbarLayout.setExpanded(true, true);
                                        recyclerView.setNestedScrollingEnabled(true);
                                        hasStoppedAtCollapsed = false;
                                    }
                                }
                            }
                        }
                        break;
                        
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isTracking = false;
                        
                        // If we stopped at top in collapsed state, keep it collapsed
                        if (isAtTop && hasStoppedAtCollapsed) {
                            toolbarLayout.setExpanded(false, false);
                        }
                        break;
                }
                return false; // Allow normal touch handling for other cases
            }
        });
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
