package com.wardrob.wardrob.fragment_look;

import android.app.Activity;
import android.content.Context;
import android.widget.GridLayout;

public interface FragmentLookItemView
{
    Context getThis();

    GridLayout getItemListLayout();

    Activity getCurrentActivity();
}
