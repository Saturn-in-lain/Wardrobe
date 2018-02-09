package com.wardrob.wardrob.screens.fragments.fragment_itemslist;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;

public class FragmentItemsList extends Fragment implements FragmentLookView
{
    public static final String IMAGE_RESOURCE_ID    = "iconResourceID";
    public static final String ITEM_NAME            = "itemName";
    public static final String ITEM_STATE           = "itemState";

    FragmentItemsListPresenter presenter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_layout_items_list,
                                    container,
                                    false);

        Bundle args = getArguments();
        String state = args.getString(FragmentItemsList.ITEM_STATE, null);
        if(state.equals(ResourcesGetterSingleton.getStr(R.string.state_pick)))
        {
            presenter = new FragmentItemsListPresenter(this, true,
                                                        args.getString(ITEM_NAME));
        }
        else
        {
            presenter = new FragmentItemsListPresenter(this, false,
                                                        args.getString(ITEM_NAME));
        }

        presenter.getListOfRelatedItems();
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