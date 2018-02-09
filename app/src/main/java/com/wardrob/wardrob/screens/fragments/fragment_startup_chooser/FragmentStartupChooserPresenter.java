package com.wardrob.wardrob.screens.fragments.fragment_startup_chooser;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.UserObject;

import java.io.File;
import java.util.List;

import timber.log.Timber;

public class FragmentStartupChooserPresenter
{
    private final FragmentStartupChooserView view;
    private AppDatabase db   = null;


    public FragmentStartupChooserPresenter(FragmentStartupChooserView v)
    {
        this.view = v;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());

        initializeAvailableUsers();

    }

    /**
     * Function: initializeAvailableUsers
     */
    private void initializeAvailableUsers()
    {
        final List<UserObject> users = this.db.userDao().getAll();

        for(int i=0; i<users.size(); i++)
        {

            final LinearLayout linearLayout = new LinearLayout(this.view.getThis());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ImageView image = new ImageView(this.view.getThis());

            try
            {
                File imgFile = new File(users.get(i).getUserImageName());
                long length = imgFile.length();
                if(length != 0)
                {
                    byte[] imgData = FileManagement.getImageFileInByteArray(users.get(i).getUserImageName());
                    Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
                    image.setImageBitmap(imageBitmap);
                }
                else
                {
                    image.setImageResource(R.drawable.default_logo_1);
                }

                final int id = i;
                image.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(v.getContext(),
                                "Set as active: " + users.get(id).getUserName().toString(),
                                Toast.LENGTH_LONG).show();

                        setUserAsActive(users.get(id));
                    }
                });

                image.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        Toast.makeText(v.getContext(),
                                "Delete: " + users.get(id).getUserName().toString(),
                                Toast.LENGTH_LONG).show();

                        FileManagement.deleteFile(users.get(id).getUserImageName());
                        deleteUserFromDatabase(users.get(id));
                        linearLayout.setVisibility(View.GONE);

                        return true;
                    }
                });

            }
            catch (NullPointerException e) {
                Timber.e(e.toString());}

            layoutParams.setMargins(10, 10, 10, 10);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.addView(image);

            this.view.getAvailableUsersLayout().addView(linearLayout);
        }
    }

    /**
     * Function: setUserAsActive()
     */
    private void setUserAsActive(UserObject user)
    {
        db.userDao().resetAllActive();
        user.setIsActive(1);
        db.userDao().insertAll(user);
    }

    /**
     * Function: deleteUserFromDatabase
     */
    private void deleteUserFromDatabase(UserObject user)
    {
        this.db.userDao().delete(user);
    }
}
