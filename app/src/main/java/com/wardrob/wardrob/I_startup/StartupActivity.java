package com.wardrob.wardrob.I_startup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wardrob.wardrob.II_mainscreen.MainActivity;
import com.wardrob.wardrob.I_startup_login_chooser.LoginChooserActivity;
import com.wardrob.wardrob.I_startup_login_chooser.LoginChooserPresenter;
import com.wardrob.wardrob.I_startup_new_member.LoginCreateActivity;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;

import timber.log.Timber;

public class StartupActivity extends GeneralActivity implements StartupView
{

    private StartupPresenter presenter = null;

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
        if (BuildConfig.DEBUG)
        {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
        else
        {
            Log.d("[WARNING]", " \n NO messages from Timber! \n");
        }

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
        Button login_button = (Button) findViewById(R.id.login_button);

        if (!presenter.isUsersPresentInDatabase())
        {
            login_button.setText(ResourcesGetterSingleton.getStr(R.string.login_create));
        }
        else
        {
            ImageView image_logo        = (ImageView) findViewById(R.id.image_logo);
            TextView login_welcome_text = (TextView) findViewById(R.id.login_welcome_text);

            presenter.setActiveUser(image_logo, login_welcome_text);
            login_button.setText(ResourcesGetterSingleton.getStr(R.string.login_button_text));
        }
    }



    /**
     * Function: onStartApplication
     * @param view View
     */
    public void onStartApplication(View view)
    {
        Intent intent;
        if (!presenter.isUsersPresentInDatabase())
        {
            intent = new Intent(this, LoginCreateActivity.class);
        }
        else
        {
            intent = new Intent(this, MainActivity.class);
        }
        this.startActivity(intent);
    }


    /**
     * Function: onChooseUser
     * @param view View
     */
    public void onChooseUser(View view)
    {
        Intent intent = new Intent(this, LoginChooserActivity.class);
        this.startActivity(intent);
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
