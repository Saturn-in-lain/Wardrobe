package com.wardrob.wardrob.screens.mainscreen;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.drive.DriveFile;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.adapter.SwipePagerAdapter;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.google_service.GoogleBaseActivity;

import java.util.List;

import timber.log.Timber;
//public class MainGlobalActivity extends BaseDemoActivity implements LocationListener
public class MainGlobalActivity extends GoogleBaseActivity implements LocationListener
{
    TabLayout.Tab SettingsTab;

    private static final int REQUEST_CODE_RESOLUTION    = 1;
    private static final int REQUEST_CODE_OPENER        = 2;
    private static final int REQUEST_CODE_CREATOR       = 3;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 4;
    private boolean fileOperation = false;

    public DriveFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_global);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        TabLayout.Tab categoryItemsTab = tabLayout.newTab().setText(ResourcesGetterSingleton.getStr(R.string.tab_items));
        categoryItemsTab.setIcon(R.drawable.ic_menu_gallery);
        tabLayout.addTab(categoryItemsTab);

        TabLayout.Tab LooksItemsTab = tabLayout.newTab().setText(ResourcesGetterSingleton.getStr(R.string.tab_looks));
        LooksItemsTab.setIcon(R.drawable.ic_menu_slideshow);
        tabLayout.addTab(LooksItemsTab);

        SettingsTab = tabLayout.newTab().setText(ResourcesGetterSingleton.getStr(R.string.tab_settings));
        SettingsTab.setIcon(R.drawable.ic_menu_share);
        tabLayout.addTab(SettingsTab);

        tabLayout.setMinimumWidth(200);
        tabLayout.setPadding(0,0,0,0);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final SwipePagerAdapter adapter = new SwipePagerAdapter( getSupportFragmentManager(),
                                                                 tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
            }
        });


    }
    //============================================================================================//
    /**
     * Function: onClickCreate
     * @param v View
     * @return:
     */
    public void onClickCreate(View v)
    {
        fileOperation = true;
        Timber.e("F:[onClickCreate]: START ");

        try
        {
            Timber.e("\t\t[onClickCreate] --> !!!\n");
            getResultsFromApi();
        }
        catch (Exception e)
        {
            Timber.e("F:[onClickCreate]: error -> " + e.toString());
        }
    }


    /**
     * Function: onClickOpen
     * @param v View
     * @return:
     */
    public void onClickOpen(View v)
    {
        fileOperation = false;
        Timber.e("F:[onClickOpen]: START ");
        try
        {
            Timber.e("\t[onClickOpen]\t-->initGoogleActivity!!!\n");
            initGoogleActivity();
        }
        catch (Exception e)
        {
            Timber.e("F:[onClickOpen]: error ->  " + e.toString());
        }
    }
    //============================================================================================//
    /**
     * Function: onCreateOptionsMenu
     * @param menu Menu
     * @return:
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Function: onOptionsItemSelected
     * @param item MenuItem
     * @return:
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------

    /**
     * Function: onLocationChanged
     * @param location Location
     * @return:
     */
    @Override
    public void onLocationChanged(Location location)
    {
        Timber.d("We have some issues here!!!");
    }

    /**
     * Function: onStatusChanged
     * @param provider String
     * @param status
     * @param extras
     * @return:
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Function: onProviderEnabled
     * @param provider Str
     * @return:
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Function: onProviderDisabled
     * @param provider Str
     * @return:
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

    //--------------------------------------------------------------------------------------------
    //                           NEW VARIANT FOR CONNECTION
    //--------------------------------------------------------------------------------------------
    @Override
    protected void onDriveClientReady()
    {
        Timber.d("\t\t\t<---[onDriveClientReady]--->");

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms)
    {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Do nothing.
    }
    //--------------------------------------------------------------------------------------------
}
