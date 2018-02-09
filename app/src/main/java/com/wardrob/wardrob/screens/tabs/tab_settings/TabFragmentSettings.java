package com.wardrob.wardrob.screens.tabs.tab_settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wardrob.wardrob.R;

public class TabFragmentSettings extends Fragment implements TabFragmentSettingsView
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tab_fragment_settings, container, false);
    }


}
