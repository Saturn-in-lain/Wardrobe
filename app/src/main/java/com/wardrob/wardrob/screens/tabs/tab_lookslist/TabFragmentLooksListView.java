package com.wardrob.wardrob.screens.tabs.tab_lookslist;

import android.app.Activity;
import android.content.Context;
import android.widget.GridLayout;
import android.widget.GridView;


public interface TabFragmentLooksListView
{
    Context getThis();

    GridView getGridView();

    Activity getCurrentActivity(); //TODO: Remove if no need!
}
