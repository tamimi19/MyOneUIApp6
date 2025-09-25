package com.example.oneuiapp;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.dlyt.yanndroid.oneui.widget.ToolbarLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // إعداد اللغة والثيم قبل الإنشاء
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

        // Collapse the toolbar by default
        toolbarLayout.setExpandable(true);
        toolbarLayout.setExpanded(false, false);

        setSupportActionBar(toolbarLayout.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarLayout.setNavigationOnClickListener(v -> onBackPressed());
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
        // توليد 200 عنصر
        for (int i = 1; i <= 200; i++) {
            String itemText = LanguageHelper.getLanguage(this).equals("ar") ?
                "العنصر رقم " + i : "Item " + i;
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
        // إعادة إنشاء النشاط إذا تغير الثيم أو اللغة
        if (ThemeHelper.hasThemeChanged(this) || LanguageHelper.hasLanguageChanged(this)) {
            recreate();
        }
    }
}
