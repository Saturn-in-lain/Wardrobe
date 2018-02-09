package com.wardrob.wardrob.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wardrob.wardrob.screens.tabs.tab_categorylist.TabFragmentCategoryList;
import com.wardrob.wardrob.screens.tabs.tab_lookslist.TabFragmentLooksList;
import com.wardrob.wardrob.screens.tabs.tab_settings.TabFragmentSettings;

public class SwipePagerAdapter extends FragmentStatePagerAdapter
{
    int mNumOfTabs;

    public SwipePagerAdapter(FragmentManager fm, int NumOfTabs)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                TabFragmentCategoryList tab1 = new TabFragmentCategoryList();
                return tab1;
            case 1:
                TabFragmentLooksList tab2 = new TabFragmentLooksList();
                return tab2;
            case 2:
                TabFragmentSettings tab3 = new TabFragmentSettings();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return mNumOfTabs;
    }

}
