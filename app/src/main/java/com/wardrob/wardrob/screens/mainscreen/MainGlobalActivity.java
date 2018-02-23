package com.wardrob.wardrob.screens.mainscreen;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.adapter.SwipePagerAdapter;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import timber.log.Timber;

import static android.icu.text.DateTimePatternGenerator.PatternInfo.OK;

public class MainGlobalActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    TabLayout.Tab SettingsTab;

    private static final int REQUEST_CODE_RESOLUTION = 1;
    private static final  int REQUEST_CODE_OPENER = 2;
    private GoogleApiClient mGoogleApiClient;
    private boolean fileOperation = false;
    private DriveId mFileId;
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

    @Override
    public void onResume()
    {
        super.onResume();
        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient != null)
        {
            // disconnect Google API client connection
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    //============================================================================================//
    /**
     *  Open list of folder and file of the Google Drive
     */
    public void OpenFileFromGoogleDrive()
    {
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "text/plain", "text/html" })
                .build(mGoogleApiClient);
        try
        {
            this.startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER,
                    null, 0, 0, 0);
        }
        catch (IntentSender.SendIntentException e) { Timber.d("Issue with: " + e.toString());}
    }

    /**
     * This is Result result handler of Drive contents.
     * this callback method call CreateFileOnGoogleDrive() method
     * and also call OpenFileFromGoogleDrive() method,
     * send intent onActivityResult() method to handle result.
     */
    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>()
            {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult)
                {
                    if (fileOperation == true)
                    {
                        CreateFileOnGoogleDrive(driveContentsResult);
                    }
                    else
                    {
                        OpenFileFromGoogleDrive();
                    }
                }
            };

    /**
     * Handle result of Created file
     */
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback =
            new ResultCallback<DriveFolder.DriveFileResult>()
            {
                @Override
                public void onResult(DriveFolder.DriveFileResult result)
                {
                    if (result.getStatus().isSuccess())
                    {
                        Toast.makeText(getBaseContext(), "file created: " +
                                result.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            };


    /**
     * Create a file in root folder using MetadataChangeSet object.
     * @param result
     */
    public void CreateFileOnGoogleDrive(DriveApi.DriveContentsResult result)
    {
        final DriveContents driveContents = result.getDriveContents();
        new Thread()
        {
            @Override
            public void run()
            {
                // write content to DriveContents
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try
                {
                    writer.write("Hello abhay!");
                    writer.close();
                }
                catch (IOException e) {}

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("Set title")
                        .setMimeType("text/plain")
                        .setStarred(true).build();

                // create a file in root folder
                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents)
                        .setResultCallback(fileCallback);
            }
        }.start();
    }

    public void onClickCreate(View v)
    {
        fileOperation = true;
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    public void onClickOpen(View v)
    {
        fileOperation = false;
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }
    //============================================================================================//

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    @Override
    public void onLocationChanged(Location location)
    {
        Timber.d("We have some issues here!!!");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     *  Handle Response of selected file
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_OPENER:
                    Timber.d("\t\t[REQUEST_CODE_OPENER]");
                    break;

                case REQUEST_CODE_RESOLUTION:   // : //
                    Timber.d("\t\t[REQUEST_CODE_RESOLUTION]");

                    mFileId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    String url = "https://drive.google.com/open?id="+ mFileId.getResourceId();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    Timber.d("\t\t[onActivityResult][DEFAULT]");
                    break;
            }
         }
    }

    //--------------------------------------------------------------------------------------------

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Toast.makeText(this, "Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Timber.d("\t\t[onConnectionFailed]-->");
        if (!connectionResult.hasResolution())
        {
            GoogleApiAvailability.getInstance().getErrorDialog(this,
                    connectionResult.getErrorCode(), 0).show();

            Timber.d("\t\t[onConnectionFailed]-->[1]");

            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */
        try
        {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
            Timber.d("\t\t[onConnectionFailed]-->[2]");
        }
        catch (IntentSender.SendIntentException e)
        {
            Timber.d("We have issue: " + e.toString());
        }
    }
}
