package com.wardrob.wardrob.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.wardrob.wardrob.R;

public class FragmentLook extends Fragment implements FragmentLookView
{
    public static final String IMAGE_RESOURCE_ID    = "iconResourceID";
    public static final String ITEM_NAME            = "itemName";

    FragmentLookPresenter presenter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_layout_looks,
                                    container,
                                    false);

        presenter = new FragmentLookPresenter(this);
        presenter.getListOfRelatedItems(getArguments().getString(ITEM_NAME));
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