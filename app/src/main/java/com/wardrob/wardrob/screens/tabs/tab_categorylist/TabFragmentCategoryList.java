package com.wardrob.wardrob.screens.tabs.tab_categorylist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.screens.listscreen.ItemsListActivity;

import java.util.HashMap;

import timber.log.Timber;

public class TabFragmentCategoryList extends Fragment implements TabFragmentCategoryListView
{
    private ViewGroup const_container = null;
    public Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        const_container = container;

        View view = inflater.inflate(R.layout.tab_fragment_itemlist,
                                    const_container,
                                    false);

        //--------------------------------------------------------------------------------------
        ImageView icon_category_upper = (ImageView) view.findViewById(R.id.icon_category_upper);
        icon_category_upper.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                prepareFragmentActivity(getString(R.string.menu_upper_level));
            }
        });
        //--------------------------------------------------------------------------------------
        ImageView icon_category_lower = (ImageView) view.findViewById(R.id.icon_category_lower);
        icon_category_lower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                prepareFragmentActivity(getString(R.string.menu_lower_level));
            }
        });
        //--------------------------------------------------------------------------------------
        return view;
    }

    @Override
    public void onAttach (Context context)
    {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * Function: prepareFragmentActivity
     * @param category
     * @Note: Send information for ItemsListActivity to prepare fragment with required
     * list information.
     */
    private void prepareFragmentActivity(String category)
    {
        Intent intent = new Intent(this.context, ItemsListActivity.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item_index), category);
        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_state),
                    ResourcesGetterSingleton.getStr(R.string.state_open));
        intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);

        TabFragmentCategoryList.this.startActivity(intent);
    }


}
