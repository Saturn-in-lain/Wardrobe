package com.wardrob.wardrob.google_service;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.wardrob.wardrob.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

/**
 * An abstract activity that handles authorization and connection to the Drive
 * services.
 *
 * TODO: Probably deprecated....
 *
 */
public abstract class BaseDemoActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, BaseDemoView, EasyPermissions.PermissionCallbacks
{
    /**
     * Request code for google sign-in
     */
    protected static final int REQUEST_CODE_SIGN_IN = 0;

    /**
     * Request code for the Drive picker
     */
    protected static final int REQUEST_CODE_OPEN_ITEM = 1;

    /**
     * Handles high-level drive functions like sync
     */
    private DriveClient mDriveClient;

    /**
     * Handle access to Drive resources/files.
     */
    private DriveResourceClient mDriveResourceClient;

    /**
     * Tracks completion of the drive picker
     */
    private TaskCompletionSource<DriveId> mOpenItemTaskSource;


    /**
     * Credentials from Google
     */
    GoogleAccountCredential mCredential;

    /**
     * Presenter
     */
    BaseDemoPresenter presenter;

    /**
     * TODO: Here is old variant for connection
     */
    private GoogleApiClient mGoogleApiClient = null;
    private boolean isAPIConnected = false;


    /**
     * Function: onStart
     */
    @Override
    protected void onStart()
    {
        super.onStart();

        presenter = new BaseDemoPresenter(this);

        //////////////////////////////////////////////////////////////
        connectAPIClient();     // VARIANT I
        //////////////////////////////////////////////////////////////
        signIn();               // VARIANT II
        //////////////////////////////////////////////////////////////


        //backupInitialization();
    }

    /**
     * Function: backupInitialization
     */
    private void backupInitialization()
    {
        //Step: 1 - Create if not exist
        presenter.createFoldersForBackupOnGoogleDrive();
    }


    /**
     * Function: onStop
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        super.onPause();
    }
    /**
     * Handles resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode != RESULT_OK)
                {
                    // Sign-in may fail or be cancelled by the user. For this sample, sign-in is
                    // required and is fatal. For apps where sign-in is optional, handle
                    // appropriately
                    Timber.e("\t\t Sign-in failed. [1] --> resultCode=" + String.valueOf(resultCode));
                    ///////////////////////////////////////////////////////////////////////////////////////
                    // Investigation!
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    Timber.d("handleSignInResult:" + result.getStatus().toString());
                    Toast.makeText(this,"Fuck: handleSignInResult:"+result.getStatus().toString(),Toast.LENGTH_LONG).show();
                    ///////////////////////////////////////////////////////////////////////////////////////
                    return;
                }

                Task<GoogleSignInAccount> getAccountTask =
                        GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful())
                {
                    initializeDriveClient(getAccountTask.getResult());
                }
                else
                {
                    Timber.e("\t\t Sign-in failed. [2]");
                    finish();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Starts the sign-in process and initializes the Drive client.
     */
    //@AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    protected void signIn()
    {
        Set<Scope> requiredScopes = new HashSet<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);


        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes))
        {
            initializeDriveClient(signInAccount);
        }
        else
        {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .requestIdToken(getString(R.string.API_WEB_CLIENT_ID))                  // TEST
                            .requestServerAuthCode(getString(R.string.API_WEB_CLIENT_ID))           // TEST
                            .build();

            GoogleSignInClient googleSignInClient =
                    GoogleSignIn.getClient(this, signInOptions);

            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);

//mCredential.setSelectedAccountName(signInAccount.getEmail());
//Timber.e("\t\t -signIn-> Signing with chooser of account: " + signInAccount.getEmail());
            //932805877006-s0pm7ddv3hps2r28i3fb6td37efof7lg.apps.googleusercontent.com

        }
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    private void initializeDriveClient(GoogleSignInAccount signInAccount)
    {
        mDriveClient = Drive.getDriveClient(getApplicationContext(), signInAccount);
        mDriveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), signInAccount);
        onDriveClientReady();

        //----------------------------------------------------------------
        //mCredential.setSelectedAccountName(signInAccount.getEmail());
        Timber.e("\t\t -initializeDriveClient-> Signing with account: " + mCredential.getSelectedAccountName());
        //----------------------------------------------------------------
        return;
    }
    /**
     * Shows a toast message.
     */
    protected void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Function: getGoogleApiClient
     * TODO: may be needed
     * @return:
     */
    @Override
    public GoogleApiClient getGoogleApiClient()
    {
        return mGoogleApiClient;
    }

    private void connectAPIClient()
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        isAPIConnected = false;
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // Called typically when the app is not yet authorized, and authorization dialog is displayed to the user.
        try
        {
            result.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
        }
        catch (IntentSender.SendIntentException e)
        {
            Timber.e("Exception while starting resolution activity. " + e.getMessage());
        }
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        Timber.i("* API client connected !!!.");
        isAPIConnected = true;
        //test();

    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        Timber.e("GoogleApiClient connection suspended.");
    }

    //----------------------------------------------------------------------------------------------

    public Context getThis() { return this; }

    //----------------------------------------------------------------------------------------------


    /**
     * Called after the user has signed in and the Drive client has been initialized.
     */
    protected abstract void onDriveClientReady();

    protected DriveClient getDriveClient()
    {
        return mDriveClient;
    }

    @Override
    public DriveResourceClient getDriveResourceClient()
    {
        return mDriveResourceClient;
    }

    public TaskCompletionSource<DriveId> getItemTaskSource()
    {
        return mOpenItemTaskSource;
    }

    //============================================================================================


    /**
     * Prompts the user to select a folder using OpenFileActivity.
     *
     * @param openOptions Filter that should be applied to the selection
     * @return Task that resolves with the selected item's ID.
     */
    @Override
    public Task<DriveId> pickItem(OpenFileActivityOptions openOptions)
    {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        getDriveClient()
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith(new Continuation<IntentSender, Void>()
                {
                    @Override
                    public Void then(@NonNull Task<IntentSender> task) throws Exception {
                        startIntentSenderForResult(
                                task.getResult(), REQUEST_CODE_OPEN_ITEM,
                                null, 0, 0, 0);
                        return null;
                    }
                });
        return mOpenItemTaskSource.getTask();
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------



    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
}

//    /**
//     * Check that Google Play services APK is installed and up to date.
//     * @return true if Google Play Services is available and up to
//     *     date on this device; false otherwise.
//     */
//    private boolean isGooglePlayServicesAvailable() {
//        GoogleApiAvailability apiAvailability =
//                GoogleApiAvailability.getInstance();
//        final int connectionStatusCode =
//                apiAvailability.isGooglePlayServicesAvailable(this);
//        return connectionStatusCode == ConnectionResult.SUCCESS;
//    }
//
//    /**
//     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
//     * Play Services installation via a user dialog, if possible.
//     */
//    private void acquireGooglePlayServices() {
//        GoogleApiAvailability apiAvailability =
//                GoogleApiAvailability.getInstance();
//        final int connectionStatusCode =
//                apiAvailability.isGooglePlayServicesAvailable(this);
//        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
//            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
//        }
//    }