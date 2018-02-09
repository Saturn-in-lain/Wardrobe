package com.wardrob.wardrob.screens.fragments.fragment_startup_creater;

import com.wardrob.wardrob.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.core.TakeImageHelper;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.UserObject;

import java.io.File;

public class FragmentStartupCreatePresenter
{
    FragmentStartupCreateView view = null;
    public TakeImageHelper object;

    public FragmentStartupCreatePresenter(FragmentStartupCreateView view)
    {
        this.view = view;
    }


    /**
     * Function: saveNewMemberInDataBase
     * @param avatarName
     * @param gender
     */
    public void saveNewMemberInDataBase(String avatarName, String gender)
    {
        if(null != avatarName && null != gender)
        {
            if( (avatarName.length() > 0) && (gender.length() > 0))
            {
                String avatar_file = object.destination.getPath();

                if (null == avatar_file)
                {
                    Toast.makeText(view.getThis(), "Choose default avatar here?!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(view.getThis(), "All information is correct", Toast.LENGTH_LONG).show();
                    addUserInSystem(avatarName,gender,avatar_file);

                    this.view.closeActivity();
                }
            }
        }
        else
        {
            setAlarmDialog();
        }
    }


    /**
     * Function: createNewImage
     * @param activity
     */
    public Intent createNewImage(final Activity activity)
    {
        Intent intent ;
        object = new TakeImageHelper(this.view.getThis());

        String pathToFolder = ResourcesGetterSingleton.getStr(R.string.path_main) +
                ResourcesGetterSingleton.getStr(R.string.path_users);
        File pictureDirectory = new File(pathToFolder);
        intent = object.getChooserIntent(activity, pictureDirectory);
        return intent;
    }

    /**
     * Function: addUserInSystem
     * @param avatarName
     * @param avatarGender
     * @param imagePath
     *
     */
    private void addUserInSystem(String avatarName, String avatarGender, String imagePath)
    {
        AppDatabase db = AppDatabase.getAppDatabase(view.getThis());

        db.userDao().resetAllActive();

        UserObject user = new UserObject();
        user.setUserName(avatarName);
        user.setUserGender(avatarGender);
        user.setUserImageName(imagePath);
        user.setIsActive(1);
        db.userDao().insertAll(user);
    }

    /**
     * Function: setAlarmDialog
     */
    public void setAlarmDialog()
    {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this.view.getThis());
        String message       = ResourcesGetterSingleton.getStr(R.string.warning_message);
        String button1String = "OK";
        alertLogout.setMessage(message);
        alertLogout.setIcon(android.R.drawable.ic_dialog_alert);
        alertLogout.setPositiveButton(button1String, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int arg1)
            {
                // TODO Do on click something
            }
        });
        alertLogout.setCancelable(true);
        alertLogout.show();
    }
}
