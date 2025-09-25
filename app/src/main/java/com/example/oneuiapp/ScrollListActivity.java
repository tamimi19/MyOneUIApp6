package com.example.oneuiapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;

import de.dlyt.yanndroid.samsung.layout.ToolbarLayout;

import com.example.oneuiapp.adapters.ScrollListAdapter;
import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollListActivity extends AppCompatActivity {
    
    private ToolbarLayout toolbarLayout;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private ScrollListAdapter adapter;
    private List<String> itemList;
    private Handler snapHandler = new Handler();
    private Runnable snapRunnable;
    private boolean isUserScrolling = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);
        
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupSnapBehavior();
        generateScrollItems();
    }
    
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        recyclerView = findViewById(R.id.recycler_view);
        nestedScrollView = (NestedScrollView) recyclerView.getParent();
    }
    
    private void setupToolbar() {
        toolbarLayout.setTitle(getString(R.string.scroll_list_title));
        toolbarLayout.setSubtitle(getString(R.string.scroll_list_subtitle));
        
        toolbarLayout.setExpandable(true);
        toolbarLayout.setExpanded(false, false);
        
        setSupportActionBar(toolbarLayout.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        toolbarLayout.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupSnapBehavior() {
        if (nestedScrollView != null) {
            nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isUserScrolling = true;
                            if (snapRunnable != null) {
                                snapHandler.removeCallbacks(snapRunnable);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            isUserScrolling = false;
                            scheduleSnapToCollapsed();
                            break;
                    }
                    return false;
                }
            });
            
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > 0 && !isUserScrolling) {
                        scheduleSnapToCollapsed();
                    }
                }
            });
        }
    }
    
    private void scheduleSnapToCollapsed() {
        if (snapRunnable != null) {
            snapHandler.removeCallbacks(snapRunnable);
        }
        
        snapRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isUserScrolling) {
                    toolbarLayout.setExpanded(false, true);
                }
            }
        };
        
        snapHandler.postDelayed(snapRunnable, 150);
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        
        itemList = new ArrayList<>();
        adapter = new ScrollListAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
    
    private void generateScrollItems() {
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
        if (ThemeHelper.hasThemeChanged(this) || LanguageHelper.hasLanguageChanged(this)) {
            recreate();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (snapRunnable != null) {
            snapHandler.removeCallbacks(snapRunnable);
        }
    }
}
