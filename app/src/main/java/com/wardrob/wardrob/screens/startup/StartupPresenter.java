package com.wardrob.wardrob.screens.startup;

import android.app.Activity;
import android.content.Intent;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.core.TakeImageHelper;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.UserObject;

import java.io.File;
import java.util.List;

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


}
