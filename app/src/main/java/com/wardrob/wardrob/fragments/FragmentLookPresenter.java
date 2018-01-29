package com.wardrob.wardrob.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wardrob.wardrob.III_newitem.NewItemActivity;
import com.wardrob.wardrob.II_mainscreen.MainActivity;
import com.wardrob.wardrob.IV_newlook.NewLookActivity;
import com.wardrob.wardrob.I_startup_new_member.LoginCreateActivity;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.GeneralManager;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.ItemObject;
import com.wardrob.wardrob.database.UserObject;

import java.util.HashMap;
import java.util.List;
import timber.log.Timber;

public class FragmentLookPresenter
{

    private FragmentLookView view;
    private AppDatabase db = null;

    public FragmentLookPresenter(FragmentLookView v)
    {
        this.view = v;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }

    /**
     * Function: getListOfRelatedItems
     * @param category String
     * @return
     */
    public void getListOfRelatedItems(String category)
    {
        UserObject activeUser = this.db.userDao().findActiveUser();

        List<ItemObject> items = this.db.itemDao().findItemsByCategoryAndUser(category,
                                                                        activeUser.getUserName());

        Point size = getGridDimensionParameters(items.size());

        for(int i=0, c = 0, r = 0;i<items.size();i++, c++)
        {
            if(c == size.x)
            {
                c = 0;
                r++;
            }
            setItemsOnScreen(items.get(i), r, c);
        }
        debugItemList(items);
    }


    /**
     * Function: getGridDimensionParameters
     * Description:
     * @param total
     * @return {@link Point}
     */
    private Point getGridDimensionParameters(int total)
    {
        Point p = GeneralManager.getScreenSize(view.getCurrentActivity());

        int DEFINED_ITEM_WIDTH = 125;
        int column = p.x / DEFINED_ITEM_WIDTH;
        int row = total / column;

        Point colAndrow = new Point();

        colAndrow.x = column + 1; //TODO: +1 ?
        colAndrow.y = row;

        return colAndrow;
    }

    /**
     * Function: setItemsOnScreen
     * Description:
     *  TODO: Set color for item
     * @param item ItemObject
     * @return
     */
    private void setItemsOnScreen(final ItemObject item, int row, int col)
    {
        final View itemLayoutView = LayoutInflater.from(view.getThis()).inflate(
                                                  R.layout.item_elements, null);

        TextView description = (TextView) itemLayoutView.findViewById(R.id.txtItemDescription);
        description.setText(item.getItemName());

        View color = (View)  itemLayoutView.findViewById(R.id.vItemColor);
        //color.setBackground(item.getItemColor());

        ImageView image = (ImageView) itemLayoutView.findViewById(R.id.imgItemImage);
        try
        {
            byte[] imgData = FileManagement.getImageFileInByteArray(item.getItemImageName());
            Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
            image.setImageBitmap(imageBitmap);

            image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(view.getThis(), NewItemActivity.class);
                    HashMap<String, String> hashMap = new HashMap<>();
                    try
                    {
                        String item_id = String.valueOf(item.getItemId());
                        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_id_item), item_id);
                    } catch(NumberFormatException nfe) {}
                    intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
                    view.getThis().startActivity(intent);
                }
            });

            image.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    //TODO: Here should be alarm popup
                    Toast.makeText(v.getContext(),
                            "Delete: " + item.getItemName(),
                            Toast.LENGTH_LONG).show();

                    deleteItemFromDatabase(item);
                    itemLayoutView.setVisibility(View.GONE);
                    return true;
                }
            });
        }
        catch (NullPointerException e) {Timber.e(e.toString());}

        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width  = GridLayout.LayoutParams.WRAP_CONTENT;
        param.rightMargin = 5;
        param.topMargin   = 5;
        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(col);
        param.rowSpec    = GridLayout.spec(row);
        itemLayoutView.setLayoutParams(param);

        this.view.getItemListLayout().addView(itemLayoutView);
    }


    /**
     * Function: deleteItemFromDatabase
     * Description: Used only for debug purposes.
     * @param item
     * @return
     */
    private void deleteItemFromDatabase(ItemObject item)
    {
        FileManagement.deleteFile(item.getItemImageName());
        this.db.itemDao().delete(item);
    }


    /**
     * Function: debugItemList
     * Description: Used only for debug purposes.
     * @param items
     * @return
     */
    private void debugItemList(List<ItemObject> items)
    {
        for(int i=0; i<items.size();i++)
        {
            Timber.d("\n Item name: " + items.get(i).getItemName());
        }
    }

}
