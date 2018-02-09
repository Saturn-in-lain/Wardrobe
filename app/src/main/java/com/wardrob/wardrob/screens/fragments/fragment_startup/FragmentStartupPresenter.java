package com.wardrob.wardrob.screens.fragments.fragment_startup;


import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.UserObject;

import timber.log.Timber;

public class FragmentStartupPresenter
{

    FragmentStartupView view;
    private AppDatabase db = null;

    public  FragmentStartupPresenter(FragmentStartupView v)
    {
        this.view = v;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
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
                byte[] imgData = FileManagement.getImageFileInByteArray(activeUser.getUserImageName());
                Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);

                image_logo.setImageBitmap(imageBitmap);
            }
            catch (NullPointerException e) {
                Timber.e(e.toString());}
        }
    }

}
