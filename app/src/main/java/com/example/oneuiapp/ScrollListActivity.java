// app/src/main/java/com/example/oneuiapp/ScrollListActivity.java
package com.example.oneuiapp;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// استيراد الحزمة الجديدة لشريط الأدوات
import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;

import com.example.oneuiapp.adapters.ScrollListAdapter;
import com.example.oneuiapp.utils.ThemeHelper;
import com.example.oneuiapp.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollListActivity extends AppCompatActivity {
    
    // متغيرات المكونات الرئيسية
    private ToolbarLayout toolbarLayout;
    private RecyclerView recyclerView;
    private ScrollListAdapter adapter;
    private List<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // إعداد اللغة والثيم قبل إنشاء النشاط
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        ThemeHelper.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);

        // تهيئة المكونات وإعداد شريط الأدوات والقائمة
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        generateScrollItems();
    }

    /**
     * تهيئة المتغيرات الخاصة بالمكونات المرئية
     */
    private void initializeViews() {
        toolbarLayout = findViewById(R.id.toolbar_layout);
        recyclerView = findViewById(R.id.recycler_view);
    }

    /**
     * إعداد شريط الأدوات والعنوان
     */
    private void setupToolbar() {
        if (toolbarLayout != null) {
            setSupportActionBar(toolbarLayout.getToolbar());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            toolbarLayout.setExpandable(true);
            toolbarLayout.setExpanded(false, false);

            toolbarLayout.setOnNavigationClickListener(new ToolbarLayout.OnNavigationClickListener() {
                @Override
                public void onClick() {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * إعداد قائمة العناصر القابلة للتمرير
     */
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

    /**
     * توليد عناصر القائمة للتجربة (200 عنصر)
     */
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // معالجة النقر على زر الرجوع في شريط الأدوات
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
