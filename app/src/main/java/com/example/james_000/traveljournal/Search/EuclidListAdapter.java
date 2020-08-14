package com.example.james_000.traveljournal.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.james_000.traveljournal.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;



public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> {

    public static final String KEY_AVATAR = "pic";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_DISTANCE = "distance";


    private final LayoutInflater mInflater;
    private List<Map<String, Object>> mData;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final com.example.james_000.traveljournal.Search.EuclidListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new com.example.james_000.traveljournal.Search.EuclidListAdapter.ViewHolder();
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.mListItemName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.mListItemAddress = (TextView) convertView.findViewById(R.id.item_address);
            viewHolder.mListItemDistance = (TextView) convertView.findViewById(R.id.item_distance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (com.example.james_000.traveljournal.Search.EuclidListAdapter.ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load((String) mData.get(position).get(KEY_AVATAR))
                .resize(120,90).centerCrop()
                .placeholder(R.color.white)
                .into(viewHolder.mListItemAvatar);

        viewHolder.mListItemName.setText(mData.get(position).get(KEY_NAME).toString().toUpperCase());
        viewHolder.mListItemAddress.setText((String) mData.get(position).get(KEY_ADDRESS));
        viewHolder.mListItemDistance.setText((String) mData.get(position).get(KEY_DISTANCE));

        return convertView;
    }

    static class ViewHolder {
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemAddress;
        TextView mListItemDistance;
    }
}
