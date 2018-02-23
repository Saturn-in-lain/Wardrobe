package com.wardrob.wardrob.screens.tabs.tab_lookslist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.screens.listscreen.ItemsListActivity;
import com.wardrob.wardrob.screens.mainscreen.MainGlobalActivity;
import com.wardrob.wardrob.screens.new_item.NewItemActivity;
import com.wardrob.wardrob.screens.new_look.NewLookActivity;

import java.util.HashMap;

public class TabFragmentLooksList extends Fragment implements TabFragmentLooksListView
{
    View view;
    FloatingActionButton fab_new_look;
    TabFragmentLooksListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_fragment_lookslist, container, false);


        presenter = new TabFragmentLooksListPresenter(this);

        fab_new_look = (FloatingActionButton) view.findViewById(R.id.fab_new_look);
        fab_new_look.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                presenter.initializeTempLookItem();

                Intent intent = new Intent(view.getContext(), NewLookActivity.class);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item), "111");
                intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.refreshLooksList();
    }

    @Override
    public Context getThis()
    {
        return getContext();
    }


    @Override
    public GridView getGridView()
    {
        return (GridView) view.findViewById(R.id.rltListOfLooks);
    }

    @Override
    public Activity getCurrentActivity() {
        return getActivity();
    }
}
