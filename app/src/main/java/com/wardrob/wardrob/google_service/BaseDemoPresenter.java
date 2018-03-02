package com.wardrob.wardrob.google_service;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

import timber.log.Timber;

public class BaseDemoPresenter
{
    BaseDemoView view;
    private AppDatabase db = null;

    private DriveFolder wardrobeFolder = null;

    public BaseDemoPresenter(BaseDemoView v)
    {
        this.view = v;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }

    /**
     * Function: saveDataBaseFileToGoogleDrive
     * @param
     * @return
     */
    public void saveDataBaseFileToGoogleDrive(URI fileName)
    {

    }

    /**
     * Function: retriveDataBaseFileFromGoogleDrive
     * @param
     * @return
     */
    public void retriveDataBaseFileFromGoogleDrive()
    {


//        DriveApi.MetadataBufferResult metadataBufferResult = Drive.DriveApi.getAppFolder(view.getDriveResourceClient())
//                .listChildren(view.getDriveResourceClient())
//                .await();
//
//        if (!metadataBufferResult.getStatus().isSuccess())
//        {
//            metadataBufferResult.release();
//            Timber.d("MetadataBufferResult failure");
//            return;
//        }
//
//        MetadataBuffer metadataBuffer = metadataBufferResult.getMetadataBuffer();
//
//        if (metadataBuffer.getCount() == 0)
//        {
//            metadataBuffer.release();
//            return;
//        }
//        DriveId driveId = metadataBuffer.get(0).getDriveId();
//        metadataBuffer.release();
//        metadataBufferResult.release();
//        driveId.asDriveFile(); // <--

    }

    /**
     * Function: getListOfFilesForBackUp
     *  Retrive database file from cloud and if files have difference, starting to prepare list
     *  of files that should be [deleted] or [added]
     * @param
     * @return
     */
    public void getListOfFilesForBackup()
    {

    }

