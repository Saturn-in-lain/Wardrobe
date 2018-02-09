package com.wardrob.wardrob.screens.tabs.tab_lookslist;

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

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.GeneralManager;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.ItemObject;
import com.wardrob.wardrob.database.LookObject;
import com.wardrob.wardrob.database.UserObject;
import com.wardrob.wardrob.screens.new_item.NewItemActivity;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class TabFragmentLooksListPresenter
{

    TabFragmentLooksListView view = null;
    private AppDatabase db = null;

    public TabFragmentLooksListPresenter(TabFragmentLooksListView view)
    {
        this.view = view;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());
    }

    public void initLooksList()
    {
        UserObject activeUser = this.db.userDao().findActiveUser();
        List<LookObject> list =  db.lookDao().findByUserName(activeUser.getUserName());

        Point size = getGridDimensionParameters(list.size());

        for(int i=0, c = 0, r = 0;i<list.size();i++, c++)
        {
            if(c == size.x)
            {
                c = 0;
                r++;
            }
            setItemsOnScreen(list.get(i), r, c);
        }

    }

    /**
     * Function: setItemsOnScreen
     * Description:
     *  TODO: this is almost full copy for FragmentItemsListPresenter-> getGridDimensionParameters, getListOfRelatedItems
     *  and should be refactored as architecture approach will be defined
     * @param item ItemObject
     * @return
     */
    private void setItemsOnScreen(final LookObject item, int row, int col)
    {
        final View itemLayoutView = LayoutInflater.from(view.getThis()).inflate(
                                                            R.layout.item_elements, null);

        TextView description = (TextView) itemLayoutView.findViewById(R.id.txtItemDescription);
        description.setText(item.getLookName());

        ImageView image = (ImageView) itemLayoutView.findViewById(R.id.imgItemImage);

        try
        {
//------------------------------------------------------------------------------------------------
            //TODO: Here we should get Look image
//            byte[] imgData = FileManagement.getImageFileInByteArray(item.getItemImageName());
//            Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
//            image.setImageBitmap(imageBitmap);
//------------------------------------------------------------------------------------------------
            image.setImageResource(R.drawable.default_logo_1);
//------------------------------------------------------------------------------------------------
            image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String item_id = String.valueOf(item.getUid());


                    {
//                        Intent intent = new Intent(view.getThis(), NewItemActivity.class);
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        try
//                        {
//                            hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_id_item), item_id);
//                        }
//                        catch (NumberFormatException nfe) {}
//                        intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
//                        view.getThis().startActivity(intent);
                    }
                    //else
                    {
                        //
                    }
                }
            });

            image.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {

                        Toast.makeText(v.getContext(),
                                "Delete: ",
                                Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        catch (NullPointerException e) {
            Timber.e(e.toString());}

        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width  = GridLayout.LayoutParams.WRAP_CONTENT;
        param.rightMargin = 5;
        param.topMargin   = 5;
        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(col);
        param.rowSpec    = GridLayout.spec(row);
        itemLayoutView.setLayoutParams(param);

        this.view.getGridLayout().addView(itemLayoutView);
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

        Point colAndRow = new Point();

        colAndRow.x = column + 1; //TODO: +1 ?
        colAndRow.y = row;

        return colAndRow;
    }

    public void initializeTempLookItem()
    {
        LookObject toDelete =  db.lookDao().findFinished(false);
        if(null != toDelete)
            db.lookDao().delete(toDelete);

        LookObject look = new LookObject();
        look.setIsFinished(false);
        db.lookDao().insertAll(look);
    }

}
