package com.wardrob.wardrob.google_service;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    public final static int LOAD_DB_FILE_FROM = 0;
    public final static int OTHERS = 0;

    private int chooseTypeOFRequest;

    MakeRequestTask(GoogleAccountCredential credential, int typeOfRequest)
    {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        chooseTypeOFRequest = typeOfRequest;

        mService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(ResourcesGetterSingleton.getStr(R.string.path_wardrobe))
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
            if (chooseTypeOFRequest == OTHERS)
            {
                return getDataFromApi();
            }
            else
            {
                Timber.e("\t[doInBackground]\tUnknown chooseTypeOFRequest");
            }

        }
        catch (Exception e)
        {
            mLastError = e;
            cancel(true);
            return null;
        }
        return null;
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

                Timber.d("\t[%s]\t[%s]\t",file.getName(), file.getId());
            }
        }
        return fileInfo;
    }

    //==================================================================================
    private List<File> retrieveAllFiles(Drive service) throws IOException
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
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        }
        while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        return result;
    }

    //==================================================================================
    private void downloadFileFromGoogleDrive(String fileId)
    {
        OutputStream outputStream = new ByteArrayOutputStream();
        try
        {
            mService.files().get(fileId).executeMediaAndDownloadTo(outputStream);

//            mService.files().export(fileId, this.getMimeType("Plain text"))
//                                            .executeMediaAndDownloadTo(outputStream);
        }
        catch (IOException e) {e.printStackTrace();}
    }

    //==================================================================================

    private void createFolder(String folderName)
    {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = null;
        try
        {
            file = mService.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
        }
        catch (IOException e) { e.printStackTrace(); }

        Timber.d("Folder ID: " + file.getId());
    }

    //TODO Test
    private void insertFile(String pathName) //"files/photo.jpg"
    {
        String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        fileMetadata.setParents(Collections.singletonList(folderId));

        java.io.File filePath = new java.io.File(pathName);
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = null;
        try
        {
            file = mService.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File ID: " + file.getId());
    }
    //==================================================================================

    private String getMimeType(String type)
    {
        String returnVal = null;

        switch(type)
        {
            case "HTML":
                returnVal = "text/html";
                break;
            case "HTML (zipped)":
                returnVal = "application/zip";
                break;
            case "Plain text":
                returnVal = "text/plain";
                break;
            case "Rich text":
                returnVal = "application/rtf";
                break;
            case "Open Office doc":
                returnVal = "application/vnd.oasis.opendocument.text";
                break;
            case "PDF":
                returnVal = "application/pdf\n";
                break;
            case "MS Word document":
                returnVal = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            case "CSV":
                returnVal = "text/csv";
                break;
            case "JPEG":
                returnVal = "image/jpeg";
                break;
            case "PNG":
                returnVal = "image/png";
                break;
            case "JSON":
                returnVal = "application/vnd.google-apps.script+json";
                break;
            default:
                returnVal = "text/plain";
                break;
        }
        return returnVal;
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