package com.wardrob.wardrob.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.provider.MediaStore;

import com.wardrob.wardrob.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import timber.log.Timber;

public class FileManagement
{

    static int maxBufferSize = 1024 * 1024 * 20; // 20 Mbyte

    public FileManagement()
    {

    }

    /**
     * Function: createFoldersForApplication()
     * @Description:
     * @Note
     */
    public boolean createFoldersForApplication()
    {

        Timber.d("\n [createFoldersForApplication] \n");

        ArrayList<String> pathToFolders = new ArrayList<>();

        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_upper_outfit));
        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_lower_outfit));
        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_outerwear));
        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_hats));
        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_shoes));
        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_accessories));

        pathToFolders.add(ResourcesGetterSingleton.getStr(R.string.path_main) +
                                    ResourcesGetterSingleton.getStr(R.string.path_users));


        File myDirectory = new File(ResourcesGetterSingleton.getStr(R.string.path_main));
        if (!myDirectory.exists())
        {
            myDirectory.mkdirs();
            if (myDirectory.mkdirs())
            {
                Timber.d("\n [createFoldersForApplication] -> Directory is created");
            }
        }

        Iterator<String> iterator = pathToFolders.iterator();
        while(iterator.hasNext())
        {
            String path = iterator.next();
            File itemsDirectory = new File(path);
            if (!itemsDirectory.exists())
            {
                itemsDirectory.mkdirs();
                if (!itemsDirectory.mkdirs())
                {
                    Timber.d("\n [createFoldersForApplication] -> Directory is not created");
                }
            }
        }
        return true;
    }



    /**
     * Function: createFile()
     * @Description: Copy file to specific folder
     * @param myDirectory - Path to image where to save
     * @param Image - Image to save
     * @Note
     */
    public static void createFile(String myDirectory, byte[] Image)
    {
        File myFile = new File(myDirectory);
        if (!myFile.exists())
        {
            int count;
            HashMap<String, Object> map = new HashMap<String, Object>();
            try
            {
                //File path = Environment.getExternalStorageDirectory();
                InputStream input = new ByteArrayInputStream(Image);
                myFile = new File(myDirectory);
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(myFile));

                byte data[] = new byte[maxBufferSize];

                while ((count = input.read(data)) != -1)
                {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Function: deleteFile()
     * @Description:
     * @Note
     */
    public static void deleteFile(String path)
    {
        File file = new File(path);
        if(!file.delete())
        {
            Timber.e("File delete error: " + path);
        }
    }

    /**
     * Function: getLastDownloadedFile()
     * @Description:
     * @Note
     */
    public static String getLastDownloadedFile()
    {
        String pathToFolder = ResourcesGetterSingleton.getStr(R.string.path_main) +
                                            ResourcesGetterSingleton.getStr(R.string.path_users);
        File dir = new File(pathToFolder);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0)
        {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile.getName();
    }


    /**
     * Function: getImageFileInByteArray
     * @Description:
     * @param filePath String
     *  TODO: This is same as GeneralActivity stuf.. remove this similarity
     */
    public static byte[] getImageFileInByteArray(String filePath)
    {
        byte[] imgData;
        FileInputStream pdata = null;

        File imgFile = new File(filePath);
        long length = imgFile.length();

        imgData = new byte[(int) length];
        try
        {
            pdata = new FileInputStream(imgFile);
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();

            //----------------------------------------------------------------------
            //TODO: Place here default login image
            Uri path = Uri.parse(ANDROID_RESOURCE + "com.wardrob.wardrob" +
                    FORESLASH + R.drawable.default_logo_1);
            Timber.e("\t\t PATH:" + path.getPath().toString());
            //----------------------------------------------------------------------
        }

        try
        {
            pdata.read(imgData);
        }
        catch (IOException e) { e.printStackTrace();}

        return imgData;
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    /**
     * Function: resizeImageForThumbnail
     * @Description: resize and return bitmap image for screen representation.
     * @param imgData Raw image in byte array
     */
    public static Bitmap resizeImageForThumbnail(byte[] imgData) //String mCurrentPhotoPath
    {
        int THUMBNAIL_HEIGHT = 150;

        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
        Float width  = new Float(imageBitmap.getWidth());
        Float height = new Float(imageBitmap.getHeight());
        Float ratio = width/height;
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
                (int)(THUMBNAIL_HEIGHT*ratio),
                THUMBNAIL_HEIGHT,
                false);
        return  imageBitmap;
    }
}
