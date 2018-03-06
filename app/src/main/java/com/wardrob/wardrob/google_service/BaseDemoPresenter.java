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
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
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
import com.google.api.services.drive.model.FileList;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.model.File;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

public class BaseDemoPresenter
{
    BaseDemoView view;
    private AppDatabase db                          = null;
    private DriveFolder wardrobeFolder              = null;
    private HashMap<String, DriveId> listOFFolders  = null;


    public BaseDemoPresenter(BaseDemoView v)
    {
        this.view = v;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
        this.listOFFolders = new HashMap<>();
    }

    /**
     * Function: createFoldersForBackupOnGoogleDrive
     * @param
     * @return
     */
    public void createFoldersForBackupOnGoogleDrive()
        {
            //          <-- Init folders names for creation/ saving drive_id -->
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_users),        null);
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_upper_outfit), null);
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_lower_outfit), null);
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_outerwear),    null);
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_hats),         null);
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_shoes),        null);
            this.listOFFolders.put(ResourcesGetterSingleton.getStr(R.string.path_accessories),  null);

//            CheckFolderOnGoogleDrive(ResourcesGetterSingleton.getStr(R.string.path_wardrobe));
//            for ( String folderName : this.listOFFolders.keySet() )
//            {
//                CheckFolderOnGoogleDrive(folderName);
//            }

            CheckFolderOnGoogleDrive(ResourcesGetterSingleton.getStr(R.string.path_wardrobe));

            createMainFolderOnGoogleDrive(ResourcesGetterSingleton.getStr(R.string.path_wardrobe));
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
     * @param out
     * @return
     */
    private void copyInputStreamToFile( OutputStream out )
    {
        java.io.File file = new java.io.File(db.getRoomDBPath());
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
            Timber.e("Exception while copy: " + e.toString());
        }

        try (Writer writer = new OutputStreamWriter(out))
        {
            writer.write(text.toString());
        }
        catch (IOException e){e.printStackTrace();}
    }


    /**
     * Function: saveFileOnGoogleDrive
     * @param
     * @return
     * @Note: HowTo save to some folder https://developers.google.com/drive/android/create-file
     */
    public void saveFileOnGoogleDrive(final String filename)
    {

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
                        copyInputStreamToFile(outputStream);
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



    private void createFolderInFolder(final DriveFolder parent, final String folderName)
    {
        //Timber.e("\t\t\t Create createFolderInFolder " + folderName);

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(folderName)
                .setMimeType(DriveFolder.MIME_TYPE)
                .setStarred(true)
                .build();

        view.getDriveResourceClient()
                .createFolder(parent, changeSet)
                .addOnSuccessListener((Activity) view.getThis(),
                        new OnSuccessListener<DriveFolder>() {
                            @Override
                            public void onSuccess(DriveFolder driveFolder)
                            {
                                Timber.d("\t[createFolderInFolder]\t\tFolder created:" + folderName);
                                Timber.d("\t[createFolderInFolder]\t\tFolder driveFolder.getDriveId:" + driveFolder.getDriveId().toString());
                                listOFFolders.put(folderName, driveFolder.getDriveId());
                            }
                        })
                .addOnFailureListener((Activity) view.getThis(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Timber.e("\t[createFolderInFolder]\t\tFolder WAS NOT created:" + folderName);
                    }
                });
    }

    /**
     * Function: createFolderOnGoogleDrive
     * @param folderName String
     * @return
     */
    public void createMainFolderOnGoogleDrive(final String folderName)
    {
        final String finalFolderName = folderName;

        Timber.e("\t\t\t Create first folders!");
        view.getDriveResourceClient()
                .getRootFolder()
                .continueWithTask(new Continuation<DriveFolder, Task<DriveFolder>>()
                {
                    @Override
                    public Task<DriveFolder> then(@NonNull Task<DriveFolder> task)
                            throws Exception
                    {
                        DriveFolder parentFolder = task.getResult();
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(finalFolderName)
                                .setMimeType(DriveFolder.MIME_TYPE)
                                .setStarred(true)
                                .build();

                        return view.getDriveResourceClient().createFolder(parentFolder, changeSet);
                    }
                })
                .addOnSuccessListener((Activity) view.getThis(),
                        new OnSuccessListener<DriveFolder>()
                        {
                            @Override
                            public void onSuccess(DriveFolder driveFolder)
                            {
                                if (finalFolderName.equals(ResourcesGetterSingleton.getStr(R.string.path_wardrobe)))
                                {
                                    wardrobeFolder = driveFolder;
                                }
                                //===========================================================
                                for ( String folderName : listOFFolders.keySet() )
                                {
                                    createFolderInFolder(driveFolder, folderName);
                                }
                                listOFFolders.put(finalFolderName, driveFolder.getDriveId());

                                //===========================================================
                                saveFileOnGoogleDrive(db.database+".db");
                                //===========================================================
                                retriveDataBaseFileFromGoogleDrive();
                                //===========================================================

                            }
                        })
                .addOnFailureListener((Activity) view.getThis(), new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Timber.e("\t\t\t Unable to create folder: " + finalFolderName, e);
                    }
                });

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //// https://medium.com/@AndroidAdvance/upload-files-to-google-drive-with-android-api-how-to-copy-paste-pls-thx-c512c98f325f
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function: CheckFolderOnGoogleDrive
     * @param folderName String
     * @return
     */
    public void CheckFolderOnGoogleDrive(final String folderName)
    {

        Timber.d("\t\tCheckFolderOnGoogleDrive: " + folderName + "\n");

        Query query =
                new Query.Builder().addFilter(Filters.and(Filters.eq(SearchableField.TITLE, folderName),
                                              Filters.eq(SearchableField.TRASHED, false))).build();

        Drive.DriveApi.query(view.getGoogleApiClient(), query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>()
        {
            @Override public void onResult(DriveApi.MetadataBufferResult result)
            {
                if (!result.getStatus().isSuccess())
                {
                    Timber.e( "Cannot create folder in the root.");
                }
                else
                {
                    boolean isFound = false;

                    /////////////////////////////////////////////////////////////////////////////////
                    // TEMP - Test debug information RAT
//                    MetadataBuffer metadata = result.getMetadataBuffer();
//                    Timber.d("-----------------------------------------------------------");
//                    for (int i = 0; i < metadata.getCount(); i++)
//                    {
//                        Timber.d("\t\tfolder id:", metadata.get(i).getDriveId().toString());
//                        Timber.d("\t\tfolder name:", metadata.get(i).getTitle().toString());
//                    }
//                    Timber.d("-----------------------------------------------------------");
                    /////////////////////////////////////////////////////////////////////////////////

                    for (Metadata m : result.getMetadataBuffer())
                    {

                        if (!m.isTrashed())
                        {
                            if (m.getTitle().equals(folderName))
                            {
                                isFound = true;
                                DriveId driveId = m.getDriveId();

                                Timber.e("Folder exists: " + m.getTitle() + " vs " + folderName +
                                        " --> driveId: " + driveId.toString());
                                Timber.e("Files size: " + m.getFileSize());

//listOFFolders.put(folderName,driveId);
                                break;
                            }
                        }
                    }
                    if (!isFound)
                    {
//createFolderOnGoogleDrive(folderName);
                    }
                    //TODO: IS THIS NEEDED?
//result.getMetadataBuffer().release();
                }
            }
        });
    }
    //============================================================================================//
    //============================================================================================//

