package com.wardrob.wardrob.screens.fragments.fragment_startup_chooser;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.screens.fragments.fragment_startup.FragmentStartup;


public class FragmentStartupChooser extends Fragment implements FragmentStartupChooserView
{

    View view;
    FragmentStartupChooserPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_startup_chooser,
                container,
                false);

        presenter = new FragmentStartupChooserPresenter(this);

        return view;
    }



    @Override
    public Context getThis()
    {
        return this.getContext();
    }

    /**
     * Function: getAvailableUsersLayout
     * @return LinearLayout
     */
    @Override
    public LinearLayout getAvailableUsersLayout()
    {
        LinearLayout lAvatartWithFamilyMembers =
                (LinearLayout) view.findViewById(R.id.lAvatartWithFamilyMembers);
        return  lAvatartWithFamilyMembers;
    }

    @Override
    public void closeFragment()
    {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

        FragmentStartup fr = new FragmentStartup();
        Bundle args = new Bundle();
        args.putString(ResourcesGetterSingleton.getStr(R.string.bundle_state), "user_available");
        fr.setArguments(args);

        fragmentTransaction.add(R.id.startup_container, fr , fr.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(this.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }
}
