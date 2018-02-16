package com.wardrob.wardrob.screens.new_look;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.database.ItemObject;
import com.wardrob.wardrob.database.LookObject;
import com.wardrob.wardrob.database.UserObject;

import timber.log.Timber;


public class NewLookActivity extends AppCompatActivity implements NewLookView
{
    NewLookPresenter presenter;


    ImageView imgHat;
    ImageView imgAccessories;
    ImageView imgUpperLevel;
    ImageView imgWarmLevel;
    ImageView imgLowerLevel;
    ImageView imgBoots;
    Button    btnSaveNewLook;
    EditText  edtLookName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_look);

        presenter = new NewLookPresenter(this);

        initUIElements();
    }

    /**
     * Function: onStart
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        initUIElements();
    }


    /**
     * Function: initUIElements
     */
    private void initUIElements()
    {
       imgHat          = (ImageView) findViewById(R.id.imgHat);
       imgAccessories  = (ImageView) findViewById(R.id.imgAccessories);
       imgUpperLevel   = (ImageView) findViewById(R.id.imgUpperLevel );
       imgWarmLevel    = (ImageView) findViewById(R.id.imgWarmLevel  );
       imgLowerLevel   = (ImageView) findViewById(R.id.imgLowerLevel );
       imgBoots        = (ImageView) findViewById(R.id.imgBoots      );

       btnSaveNewLook  = (Button) findViewById(R.id.btnSaveNewLook);
       edtLookName     = (EditText) findViewById(R.id.edtLookName);

        LookObject look = presenter.db.lookDao().findFinished(false);

        setLookItems(look,"Upper Level",    imgUpperLevel);
        setLookItems(look,"Lower level",    imgLowerLevel);
        setLookItems(look,"Warm Clothes",   imgWarmLevel);
        setLookItems(look,"Hats",           imgHat);
        setLookItems(look,"Accessories",    imgAccessories);
        setLookItems(look,"Boots",          imgBoots);

        String name = look.getLookName();
        if (null != name) { edtLookName.setText(name);}
    }

    /**
     * Function: setLookItems
     * @param look
     * @param category
     * @param img
     */
    private void setLookItems( LookObject look, String category, ImageView img )
    {
        int id = 0;

        switch (category)
        {
            case "Upper Level":
                id = look.getLookUpperLevelObject();
                break;
            case "Lower level":
                id = look.getLookLowerLevelObject();
                break;
            case "Warm Clothes":
                id = look.getLookWarmLevelObject();
                break;
            case "Hats":
                id = look.getLookHatObject();
                break;
            case "Accessories":
                id = look.getLookAccessoriesObject();
                break;
            case "Boots":
                id = look.getLookBootsLevelObject();
                break;
            default:
                break;
        }

        if(id !=0)
        {
            ItemObject object = presenter.db.itemDao().findById(id);
            if (null != object)
            {
                Bitmap imageBitmap = BitmapFactory.decodeFile(object.getItemImageName());
                img.setImageBitmap(imageBitmap);
            }
        }
    }


    /**
     * Function:
     * @param v
     */
    public void onHatSelectionClick (View v)
    {
        presenter.callCorrectItemsList(getString(R.string.menu_hats));
    }

    /**
     * Function:
     * @param v
     */
    public void onAccessoriesSelectionClick (View v)
    {
        presenter.callCorrectItemsList(getString(R.string.menu_accessories));
    }

    /**
     * Function:
     * @param v
     */
    public void onUpperLevelSelectionClick (View v)
    {
        presenter.callCorrectItemsList(getString(R.string.menu_upper_level));
    }

    /**
     * Function:
     * @param v
     */
    public void onLowerLevelSelectionClick (View v)
    {
        presenter.callCorrectItemsList(getString(R.string.menu_lower_level));
    }

    /**
     * Function:
     * @param v
     */
    public void onWarmLevelSelectionClick (View v)
    {
        presenter.callCorrectItemsList(getString(R.string.menu_warm_clothes));
    }

    /**
     * Function:
     * @param v
     */
    public void onBootsSelectionClick (View v)
    {
        presenter.callCorrectItemsList(getString(R.string.menu_boots));
    }

    /**
     * Function: onSaveNewLook
     * @param v
     */
    public void onSaveNewLook (View v)
    {
        if(edtLookName.getText() != null &&  !edtLookName.getText().equals("")) //TODO: don't believe in it
        {
            LookObject look = presenter.db.lookDao().findFinished(false);
            look.setIsFinished(true);

            UserObject activeUser = presenter.db.userDao().findActiveUser();
            look.setUserName(activeUser.getUserName());

            //TODO: correct update should be here
            presenter.db.lookDao().delete(look);
            presenter.db.lookDao().insertAll(look);
            finish();
        }
        else
        {
            Toast.makeText(this,"You need add some name here", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public Context getThis()
    {
        return this;
    }
}
