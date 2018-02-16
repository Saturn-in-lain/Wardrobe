package com.wardrob.wardrob.screens.fragments.fragment_startup_chooser;

import android.content.Context;
import android.widget.LinearLayout;


public interface FragmentStartupChooserView
{
    Context getThis();

    LinearLayout getAvailableUsersLayout();

    void closeFragment();
}
