package com.example.james_000.traveljournal.Profile;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.james_000.traveljournal.New.database.DetailDBHelper;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Utils.UniversalImageLoader;

import static com.baidu.mapapi.BMapManager.getContext;

public class LargeImageActivity extends AppCompatActivity {
    private ImageView largeimage;
    private RelativeLayout imagelayout;
    private DetailDBHelper mhelper;
    private SQLiteDatabase db;
    private ImageView menu;
    String caption  = new String();
    String address  = new String();
    String time = new String();
    String id  = new String();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        largeimage=(ImageView)findViewById(R.id.large_image);
        Intent intent = getIntent();
        String imgURL = intent.getStringExtra("uri");

        mhelper=new DetailDBHelper(this);
        db=mhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Detail where Path = ?",new String[] { String.valueOf(imgURL)});

        while (cursor.moveToNext()) {
            caption = cursor.getString(cursor.getColumnIndex("Caption"));
            address = cursor.getString(cursor.getColumnIndex("Address"));
            time = cursor.getString(cursor.getColumnIndex("Time"));
            id = cursor.getString(cursor.getColumnIndex("Id"));
        }

        TextView Time = findViewById(R.id.imagetime);
        TextView Address = findViewById(R.id.address);
        TextView Caption = findViewById(R.id.caption);

        Time.setText(time);
        Address.setText(address);
        Caption.setText(caption);


        UniversalImageLoader.setImage(imgURL, largeimage, null, "");
        imagelayout=(RelativeLayout)findViewById(R.id.image_layout);
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menu = (ImageView)findViewById(R.id.image_more);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(getContext(), view);//第二个参数是绑定的那个view
                // 获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.detail_menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                mhelper=new DetailDBHelper(getContext());
                                db=mhelper.getWritableDatabase();
                                db.execSQL("delete from Detail where Id = '"+ id +"'");
                                Intent intent = new Intent("jerry");
                                intent.putExtra("change", "yes");
                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                                finish();
//                db.delete("Detail","Id=?",new String[]{id});
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        });
        menu.setTag(6<<24,id);

//        imagelayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


    }


}
