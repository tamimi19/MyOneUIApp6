package com.example.oneuiapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;

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
        if (toolbarLayout != null) {
            setSupportActionBar(toolbarLayout.getToolbar());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            toolbarLayout.setExpandable(true);
            toolbarLayout.setExpanded(false, false);

            // استبدال المستمع غير المدعوم بمستمع Toolbar القياسي
            toolbarLayout.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void setupRecyclerView() {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            itemList = new ArrayList<>();
            adapter = new ScrollListAdapter(itemList);
            recyclerView.setAdapter(adapter);
        }
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
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // الاستجابة لزر الرجوع في شريط الأدوات
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
}
