package com.wardrob.wardrob.fragment_look;

import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.LookObject;

public class FragmentLookItemsPresenter
{
    private FragmentLookItems view;
    private AppDatabase db = null;

    public FragmentLookItemsPresenter(FragmentLookItems fragmentLookItems)
    {
        this.view = fragmentLookItems;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }


    /**
     * Function: loadLookObjects
     * Description:
     * @return
     */
    public void loadLookObjects()
    {

    }


    /**
     * Function: deleteLookFromDatabase
     * Description:
     * @param item
     * @return
     */
    private void deleteLookFromDatabase(LookObject item)
    {
        this.db.lookDao().delete(item);
    }
}
