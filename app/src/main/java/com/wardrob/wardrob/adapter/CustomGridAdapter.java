package com.wardrob.wardrob.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.database.AppDatabase;
import com.wardrob.wardrob.database.LookObject;
import com.wardrob.wardrob.screens.new_look.NewLookActivity;

import java.util.HashMap;
import java.util.List;

public class CustomGridAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener
{
    Context context;
    int layoutResourceId;
    List<LookObject> look_list;

    private AppDatabase db = null;

    public CustomGridAdapter(@NonNull Context c, int lResourceId, List<LookObject> list)
    {
        super(c, lResourceId ,list);

        context = c;
        look_list = list;
        layoutResourceId = lResourceId;

        this.db = AppDatabase.getAppDatabase(context);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        }
        else
        {
            holder = (RecordHolder) row.getTag();
        }


        final LookObject item = look_list.get(position);
        holder.txtTitle.setText(item.getLookName().toString());

        // TODO: Set image here
        //byte[] imgData = FileManagement.getImageFileInByteArray(item.getItemImageName());
        //Bitmap imageBitmap = FileManagement.resizeImageForThumbnail(imgData);
        //image.setImageBitmap(imageBitmap);
        //holder.imageItem.setImageBitmap(imageBitmap);
        //NOW JUST DEFAULT IMAGE
        holder.imageItem.setImageResource(R.drawable.default_logo_1);


        final View finalRow = row;
        final int pos = position;

        holder.imageItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callLookItemActivity(pos);
            }
        });

        holder.imageItem.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                db.lookDao().delete(item);
                finalRow.setVisibility(View.GONE);
                return true;
            }
        });
        return row;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        callLookItemActivity(position);
    }

    private void callLookItemActivity(int position)
    {
        String item_id = String.valueOf( look_list.get(position).getUid());

        Intent intent = new Intent(context, NewLookActivity.class);
        HashMap<String, String> hashMap = new HashMap<>();
        try
        {
            hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_id_item), item_id);
        }
        catch (NumberFormatException nfe) {}
        intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
        context.startActivity(intent);
    }

    static class RecordHolder
    {
        TextView txtTitle;
        ImageView imageItem;
    }
}
