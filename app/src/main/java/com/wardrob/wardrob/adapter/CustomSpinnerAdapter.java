package com.wardrob.wardrob.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wardrob.wardrob.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;

    Context mContext;

    Integer[] sr_row_img            = null;
    ArrayList<String>  sr_row_str   = null;

    public CustomSpinnerAdapter(Context context,
                                Integer[] imgArray,
                                ArrayList<String> strArray)
    {
        mContext = context;
        sr_row_img = imgArray;
        sr_row_str = strArray;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return sr_row_str.size();
    }

    @Override
    public Object getItem(int position)
    {
        return sr_row_str.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.spinner_item_washing, null);


        ImageView img = (ImageView) vi.findViewById(R.id.img_item);
        img.setImageResource(sr_row_img[position]);

        TextView text = (TextView) vi.findViewById(R.id.txt_spinner);
        text.setText(sr_row_str.get(position));

        return vi;
    }

    /**
     * Function: onItemClick - Override method
     * @params:
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.i("Item","\n<<<<<<onClick>>>>\n");
    }
}