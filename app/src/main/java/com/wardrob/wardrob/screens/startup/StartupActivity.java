package com.wardrob.wardrob.screens.startup;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.wardrob.wardrob.screens.fragments.fragment_startup_chooser.FragmentStartupChooser;
import com.wardrob.wardrob.screens.fragments.fragment_startup_creater.FragmentStartupCreate;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.GeneralActivity;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.screens.fragments.fragment_startup.FragmentStartup;

import timber.log.Timber;

public class StartupActivity extends GeneralActivity implements StartupView
{
    private StartupPresenter presenter = null;

    FragmentManager fragmentManager = getFragmentManager();

    FragmentStartup         fragment_start;
    FragmentStartupCreate   fragment_create  = new FragmentStartupCreate();
    FragmentStartupChooser  fragment_chooser = new FragmentStartupChooser();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        // -----------------------------------------------------------
        // Resources
        // -----------------------------------------------------------
        ResourcesGetterSingleton.getInstance(this);
        checkPermissionsForApplication(this, true);


        // -----------------------------------------------------------
        // Debug
        // TODO *Check if I'm not creating new plant each time...
        // -----------------------------------------------------------
        Timber.uprootAll();
        Timber.plant(new Timber.DebugTree());


        // -----------------------------------------------------------
        // Presenter
        // -----------------------------------------------------------
        presenter = new StartupPresenter(this);
        presenter.initStaringProcess();
        // -----------------------------------------------------------


        // -----------------------------------------------------------
        // Test for color gradient
        // -----------------------------------------------------------
//        Shader textShader=new LinearGradient(0, 0, 0, 20,
//                new int[]{Color.GREEN, Color.BLUE},
//                new float[]{0, 1}, Shader.TileMode.CLAMP);
//        login_welcome_text.getPaint().setShader(textShader);
        // -----------------------------------------------------------


    }

    /**
     * Function: onStart
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        defineUser();
    }



    /**
     * Function: defineUser
     */
    private void defineUser()
    {
        Bundle args = new Bundle();
        if (presenter.isUsersPresentInDatabase())
        {
            fragment_start   = new FragmentStartup();
            args.putString(ResourcesGetterSingleton.getStr(R.string.bundle_state), "user_available");
            fragment_start.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.startup_container, fragment_start)
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.startup_container, fragment_create)
                    .commit();
        }

    }

    /**
     * Function: onChooseUser
     * @param view View
     */
    public void onChooseUser(View view)
    {
        fragmentManager.beginTransaction()
                .replace(R.id.startup_container, fragment_chooser).addToBackStack("chooser").commit();
    }

    /**
     * Function: onCreateNewMember
     * @param view {@link View}
     */
    public void onCreateNewMember(View view)
    {
        fragmentManager.beginTransaction()
                .replace(R.id.startup_container, fragment_create)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        fragment_create.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Function: getThis
     * @return this
     */
    @Override
    public Context getThis()
    {
        return this;
    }
}
