package com.wardrob.wardrob.screens.new_look;

import android.content.Intent;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.ItemObject;
import com.wardrob.wardrob.database.LookObject;
import com.wardrob.wardrob.screens.listscreen.ItemsListActivity;

import java.util.HashMap;

public class NewLookPresenter
{
    public NewLookView view;
    public AppDatabase db = null;

    public NewLookPresenter(NewLookView view)
    {
        this.view = view;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }

    /**
     * Function: callCorrectItemsList
     * @param category_name
     */
    public void callCorrectItemsList(String category_name)
    {
        Intent intent = new Intent(view.getThis(), ItemsListActivity.class);
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item_index),category_name);
        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_state),
                    ResourcesGetterSingleton.getStr(R.string.state_pick));
        intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
        view.getThis().startActivity(intent);
    }

    /**
     * Function: getLookItem
     * @param id
     */
    public LookObject getLookItem(Integer id)
    {
        LookObject object = db.lookDao().findById(id.intValue());
        return object;
    }
}
