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
import com.wardrob.wardrob.adapter.CustomGridAdapter;
import com.wardrob.wardrob.core.FileManagement;
import com.wardrob.wardrob.core.GeneralManager;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.ItemObject;
import com.wardrob.wardrob.database.LookObject;
import com.wardrob.wardrob.database.UserObject;
import com.wardrob.wardrob.screens.new_item.NewItemActivity;
import com.wardrob.wardrob.screens.new_look.NewLookActivity;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class TabFragmentLooksListPresenter
{

    TabFragmentLooksListView view = null;
    private AppDatabase db = null;
    CustomGridAdapter customGridAdapter = null;

    public TabFragmentLooksListPresenter(TabFragmentLooksListView view)
    {
        this.view = view;
        this.db = AppDatabase.getAppDatabase(this.view.getThis());

        initLooksList();
    }

    /**
     * Function: initLooksList
     * Description:
     * @param
     * @return
     */
    public void initLooksList()
    {
        UserObject activeUser = this.db.userDao().findActiveUser();
        List<LookObject> list =  db.lookDao().findByUserName(activeUser.getUserName());

        customGridAdapter = new CustomGridAdapter(this.view.getThis(),
                                                                    R.layout.row_grid, list);
        this.view.getGridView().setAdapter(customGridAdapter);

    }

    /**
     * Function: initializeTempLookItem
     * Description:
     * @param
     * @return
     */
    public void initializeTempLookItem()
    {
        LookObject toDelete =  db.lookDao().findFinished(false);
        if(null != toDelete)
            db.lookDao().delete(toDelete);

        LookObject look = new LookObject();
        look.setIsFinished(false);
        db.lookDao().insertAll(look);
    }

    /**
     * Function: refreshLooksList
     * Description:
     * @param
     * @return
     */
    public void refreshLooksList()
    {
        Timber.d("\t\t\r >>> Refresh looks list");
        customGridAdapter.notifyDataSetChanged();
        this.view.getGridView().setAdapter(customGridAdapter);
    }

//    /**
//     * Function: setItemsOnScreen
//     * Description:
//     *  TODO: this is almost full copy for FragmentItemsListPresenter-> getGridDimensionParameters, getListOfRelatedItems
//     *  and should be refactored as architecture approach will be defined
//     * @param item ItemObject
//     * @return
//     */
//    private void setItemsOnScreen(final LookObject item, int row, int col)
//    {
//        final View itemLayoutView = LayoutInflater.from(view.getThis()).inflate(
//                                                            R.layout.item_elements, null);
//
//        TextView description = (TextView) itemLayoutView.findViewById(R.id.txtItemDescription);
//        description.setText(item.getLookName());
//
//        ImageView image = (ImageView) itemLayoutView.findViewById(R.id.imgItemImage);
//
//        try
//        {
////------------------------------------------------------------------------------------------------
//            //TODO: Here we should get Look image
////            byte[] imgData = FileManagement.getImageFileInByteArray(item.getItemImageName());
////            Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
////            image.setImageBitmap(imageBitmap);
////------------------------------------------------------------------------------------------------
//            image.setImageResource(R.drawable.default_logo_1);
////------------------------------------------------------------------------------------------------
//            image.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    String item_id = String.valueOf(item.getUid());
//
//                    Intent intent = new Intent(view.getThis(), NewLookActivity.class);
//                    HashMap<String, String> hashMap = new HashMap<>();
//                    try
//                    {
//                        hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_id_item), item_id);
//                    }
//                    catch (NumberFormatException nfe) {}
//                    intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
//                    view.getThis().startActivity(intent);
//                }
//            });
//
//            image.setOnLongClickListener(new View.OnLongClickListener()
//            {
//                @Override
//                public boolean onLongClick(View v)
//                {
//
//                    itemLayoutView.setVisibility(View.GONE);
//
//                    return true;
//                }
//            });
//        }
//        catch (NullPointerException e) {Timber.e(e.toString());}
//
//        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
//        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
//        param.width  = GridLayout.LayoutParams.WRAP_CONTENT;
//        param.rightMargin = 35;
//        param.topMargin   = 45;
//        param.setGravity(Gravity.CENTER);
//        param.columnSpec = GridLayout.spec(col);
//        param.rowSpec    = GridLayout.spec(row);
//        itemLayoutView.setLayoutParams(param);
//
//        this.view.getGridView().addView(itemLayoutView);
//        //this.view.getGridLayout().addView(itemLayoutView);
//    }
}
