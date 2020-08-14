package com.example.james_000.traveljournal.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.james_000.traveljournal.New.database.DetailDBHelper;
import com.example.james_000.traveljournal.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;


public class HomeAdapter extends ArrayAdapter<Map<String, Object>> implements OnClickListener,OnMenuItemClickListener {

    public static final String KEY_AVATAR = "path";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TIME = "time";

    private ImageView menu;
    private DetailDBHelper mhelper;
    private SQLiteDatabase db;
    public String time;
    public int pos;
    public String id;



    private final LayoutInflater mInflater;
    private List<Map<String, Object>> mData;

    public HomeAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final com.example.james_000.traveljournal.Home.HomeAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_listitem, parent, false);
            viewHolder = new com.example.james_000.traveljournal.Home.HomeAdapter.ViewHolder();
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.mListItemCaption = (TextView) convertView.findViewById(R.id.item_caption);
            viewHolder.mListItemAddress = (TextView) convertView.findViewById(R.id.item_address);
            viewHolder.mListItemTime = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (com.example.james_000.traveljournal.Home.HomeAdapter.ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(Uri.parse((String) mData.get(position).get(KEY_AVATAR)))
//                .resize(120,90).centerCrop()
                .placeholder(R.color.white)
                .into(viewHolder.mListItemAvatar);

        viewHolder.mListItemCaption.setText(mData.get(position).get(KEY_CAPTION).toString());
        viewHolder.mListItemAddress.setText((String) mData.get(position).get(KEY_ADDRESS));
        viewHolder.mListItemTime.setText((String) mData.get(position).get(KEY_TIME));
//        id=(String) mData.get(position).get("id");
//        time=(String) mData.get(position).get(KEY_TIME);


        menu = (ImageView)convertView.findViewById(R.id.detailMenu);
        menu.setOnClickListener(this);
        menu.setTag(5<<24,position);
        menu.setTag(6<<24,mData.get(position).get("id"));

        return convertView;
    }

    @Override
    public void onClick(View view) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(getContext(), view);//第二个参数是绑定的那个view
        // 获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.detail_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        pos = (Integer) view.getTag(5<<24);
        id = (String) view.getTag(6<<24);
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete:
                mData.remove(pos);
                this.notifyDataSetChanged();
                mhelper=new DetailDBHelper(getContext());
                db=mhelper.getWritableDatabase();
                db.execSQL("delete from Detail where Id = '"+ id +"'");
                Intent intent = new Intent("jerry");
                intent.putExtra("change", "yes");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                break;
            default:
                break;
        }
        return false;
    }

    static class ViewHolder {
        ImageView mListItemAvatar;
        TextView mListItemCaption;
        TextView mListItemAddress;
        TextView mListItemTime;

    }
}