package com.wardrob.wardrob.screens.tabs.tab_lookslist;

import android.app.Activity;
import android.content.Context;
import android.widget.GridLayout;


public interface TabFragmentLooksListView {
    Context getThis();

    GridLayout getGridLayout();

    Activity getCurrentActivity();
}
