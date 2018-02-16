package com.wardrob.wardrob.screens.fragments.fragment_startup_creater;


import android.app.FragmentManager;
import android.content.Context;

public interface FragmentStartupCreateView
{
    Context getThis();

    FragmentManager getFragmentManager();

    void closeFragment();
}
