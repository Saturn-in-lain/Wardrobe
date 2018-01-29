package com.wardrob.wardrob.fragments;


import android.app.Activity;
import android.content.Context;
import android.widget.GridLayout;

public interface FragmentLookView
{

    Context getThis();

    GridLayout getItemListLayout();

    Activity getCurrentActivity();

}
