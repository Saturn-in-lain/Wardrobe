package com.wardrob.wardrob.fragment_look;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.wardrob.wardrob.R;

public class FragmentLookItems extends Fragment implements FragmentLookItemView
{
    public static final String IMAGE_RESOURCE_ID    = "iconResourceID";
    public static final String ITEM_NAME            = "itemName";

    FragmentLookItemsPresenter presenter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_layout_looks,
                container,
                false);


        presenter = new FragmentLookItemsPresenter(this);


        presenter.loadLookObjects(); //TODO

        return view;
    }



    @Override
    public Context getThis()
    {
        return this.getContext();
    }

    @Override
    public GridLayout getItemListLayout()
    {
        GridLayout l = (GridLayout) view.findViewById(R.id.rltListOfItems);
        return l;
    }

    @Override
    public Activity getCurrentActivity()
    {
        return getActivity();
    }
}