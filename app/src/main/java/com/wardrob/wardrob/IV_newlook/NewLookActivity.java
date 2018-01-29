package com.wardrob.wardrob.IV_newlook;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.fragments.FragmentLook;


public class NewLookActivity extends AppCompatActivity implements NewLookView
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_look);

        ImageView imgHat          = (ImageView) findViewById(R.id.imgHat);
        ImageView imgAccessories  = (ImageView) findViewById(R.id.imgAccessories);
        ImageView imgUpperLevel   = (ImageView) findViewById(R.id.imgUpperLevel );
        ImageView imgWarmLevel    = (ImageView) findViewById(R.id.imgWarmLevel  );
        ImageView imgLowerLevel   = (ImageView) findViewById(R.id.imgLowerLevel );
        ImageView imgBoots        = (ImageView) findViewById(R.id.imgBoots      );

    }


    public void onHatSelectionClick (View v)
    {

    }

    public void onAccessoriesSelectionClick (View v)
    {

    }

    public void onUpperLevelSelectionClick (View v)
    {
//        Fragment fragment = new FragmentLook();
//        Bundle args = new Bundle();
//
//        args.putString(FragmentLook.ITEM_NAME, "TESTING CREATION FROM LOOK");
//        args.putInt(FragmentLook.IMAGE_RESOURCE_ID, R.drawable.shirt);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .commit();
    }

    public void onLowerLevelSelectionClick (View v)
    {

    }

    public void onWarmLevelSelectionClick (View v)
    {

    }

    public void onBootsSelectionClick (View v)
    {

    }

    @Override
    public Context getThis()
    {
        return this;
    }
}
