package com.wardrob.wardrob.II_mainscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wardrob.wardrob.III_newitem.NewItemActivity;
import com.wardrob.wardrob.IV_newlook.NewLookActivity;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.PlaceholderFragment;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.fragments.FragmentLook;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView
{

    private CharSequence    mTitle;
    private Integer         stateOfFragments = -1;
    private MainPresenter   presenter;

    private final Integer CONST_UPPER       = 0;
    private final Integer CONST_LOWER       = 1;
    private final Integer CONST_WARM        = 2;
    private final Integer CONST_HAT         = 3;
    private final Integer CONST_BOOTS       = 4;
    private final Integer CONST_ACCESSORIES = 5;
    private final Integer CONST_LOOK        = 6;
    private final Integer CONST_WISHLIST    = 7;
    private final Integer CONST_WEATHER     = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this);

        // --------------------------------------------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if(stateOfFragments == CONST_LOOK)
                {
                    //TODO: Here we creating new lool

                    Intent intent = new Intent(getThis(), NewLookActivity.class);
                    MainActivity.this.startActivity(intent);
                }
                else
                {
                        Intent intent = new Intent(getThis(), NewItemActivity.class);
                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item),
                                presenter.getFragmentFolderPath(stateOfFragments));
                        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item_index),
                                stateOfFragments.toString());
                        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_id_item), "none");

                        intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
                        MainActivity.this.startActivity(intent);

                    //Snackbar.make(view, "Fragment number: "+stateOfFragments.toString() ,
                    //              Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        // --------------------------------------------------------------------------------------


        // --------------------------------------------------------------------------------------

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                                                    R.string.navigation_drawer_open,
                                                                    R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // --------------------------------------------------------------------------------------



        // --------------------------------------------------------------------------------------
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView txtName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtHeaderText);
        txtName.setText(getResources().getString(R.string.app_name));
        navigationView.setNavigationItemSelectedListener(this);
        // --------------------------------------------------------------------------------------
}

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int position = 0;

        Fragment fragment = null;

        switch (id)
        {
            case R.id.nav_upper_level:
                mTitle = getString(R.string.menu_upper_level);
                position = CONST_UPPER;

                fragment = new FragmentLook();
                Bundle args = new Bundle();

                args.putString(FragmentLook.ITEM_NAME, (String) mTitle);
                args.putInt(FragmentLook.IMAGE_RESOURCE_ID, R.drawable.coat);
                fragment.setArguments(args);

                break;
            case R.id.nav_lower_level:
                mTitle = getString(R.string.menu_lower_level);
                position = CONST_LOWER;
                break;
            case R.id.nav_warm_level:
                mTitle = getString(R.string.menu_warm_clothes);
                position = CONST_WARM;
                break;
            case R.id.nav_hats_level:
                mTitle = getString(R.string.menu_hats);
                position = CONST_HAT;
                break;

            case R.id.nav_boots_level:
                mTitle = getString(R.string.menu_boots);
                position = CONST_HAT;
                break;

            case R.id.nav_accessories_level:
                mTitle = getString(R.string.menu_accessories);
                position = CONST_ACCESSORIES;
                break;

            case R.id.nav_my_looks:
                mTitle = getString(R.string.menu_my_looks);
                position = CONST_LOOK;
                break;

            case R.id.nav_wish_list:
                mTitle = getString(R.string.menu_wish_list);
                position = CONST_WISHLIST;
                break;
            case R.id.nav_weather:
                mTitle = getString(R.string.menu_weather_settings);
                position = CONST_WEATHER;
                break;
        }


        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        FragmentManager fragmentManager = getSupportFragmentManager();

        //TODO: This one is ugly part
        if (id == R.id.nav_upper_level)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container,
                            PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }

        stateOfFragments = position; /*Trick to know which fragment*/
        return true;
    }

    @Override
    public Context getThis()
    {
        return this;
    }
}
