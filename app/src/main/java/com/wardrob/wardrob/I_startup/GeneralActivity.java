package com.wardrob.wardrob.I_startup;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.core.TakeImageHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class GeneralActivity extends AppCompatActivity
{

    final private int REQUEST_CODE_ASK_PERMISSIONS          = 123;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    /**
     * @Class: BaseReport Constructor
     */
    public GeneralActivity()
    {}

    /**
     * Function: showMessageOKCancel
     * @Desciption:
     * @param message String
     * @param okListener Lisener to response
     * @param activity Activity to show
     */
    private static void showMessageOKCancel(String message,
                                            DialogInterface.OnClickListener okListener,
                                            Activity activity)
    {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(ResourcesGetterSingleton.getStr(R.string.persmission_ok), okListener)
                .setNegativeButton(ResourcesGetterSingleton.getStr(R.string.persmission_cancel), null)
                .create()
                .show();
    }

    /**
     * Function: addPermission
     * @Desciption:
     * @param permission String
     * @param permissionsList List<String>
     * @param ctx Context for  ActivityCompat.checkSelfPermission < SDK 23 support
     */
    private boolean addPermission(List<String> permissionsList, String permission, Context ctx)
    {
        if (ActivityCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(permission))  // Check for Rationale Option
                    return false;
            }
        }
        return true;
    }

    /**
     * Function: checkPermissionsForApplication
     * @Desciption: Check permissions for applications.
     * isCameraNeeded == True in case user want to attach photo
     * isCameraNeeded == False in case user want to download resource file from server
     * @param activity Activity
     */
    public void checkPermissionsForApplication(final Activity activity, boolean isCameraNeeded)
    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if(isCameraNeeded) {
            if (!addPermission(permissionsList, Manifest.permission.CAMERA, activity))
                permissionsNeeded.add(ResourcesGetterSingleton.getStr(R.string.persmission_camera));
        }
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE, activity))
            permissionsNeeded.add(ResourcesGetterSingleton.getStr(R.string.persmission_read_external_storage));
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE, activity))
            permissionsNeeded.add(ResourcesGetterSingleton.getStr(R.string.persmission_write_external_storage));


        if (permissionsList.size() > 0)
        {
            if (permissionsNeeded.size() > 0)
            {
                String message = ResourcesGetterSingleton.getStr(R.string.persmission_message_granting) + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        },activity);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }


    /**
     * Function: onRequestPermissionsResult
     * @Desciption: override function
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults != null && grantResults.length>0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        // Permission Granted
                        Timber.d("\n F:[onRequestPermissionsResult] Permission granted \n");
                    }
                    else
                    {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        Timber.e("\n F:[onRequestPermissionsResult] Permission Denied \n");
                    }
                }
                break;

            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:

                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.CAMERA,                 PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE,  PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    Timber.d("\n F:[onRequestPermissionsResult] Permission granted \n");
                }
                else
                {
                    Timber.d("\n F:[onRequestPermissionsResult] Permission denied \n");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Function: closeKeyboard
     * @Desciption: close UI keyboard for activities.
     */
    public void closeKeyboard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * Function: onResponseActions
     * @Description: Perform most onActivityResult functionality in reports in general way.
     * @param requestCode - onActivityResult requestCode code redirection
     * @param resultCode - onActivityResult resultCode code redirection
     * @param data - Intent information for correct detection of  onActivityResult incoming data
     * @param imageHelper - class for image
     * @param activity - current activity
     */
    public void onResponseActions(int requestCode,
                                  int resultCode,
                                  Intent data,
                                  final TakeImageHelper imageHelper,
                                  Activity activity,
                                  final ImageView image_view)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == TakeImageHelper.REQUEST_IMAGE_SELECT)
            {
                final boolean isCamera;
                if (data == null || data.getData() == null)
                {
                    isCamera = true;
                }
                else
                {
                    final String action = data.getAction();
                    if (action == null)
                    {
                        isCamera = false;
                    }
                    else
                    {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;

                if (isCamera)
                {
                    try
                    {
                        byte[] imgData = FileManagement.getImageFileInByteArray(imageHelper.destination.getPath());
                        Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
                        image_view.setImageBitmap(imageBitmap);
                    }
                    catch (NullPointerException e){Timber.e(e.toString());}
                }
                else // -------------------------------- GALLERY -----------------------------------------------------------------------------------
                {
                    selectedImageUri = data == null ? null : data.getData();
                    Timber.d("selectedImageUri: %s", getPath(activity.getBaseContext(), selectedImageUri));

                    String filePath = getPath(activity.getBaseContext(), selectedImageUri);
                    //final String fileName = new File(filePath).getName();
                    File imgFile = new  File(filePath);


                    long length = imgFile.length();
                    byte[] imgData = new byte[(int) length];

                    FileInputStream pdata = null;
                    try {pdata = new FileInputStream(imgFile);}
                    catch (FileNotFoundException e1){e1.printStackTrace();}

                    try {pdata.read(imgData);}
                    catch (IOException e) {e.printStackTrace();}

                    if(imgFile.exists())
                    {
                        final Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        image_view.setImageBitmap(imageBitmap);

                        FileManagement.createFile(imageHelper.destination.getPath(), imgData);
                        File file = new File(imageHelper.destination.getPath());
                        imageHelper.destination = file;
                    }
                    else
                    {
                        Timber.e("Cant not create file: %s", getPath(activity.getBaseContext(), selectedImageUri));
                    }
                }
            }
        }
    }


    /**
     * Function: humanReadableByteCount
     * @Description:
     * @param bytes - amount of bytes in raw
     * @param si - formater swich wheater true means that 1 Kb equals 1000 byte,
     *              false means that 1 Kb equals 1024 byte
     */
    public static String humanReadableByteCount(long bytes, boolean si)
    {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Function: getPath
     * @Description: Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     * @param context The context.
     * @param uri The Uri to query.
     */
    public static String getPath(final Context context, final Uri uri)
    {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    /**
     * Function: getDataColumn
     * @Description: Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs)
    {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * Function: isExternalStorageDocument
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * Function: isDownloadsDocument
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * Function: isMediaDocument
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
