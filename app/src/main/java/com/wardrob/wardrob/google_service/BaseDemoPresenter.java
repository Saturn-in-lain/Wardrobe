package com.wardrob.wardrob.google_service;


import com.wardrob.wardrob.database.AppDatabase;

public class BaseDemoPresenter
{
    BaseDemoView view;
    private AppDatabase db = null;

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
    public void saveDataBaseFileToGoogleDrive()
    {
        String databaseFile = this.db.getDataBasePath();
    }

    /**
     * Function: retriveDataBaseFileFromGoogleDrive
     * @param
     * @return
     */
    public void retriveDataBaseFileFromGoogleDrive()
    {

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
     * Function: saveFileOnGoogleDrive
     * @param
     * @return
     */
    public void saveFileOnGoogleDrive()
    {

    }


    /**
     * Function: deleteFileFromGoogleDrive
     * @param
     * @return
     */
    public void deleteFileFromGoogleDrive()
    {

    }


}