    /**
     * Function: copyInputStreamToFile
     * @param in
     * @param out
     * @return
     */
    private void copyInputStreamToFile( InputStream in, OutputStream out ) {
        try
        {
            byte[] buf = new byte[in.available()];
            int len;
            while((len=in.read(buf))>0){ out.write(buf,0,len);}
            out.close();
            in.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }


    /**
     * Function: saveFileOnGoogleDrive
     * @param
     * @return
     * @Note: HowTo save to some folder https://developers.google.com/drive/android/create-file
     */
    public void saveFileOnGoogleDrive()
    {
        final String filename = db.database+".db";


        final Task<DriveFolder> rootFolderTask = view.getDriveResourceClient().getRootFolder();
        final Task<DriveContents> createContentsTask = view.getDriveResourceClient().createContents();

        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(new Continuation<Void, Task<DriveFile>>()
                {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<Void> task) throws Exception
                    {
                        DriveFolder parent;

                        if(null != wardrobeFolder) { parent = wardrobeFolder; }
                        else
                        {
                            parent = rootFolderTask.getResult();
                            Timber.e("\t\t We have issue with correct folder!");
                        }

                        DriveContents contents = createContentsTask.getResult();

                        //---------------------------------------------------------------------
                        //Save data to file - TODO: Separate method
                        OutputStream outputStream = contents.getOutputStream();

                        File file = new File(db.getRoomDBPath());
                        StringBuilder text = new StringBuilder();
                        try
                        {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null)
                            {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();
                        }
                        catch (IOException e)
                        {
                         Timber.e("Exception while copy: "+e.toString());
                        }

                        //---------------------------------------------------------------------
                        try (Writer writer = new OutputStreamWriter(outputStream))
                        {
                            writer.write(text.toString());
                        }
                        //---------------------------------------------------------------------

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(filename)
                                .setMimeType("text/plain")
                                .setStarred(true)
                                .build();

                        return view.getDriveResourceClient().createFile(parent,
                                                                        changeSet,
                                                                        contents);
                    }
                })
                .addOnSuccessListener((Activity) view.getThis(),
                        new OnSuccessListener<DriveFile>()
                        {
                            @Override
                            public void onSuccess(DriveFile driveFile)
                            {
                                Timber.d("File was created!");

                            }
                        })
                .addOnFailureListener((Activity) view.getThis(), new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Timber.e("Unable to create file", e);
                    }
                });
    }


    /**
     * Function: deleteFileFromGoogleDrive
     * @param file DriveFile {@link DriveFile}
     * @return
     */
    public void deleteFileFromGoogleDrive(DriveFile file)
    {
        view.getDriveResourceClient()
                .delete(file)
                .addOnSuccessListener((Activity) view.getThis(),
                        new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Timber.d("File deleted successfully");
                            }
                        })
                .addOnFailureListener((Activity) view.getThis(), new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Timber.e("Unable to delete file", e);
                    }
                });
    }

    /**
     * Function: createFolderOnGoogleDrive
     * @param folderName String
     * @return
     */
    public void createFolderOnGoogleDrive(String folderName)
    {
        if(null == folderName)
            folderName = ResourcesGetterSingleton.getStr(R.string.google_folder);
        final String finalFolderName = folderName;

        view.getDriveResourceClient()
                .getRootFolder()
                .continueWithTask(new Continuation<DriveFolder, Task<DriveFolder>>()
                {
                    @Override
                    public Task<DriveFolder> then(@NonNull Task<DriveFolder> task)
                            throws Exception {
                        DriveFolder parentFolder = task.getResult();
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(finalFolderName)
                                .setMimeType(DriveFolder.MIME_TYPE)
                                .setStarred(true)
                                .build();

                        return  view.getDriveResourceClient().createFolder(parentFolder, changeSet);
                    }
                })
                .addOnSuccessListener((Activity) view.getThis(),
                        new OnSuccessListener<DriveFolder>()
                        {
                            @Override
                            public void onSuccess(DriveFolder driveFolder)
                            {
                                wardrobeFolder = driveFolder;
                                Timber.d("Folder creation: "+
                                        driveFolder.getDriveId().encodeToString());

                                // TODO: Save file only here
                                saveFileOnGoogleDrive();
                            }
                        })
                .addOnFailureListener((Activity) view.getThis(), new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Timber.e("Unable to create folder", e);
                    }
                });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //// https://medium.com/@AndroidAdvance/upload-files-to-google-drive-with-android-api-how-to-copy-paste-pls-thx-c512c98f325f
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void check_folder_exists(final String folderName)
    {
        Query query =
                new Query.Builder().addFilter(Filters.and(Filters.eq(SearchableField.TITLE, folderName),
                                              Filters.eq(SearchableField.TRASHED, false))).build();

        Drive.DriveApi.query(view.getGoogleApiClient(), query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>()
        {
            @Override public void onResult(DriveApi.MetadataBufferResult result) {
                if (!result.getStatus().isSuccess())
                {
                    Timber.e( "Cannot create folder in the root.");
                } else {
                    boolean isFound = false;
                    for (Metadata m : result.getMetadataBuffer())
                    {
                        if (m.getTitle().equals(folderName))
                        {
                            Timber.e("Folder exists");
                            isFound = true;
                            DriveId driveId = m.getDriveId();
                            break;
                        }
                    }
                    if (!isFound)
                    {
                        Timber.i( "Folder not found; creating it.");
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(folderName).build();
                        Drive.DriveApi.getRootFolder(view.getGoogleApiClient())
                                .createFolder(view.getGoogleApiClient(), changeSet)
                                .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>()
                                {
                                    @Override public void onResult(DriveFolder.DriveFolderResult result)
                                    {
                                        if (!result.getStatus().isSuccess())
                                        {
                                            Timber.e( "U AR A MORON! Error while trying to create the folder");
                                        }
                                        else
                                            {
                                            Timber.i( "Created a folder");
                                            DriveId driveId = result.getDriveFolder().getDriveId();

                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
    //============================================================================================//
    //============================================================================================//
    /**
     * Prompts the user to select a text file using OpenFileActivity.
     *
     * @return Task that resolves with the selected item's ID.
     */
    protected Task<DriveId> pickTextFile()
    {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                        .setActivityTitle(ResourcesGetterSingleton.getStr(R.string.select_file))
                        .build();
        return view.pickItem(openOptions);
    }

    /**
     * Prompts the user to select a folder using OpenFileActivity.
     *
     * @return Task that resolves with the selected item's ID.
     */
    protected Task<DriveId> pickFolder()
    {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(
                                Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE))
                        .setActivityTitle(ResourcesGetterSingleton.getStr(R.string.select_folder))
                        .build();
        return view.pickItem(openOptions);
    }
    //============================================================================================//
    //============================================================================================//
}
