package com.wardrob.wardrob.screens.new_item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rarepebble.colorpicker.ColorPickerView;
import com.wardrob.wardrob.core.GeneralActivity;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.adapter.CustomSpinnerAdapter;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.core.SpeechRecognitionHelper;
import com.wardrob.wardrob.core.TakeImageHelper;
import com.wardrob.wardrob.database.ItemObject;

import java.util.ArrayList;
import java.util.HashMap;

import timber.log.Timber;

public class NewItemActivity extends GeneralActivity implements NewItemView
{

    NewItemPresenter presenter;

    ItemObject item = null;

    ImageView cloth_img                 = null;
    ImageView default_category          = null;
    TextView  default_category_name     = null;

    Spinner   sprCategorySelection      = null;
    Spinner   sprRelationsSelection     = null;
    Spinner   sprWashingSelection       = null;
    Spinner   sprDrySelection           = null;
    Spinner   sprIroningSelection       = null;
    Spinner   sprProfCleaningSelection  = null;
    Spinner   sprWhiteningSelection     = null;

    CheckBox ckbxSecurity = null;

    String category;

    final ArrayList<String> categoryList  = new ArrayList<>();
    final ArrayList<String> relationsList = new ArrayList<>();
    final ArrayList<String> washingArray  = new ArrayList<>();
    final ArrayList<String> dryArray      = new ArrayList<>();
    final ArrayList<String> proCleanArray = new ArrayList<>();
    final ArrayList<String> ironArray     = new ArrayList<>();
    final ArrayList<String> whiteArray    = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        initUIElementsPerActivity();


