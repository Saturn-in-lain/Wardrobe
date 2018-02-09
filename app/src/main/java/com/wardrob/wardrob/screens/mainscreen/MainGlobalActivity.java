package com.wardrob.wardrob.screens.mainscreen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.adapter.SwipePagerAdapter;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;

public class MainGlobalActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_global);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        TabLayout.Tab categoryItemsTab = tabLayout.newTab().setText(ResourcesGetterSingleton.getStr(R.string.tab_items));
        categoryItemsTab.setIcon(R.drawable.ic_menu_gallery);
        tabLayout.addTab(categoryItemsTab);

        TabLayout.Tab LooksItemsTab = tabLayout.newTab().setText(ResourcesGetterSingleton.getStr(R.string.tab_looks));
        LooksItemsTab.setIcon(R.drawable.ic_menu_slideshow);
        tabLayout.addTab(LooksItemsTab);

        TabLayout.Tab SettingsTab = tabLayout.newTab().setText(ResourcesGetterSingleton.getStr(R.string.tab_settings));
        SettingsTab.setIcon(R.drawable.ic_menu_share);
        tabLayout.addTab(SettingsTab);

        tabLayout.setMinimumWidth(200);
        tabLayout.setPadding(0,0,0,0);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final SwipePagerAdapter adapter = new SwipePagerAdapter( getSupportFragmentManager(),
                                                                 tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
