package com.wardrob.wardrob.core;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.LinearLayout;

import com.wardrob.wardrob.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TakeImageHelper
{
    public final static int REQUEST_IMAGE_SELECT  = 2;
    public Intent chooserIntent  = null;
    public Context ctx            = null;

    public File     destination   = null;
    public Uri      pictureUri    = null;
    public String   file_name     = null;

    public String   default_user_folder = null;
    public String   default_folder = null;

    public TakeImageHelper(Context ctx)
    {
        this.ctx = ctx;

        default_user_folder = ResourcesGetterSingleton.getStr(R.string.path_main)  +
                              ResourcesGetterSingleton.getStr(R.string.path_users) + "/";

        default_folder = ResourcesGetterSingleton.getStr(R.string.path_main);
    }

    /**
     * Function: getChooserIntent()
     * @param activity Activity deprecated param?
     * Note: http://stackoverflow.com/questions/4455558/allow-user-to-select-camera-or-gallery-for-image/12347567#12347567
     */
    public Intent getChooserIntent(Activity activity, File pictureDirectory)
    {
        file_name = "IMG_WRDRB_" + System.currentTimeMillis() + "_picture.jpg";

        destination = new File(pictureDirectory, file_name);
        pictureUri = Uri.fromFile(destination);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        final PackageManager packageManager = this.ctx.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        for (ResolveInfo res : listCam)
        {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            cameraIntents.add(intent);
        }
        // Gallery choose.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        chooserIntent = Intent.createChooser(galleryIntent, "Select Source");                           // Chooser of filesystem options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));                           // Add the camera options.
        return chooserIntent;
    }

    /**
     * Function: setImageOnLayout()
     * Description: Represent image on given layout.
     * @param layout LinearLayout - layout where to put image
     * @param imageBitmap - Actually image to show
     */
    public void setImageOnLayout(LinearLayout layout,
                                 Bitmap imageBitmap)
    {
        layout.setOrientation(LinearLayout.VERTICAL);
        //layout.addView(getAttachedView(imageBitmap, fileName, size, uploadId));
    }


}
