package com.wardrob.wardrob.I_startup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.UserObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class StartupPresenter
{

    private StartupView view = null;
    private AppDatabase db   = null;

    public StartupPresenter(StartupView view)
    {
        this.view = view;
        initializeApplicationDataBase();
    }

    /**
     * Function: initStaringProcess
     * Description:
     */
    public void initStaringProcess()
    {
        FileManagement object = new FileManagement();
        object.createFoldersForApplication();
    }

    /**
     * Function: initializeApplicationDataBase
     * Description:
     */
    private void initializeApplicationDataBase()
    {
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }

    /**
     * Function: isUsersPresentInDatabase
     * Description:
     */
    public boolean isUsersPresentInDatabase()
    {
        List<UserObject> users = this.db.userDao().getAll();
        if (0 != users.size()){return true;}
        return false;
    }


    /**
     * Function: setActiveUser
     * Description:
     * @param image_logo
     * @param login_welcome_text
     */
    public void setActiveUser(ImageView image_logo, TextView login_welcome_text)
    {
        UserObject activeUser = this.db.userDao().findActiveUser();

        if(null != activeUser)
        {
            login_welcome_text.setText(
                    ResourcesGetterSingleton.getStr(R.string.welcome_string) + " " +
                            activeUser.getUserName());
            try
            {
                //String pathToFolder = ResourcesGetterSingleton.getStr(R.string.path_main) +
                //        ResourcesGetterSingleton.getStr(R.string.path_users) + "/";
                //byte[] imgData = FileManagement.getImageFileInByteArray(pathToFolder + activeUser.getUserImageName());

                byte[] imgData = FileManagement.getImageFileInByteArray(activeUser.getUserImageName());
                Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);

                image_logo.setImageBitmap(imageBitmap);
            }
            catch (NullPointerException e) {Timber.e(e.toString());}
        }
    }


}
