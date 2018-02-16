package com.wardrob.wardrob.screens.new_item;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;


import com.wardrob.wardrob.core.TakeImageHelper;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.ItemObject;
import com.wardrob.wardrob.database.UserObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class NewItemPresenter
{
    private String pathToFolder = null;
    public Calendar newCalendar = Calendar.getInstance();
    public NewItemView view;
    public TakeImageHelper object;

    public AppDatabase db   = null;

    public NewItemPresenter(NewItemView view, String pathToFolder)
    {
        this.view = view;
        this.pathToFolder = pathToFolder;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }


    /**
     * Function: getPhotoIntent
     * @param activity {@link Activity}
     * @return Intent
     */
    public Intent getPhotoIntent(final Activity activity)
    {
        Intent intent;
        object = new TakeImageHelper(this.view.getThis());
        File pictureDirectory = new File(this.pathToFolder);
        intent = object.getChooserIntent(activity, pictureDirectory);
        return intent;
    }

    /**
     * Function: getDateSelection
     * @param ctx
     * @param setDate
     * @return None
     */
    public void getDateSelection(final Context ctx, final EditText setDate)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                newCalendar.set(Calendar.DATE, dayOfMonth);
                newCalendar.set(Calendar.MONTH, monthOfYear);
                newCalendar.set(Calendar.YEAR, year);

                DateFormat targetFormat = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH);
                String NewDate = targetFormat.format(newCalendar.getTime());

                setDate.setText(NewDate);
            }
        },
        newCalendar.get(Calendar.YEAR),
        newCalendar.get(Calendar.MONTH),
        newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * Function: saveItem
     * @param object ItemObject
     */
    public void saveItem(ItemObject object)
    {
        UserObject activeUser = this.db.userDao().findActiveUser();
        object.setUserName(activeUser.getUserName());

        db.itemDao().insertAll(object);
    }

    /**
     * Function: replaceItem
     * @param object ItemObject
     */
    public void replaceItem(ItemObject object)
    {
        UserObject activeUser = this.db.userDao().findActiveUser();
        object.setUserName(activeUser.getUserName());
        db.itemDao().insertAll(object);
    }

    /**
     * Function: getItem
     * @param id
     */
    public ItemObject getItem(Integer id)
    {
        ItemObject object = db.itemDao().findById(id.intValue());
        return object;
    }
}