//    private static void printFile(Drive service, String fileId)
//    {
//        try
//        {
//            File file = service.files().get(fileId).execute();
//
//            System.out.println("Title: "        + file.getTitle());
//            System.out.println("Description: "  + file.getDescription());
//            System.out.println("MIME type: "    + file.getMimeType());
//        }
//        catch (IOException e) { System.out.println("An error occurred: " + e); }
//    }

    /**
     * Download a file's content.
     *
     * @param service Drive API service instance.
     * @param file Drive File instance.
     * @return InputStream containing the file's content if successful,
     *         {@code null} otherwise.
     */
//    private static InputStream downloadFile(Drive service, File file)
// {
//        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0)
// {
//            try
//              {
//                HttpResponse resp =
//                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
//                                .execute();
//                return resp.getContent();
//            }
//            catch (IOException e)     // An error occurred.
//            {
//                e.printStackTrace();
//                return null;
//            }
//        } else {
//            // The file doesn't have any content stored on Drive.
//            return null;
//        }
//    }

    //============================================================================================//
    //============================================================================================//

    /**
     * Function: retriveDataBaseFileFromGoogleDrive
     * @param
     * @return
     */
    public void retriveDataBaseFileFromGoogleDrive()
    {
//        String file_to_download = db.database+".db";
//
//        DriveFile file = Drive.DriveApi.getFile(view.getGoogleApiClient(), driveId);
//
//
//        Drive service = new Drive();
//
//        DriveResource.MetadataResult mdRslt = file.getMetadata(view.getGoogleApiClient()).await();
//        if (mdRslt != null && mdRslt.getStatus().isSuccess())
//        {
//            String link = mdRslt.getMetadata().getWebContentLink();
//            Timber.d("\t\t\t ---> LINK: ", link);
//        }


//        1wGbJJPaiPcQA_Lea9NpoeR8rQtlNGSV3 -> tst.txt default tes file
//        "defaultOpenWithLink": "https://drive.google.com/file/d/1wGbJJPaiPcQA_Lea9NpoeR8rQtlNGSV3/view?usp=drivesdk",
//        "iconLink": "https://drive-thirdparty.googleusercontent.com/16/type/text/plain",
//        "thumbnailLink": "https://lh3.googleusercontent.com/wODwyNU3-YdXjozTjf8Y1Z1Q9xrTVfZMwuO11IZASVyWGtbqoX9kkU4YjZ4F744giBx7j7gCkRs=s220",
////        "title": "tst.txt",

//        Drive.DriveApi.fetchDriveId(view.getGoogleApiClient(), "1wGbJJPaiPcQA_Lea9NpoeR8rQtlNGSV3")
//                .setResultCallback(idCallback);
    }




    //============================================================================================//
    //============================================================================================//
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