        Intent intent = this.getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)
                                            intent.getSerializableExtra(
                                            ResourcesGetterSingleton.getStr(R.string.bundle_hash));

        String id = hashMap.get(ResourcesGetterSingleton.getStr(R.string.bundle_id_item));


        if (!id.equals("none"))
        {
            try
            {
                presenter = new NewItemPresenter(this, null);
                Integer item_id = Integer.parseInt(id);
                item = presenter.getItem(item_id);
                loadUIElementsPerObject(item);
                presenter = new NewItemPresenter(this, item.getItemMainCategory());

            } catch(NumberFormatException nfe) {}
        }
        else
        {
            category = hashMap.get(ResourcesGetterSingleton.getStr(R.string.bundle_new_item));
            presenter = new NewItemPresenter(this, category);

            Integer index = Integer.valueOf(hashMap.get(ResourcesGetterSingleton.getStr(R.string.bundle_new_item_index)));
            setHeaderTextAndIcon(index);
        }

        closeKeyboard();
    }

    /**
     * Function: initUIElementsPerActivity
     */
    private void initItemsStringArrays()
    {
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_none));
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_summer));
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_autumn));
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_spring));
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_winter));
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_spring_summer));
        categoryList.add(ResourcesGetterSingleton.getStr(R.string.category_winter_autumn));

        relationsList.add(ResourcesGetterSingleton.getStr(R.string.relations_casual));
        relationsList.add(ResourcesGetterSingleton.getStr(R.string.relations_beach));
        relationsList.add(ResourcesGetterSingleton.getStr(R.string.relations_sports));
        relationsList.add(ResourcesGetterSingleton.getStr(R.string.relations_hobby));
        relationsList.add(ResourcesGetterSingleton.getStr(R.string.relations_Evening));

        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_can));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_light));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_delicate));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_till_30));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_till_40));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_above_50));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_not));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_only_hand));
        washingArray.add(ResourcesGetterSingleton.getStr(R.string.washing_not_shower));

        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_1));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_2));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_3));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_4));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_5));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_6));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_7));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_8));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_9));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_10));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_11));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_12));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_13));
        dryArray.add(ResourcesGetterSingleton.getStr(R.string.drying_14));

        ironArray.add(ResourcesGetterSingleton.getStr(R.string.ironing_1));
        ironArray.add(ResourcesGetterSingleton.getStr(R.string.ironing_2));
        ironArray.add(ResourcesGetterSingleton.getStr(R.string.ironing_3));
        ironArray.add(ResourcesGetterSingleton.getStr(R.string.ironing_4));
        ironArray.add(ResourcesGetterSingleton.getStr(R.string.ironing_5));
        ironArray.add(ResourcesGetterSingleton.getStr(R.string.ironing_6));

        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_1));
        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_2));
        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_3));
        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_4));
        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_5));
        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_6));
        proCleanArray.add(ResourcesGetterSingleton.getStr(R.string.cleaning_7));

        whiteArray.add(ResourcesGetterSingleton.getStr(R.string.whitening_1));
        whiteArray.add(ResourcesGetterSingleton.getStr(R.string.whitening_2));
        whiteArray.add(ResourcesGetterSingleton.getStr(R.string.whitening_3));
        whiteArray.add(ResourcesGetterSingleton.getStr(R.string.whitening_4));
    }


    /**
     * Function: initUIElementsPerActivity
     */
    private void initUIElementsPerActivity()
    {
        cloth_img             = (ImageView) findViewById(R.id.cloth_img);
        default_category      = (ImageView) findViewById(R.id.default_category);
        default_category_name = (TextView)  findViewById(R.id.default_category_name);
        ckbxSecurity          = (CheckBox)  findViewById(R.id.ckbxSecurity);

        //---------------------------------------------------------------------------
        initItemsStringArrays();
        //---------------------------------------------------------------------------

        sprCategorySelection  = (Spinner) findViewById(R.id.sprCategorySelection);
        sprCategorySelection.setAdapter(new ArrayAdapter<String>(this,
                                        R.layout.spinner_item,
                                        categoryList));
        sprCategorySelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){}
            public void onNothingSelected(AdapterView<?> parent){}
        });

        //---------------------------------------------------------------------------
        sprRelationsSelection = (Spinner) findViewById(R.id.sprRelationsSelection);
        sprRelationsSelection.setAdapter(new ArrayAdapter<String>(this,
                R.layout.spinner_item,
                relationsList));
        sprRelationsSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){}
            public void onNothingSelected(AdapterView<?> parent){}
        });
        //---------------------------------------------------------------------------
        sprWashingSelection = (Spinner)findViewById(R.id.sprWashingSelection);
        Integer[] washingImageArray =
        { R.drawable.washing_canbe, R.drawable.washing_light, R.drawable.washing_delicate,
          R.drawable.washing_till30, R.drawable.washing_till40, R.drawable.washing_above50,
          R.drawable.washing_notwash, R.drawable.washing_byhand, R.drawable.washing_notshowering};
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this,washingImageArray, washingArray);
        sprWashingSelection.setAdapter(adapter);
        //---------------------------------------------------------------------------

        sprDrySelection  = (Spinner)findViewById(R.id.sprDrySelection);

        Integer[] dryImageArray = {
        R.drawable.dry_1, R.drawable.dry_2, R.drawable.dry_3, R.drawable.dry_4,
        R.drawable.dry_5, R.drawable.dry_6, R.drawable.dry_7, R.drawable.dry_8,
        R.drawable.dry_9, R.drawable.dry_10, R.drawable.dry_11, R.drawable.dry_12,
        R.drawable.dry_13, R.drawable.dry_14 };
        CustomSpinnerAdapter adapter_dry = new CustomSpinnerAdapter(this, dryImageArray, dryArray);
        sprDrySelection.setAdapter(adapter_dry);
        //---------------------------------------------------------------------------

        sprIroningSelection  = (Spinner)findViewById(R.id.sprIroningSelection);
        Integer[] ironImageArray = {
            R.drawable.ironing_1, R.drawable.ironing_2, R.drawable.ironing_3,
            R.drawable.ironing_4, R.drawable.ironing_5, R.drawable.ironing_6 };
        CustomSpinnerAdapter adapter_iron = new CustomSpinnerAdapter(this,ironImageArray, ironArray);
        sprIroningSelection.setAdapter(adapter_iron);
        //---------------------------------------------------------------------------

        sprProfCleaningSelection  = (Spinner)findViewById(R.id.sprProfCleaningSelection);
        Integer[] proCleanImageArray = {
            R.drawable.proclean_1, R.drawable.proclean_2, R.drawable.proclean_3,
            R.drawable.proclean_4, R.drawable.proclean_5, R.drawable.proclean_6, R.drawable.proclean_7 };
        CustomSpinnerAdapter adapter_cleaning = new CustomSpinnerAdapter(this,proCleanImageArray, proCleanArray);
        sprProfCleaningSelection.setAdapter(adapter_cleaning);

        //---------------------------------------------------------------------------
        sprWhiteningSelection  = (Spinner)findViewById(R.id.sprWhiteningSelection);
        Integer[] whiteImageArray = { R.drawable.whitening_1, R.drawable.whitening_2,
                                     R.drawable.whitening_3, R.drawable.whitening_4 };
        CustomSpinnerAdapter adapter_white= new CustomSpinnerAdapter(this,whiteImageArray,whiteArray);
        sprWhiteningSelection.setAdapter(adapter_white);
        //---------------------------------------------------------------------------
    }

    /**
     * Function: setSelectedItemInSpinner
     * @param item - Searched string
     * @param sp - Spinner to set
     * @param arr - String Array with list of strings
     */
    private void setSelectedItemInSpinner(String item, Spinner sp, ArrayList<String> arr)
    {
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).equals(item))
            {
                sp.setSelection(i);
            }
        }
    }

    /**
     * Function: loadUIElementsPerObject
     */
    private void loadUIElementsPerObject(ItemObject item)
    {
        // cloth_img             = (ImageView) findViewById(R.id.cloth_img);
        // default_category      = (ImageView) findViewById(R.id.default_category);
        //---------------------------------------------------------------------------
        ckbxSecurity.setSelected(item.getItemVisibility());
        default_category_name.setText(item.getItemMainCategory());
        //---------------------------------------------------------------------------
        setSelectedItemInSpinner(item.getItemCategory(), sprCategorySelection,     categoryList);
        setSelectedItemInSpinner(item.getItemRelations(),sprRelationsSelection,    relationsList);
        setSelectedItemInSpinner(item.getItemWashing1(), sprWashingSelection,      washingArray);
        setSelectedItemInSpinner(item.getItemWashing2(), sprDrySelection,          dryArray);
        setSelectedItemInSpinner(item.getItemWashing3(), sprIroningSelection,      ironArray);
        setSelectedItemInSpinner(item.getItemWashing4(), sprProfCleaningSelection, proCleanArray);
        setSelectedItemInSpinner(item.getItemWashing5(), sprWhiteningSelection,    whiteArray);
        //---------------------------------------------------------------------------
        byte[] imgData = FileManagement.getImageFileInByteArray(item.getItemImageName());
        Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
        cloth_img.setImageBitmap(imageBitmap);
        //---------------------------------------------------------------------------
        EditText edtItemNaming = (EditText) findViewById(R.id.edtItemNaming);
        edtItemNaming.setText(item.getItemName());
        //---------------------------------------------------------------------------
        ColorPickerView colorPicker = (ColorPickerView) findViewById(R.id.colorPicker);
        colorPicker.setColor(item.getItemColor());
        //---------------------------------------------------------------------------
        EditText edtDateSet = (EditText) findViewById(R.id.edtDateSet);
        edtDateSet.setText(item.getItemPurchaseData());
        //---------------------------------------------------------------------------
        EditText edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtPrice.setText(item.getItemPrice());
        //---------------------------------------------------------------------------
        RatingBar ratingBarWarmsLevel = (RatingBar) findViewById(R.id.ratingBarWarmsLevel);
        ratingBarWarmsLevel.setRating(item.getItemWarms());
        //---------------------------------------------------------------------------
        RatingBar ratingBarAttitude = (RatingBar) findViewById(R.id.ratingBarAttitude);
        ratingBarAttitude.setRating(item.getItemFavor());
        //---------------------------------------------------------------------------
    }

    /**
     * Function: setHeaderTextAndIcon
     * @param index
     */
    private void setHeaderTextAndIcon(Integer index)
    {
        switch (index)
        {
            case 0:
                default_category.setImageResource(R.drawable.shirt);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_upper_level));
                break;

            case 1:
                default_category.setImageResource(R.drawable.pants);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_lower_level));
                break;

            case 2:
                default_category.setImageResource(R.drawable.coat);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_warm_clothes));
                break;

            case 3:
                default_category.setImageResource(R.drawable.hat);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_hats));
                break;

            case 4:
                default_category.setImageResource(R.drawable.boots);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_boots));

            case 5:
                default_category.setImageResource(R.drawable.accessories);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_accessories));
                break;

            case 6:
                default_category.setImageResource(R.drawable.accessories);
                default_category_name.setText(ResourcesGetterSingleton.getStr(R.string.menu_accessories));
                break;

            default:
                break;
        }
    }

    /**
     * Function: getDateSelection
     * @param view
     */
    public void onTakePhoto(View view)
    {
        startActivityForResult(presenter.getPhotoIntent(this),
                               TakeImageHelper.REQUEST_IMAGE_SELECT);
    }


    /**
     * Function: getDateSelection
     * @param view
     */
    public void onDateDropDownDr(View view)
    {
        EditText edtDateSet = (EditText) findViewById(R.id.edtDateSet);
        presenter.getDateSelection(this,edtDateSet);
    }


    /**
     * Function: onSaveResults
     * @param view
     */
    public void onSaveResults(View view)
    {

        if (null == item)
        {
            ItemObject object = new ItemObject();

            object.setItemMainCategory(default_category_name.getText().toString());

            EditText edtItemNaming = (EditText) findViewById(R.id.edtItemNaming);
            object.setItemName(edtItemNaming.getText().toString());

            object.setItemImageName(presenter.object.destination.getPath());
            object.setItemCategory(sprCategorySelection.getSelectedItem().toString());

            object.setItemRelations(sprRelationsSelection.getSelectedItem().toString());

            ColorPickerView colorPicker = (ColorPickerView) findViewById(R.id.colorPicker);
            object.setItemColor(colorPicker.getColor());

            EditText edtDateSet = (EditText) findViewById(R.id.edtDateSet);
            object.setItemPurchaseData(edtDateSet.getText().toString());

            EditText edtPrice = (EditText) findViewById(R.id.edtPrice);
            object.setItemPrice(edtPrice.getText().toString());

            RatingBar ratingBarWarmsLevel = (RatingBar) findViewById(R.id.ratingBarWarmsLevel);
            object.setItemWarms(ratingBarWarmsLevel.getRating());

            RatingBar ratingBarAttitude = (RatingBar) findViewById(R.id.ratingBarAttitude);
            object.setItemFavor(ratingBarAttitude.getRating());

            CheckBox ckbxSecurity = (CheckBox) findViewById(R.id.ckbxSecurity);
            object.setItemVisibility(ckbxSecurity.isChecked());

            object.setItemWashing1(sprWashingSelection.getSelectedItem().toString());
            object.setItemWashing2(sprDrySelection.getSelectedItem().toString());
            object.setItemWashing3(sprIroningSelection.getSelectedItem().toString());
            object.setItemWashing4(sprProfCleaningSelection.getSelectedItem().toString());

            presenter.saveItem(object);
            Timber.d("Save item in database");
        }
        else
        {
            presenter.db.itemDao().delete(item);

            item.setItemMainCategory(default_category_name.getText().toString());

            EditText edtItemNaming = (EditText) findViewById(R.id.edtItemNaming);
            item.setItemName(edtItemNaming.getText().toString());

            if(null != presenter.object)
                item.setItemImageName(presenter.object.destination.getPath());

            item.setItemCategory(sprCategorySelection.getSelectedItem().toString());

            item.setItemRelations(sprRelationsSelection.getSelectedItem().toString());

            ColorPickerView colorPicker = (ColorPickerView) findViewById(R.id.colorPicker);
            item.setItemColor(colorPicker.getColor());

            EditText edtDateSet = (EditText) findViewById(R.id.edtDateSet);
            item.setItemPurchaseData(edtDateSet.getText().toString());

            EditText edtPrice = (EditText) findViewById(R.id.edtPrice);
            item.setItemPrice(edtPrice.getText().toString());

            RatingBar ratingBarWarmsLevel = (RatingBar) findViewById(R.id.ratingBarWarmsLevel);
            item.setItemWarms(ratingBarWarmsLevel.getRating());

            RatingBar ratingBarAttitude = (RatingBar) findViewById(R.id.ratingBarAttitude);
            item.setItemFavor(ratingBarAttitude.getRating());

            CheckBox ckbxSecurity = (CheckBox) findViewById(R.id.ckbxSecurity);
            item.setItemVisibility(ckbxSecurity.isChecked());

            item.setItemWashing1(sprWashingSelection.getSelectedItem().toString());
            item.setItemWashing2(sprDrySelection.getSelectedItem().toString());
            item.setItemWashing3(sprIroningSelection.getSelectedItem().toString());
            item.setItemWashing4(sprProfCleaningSelection.getSelectedItem().toString());

            presenter.replaceItem(item);
            Timber.d("Replace item in database");
        }

        finish();
    }

    /**
     * Function: onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        onResponseActions(requestCode,
                            resultCode,
                            data,
                            presenter.object,
                            this,
                            cloth_img);

        // Speech recognition part
        if (resultCode == RESULT_OK && null != data)
        {
            if (requestCode == SpeechRecognitionHelper.REQ_CODE_SPEECH_INPUT) { }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getThis()
    {
        return this;
    }
}
