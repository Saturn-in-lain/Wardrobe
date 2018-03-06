package com.wardrob.wardrob.google_service;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * An asynchronous task that handles the Drive API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class MakeRequestTask extends AsyncTask<Void, Void, List<String>>
{
    private com.google.api.services.drive.Drive mService = null;
    private Exception mLastError = null;

    MakeRequestTask(GoogleAccountCredential credential)
    {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

//        catch (UserRecoverableAuthIOException e)
//        {
//            startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
//        }

        mService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Drive API Android Quickstart")
                .build();

        if (credential.getSelectedAccountName() == null)
        {
            Timber.e("\t\t Credentials are empty!");

        }
    }

    /**
     * Background task to call Drive API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params)
    {
        try
        {
            //retrieveAllFiles(mService);
            return getDataFromApi();

        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of up to 10 file names and IDs.
     * @return List of Strings describing files, or an empty list if no files
     *         found.
     * @throws IOException
     */
    private List<String> getDataFromApi() throws IOException
    {
        // Get a list of up to 10 files.
        List<String> fileInfo = new ArrayList<String>();
        FileList result = mService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();


        if (files != null)
        {
            for (File file : files)
            {
                fileInfo.add(String.format("%s (%s)\n",
                        file.getName(), file.getId()));
            }
        }
        return fileInfo;
    }

    private static List<File> retrieveAllFiles(Drive service) throws IOException
    {
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = service.files().list();

        do {
            try
            {
                FileList files = request.execute();

                result.addAll(files.getFiles());
                request.setPageToken(files.getNextPageToken());
            }
            catch (IOException e)
            {
                Timber.e("\t\t[retrieveAllFiles]An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        return result;
    }

    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected void onPostExecute(List<String> output)
    {

        if (output == null || output.size() == 0)
        {
            Timber.d("\t\tNo results returned.");
        }
        else
        {
            output.add(0, "\t\tData retrieved using the Drive API:");

        }
    }

    @Override
    protected void onCancelled()
    {
        if (mLastError != null)
        {
                Timber.e("\t[onCancelled] tThe following error occurred:\n"+ mLastError.getMessage());
        }
    }
}