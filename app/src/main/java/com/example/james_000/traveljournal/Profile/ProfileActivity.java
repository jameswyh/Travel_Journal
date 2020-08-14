package com.example.james_000.traveljournal.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.james_000.traveljournal.Likes.database.LikeDBHelper;
import com.example.james_000.traveljournal.New.database.DetailDBHelper;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Utils.BottomNavigationViewHelper;
import com.example.james_000.traveljournal.Utils.GridImageAdapter;
import com.example.james_000.traveljournal.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private DetailDBHelper mhelper1;
    private SQLiteDatabase db1;
    private LikeDBHelper mhelper2;
    private SQLiteDatabase db2;
    private int postcount = 0;
    private int beencount = 0;
    private int likecount = 0;
    private TextView posttv;
    private TextView beentv;
    private TextView liketv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started");

        setupBottomNavigationView();
        setupToolbar();
        initcount();
        tempGridSetup();

    }

    private void initcount(){
        mhelper1=new DetailDBHelper(this);
        db1=mhelper1.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery("select count(*) from Detail",null);
        if (cursor1.moveToFirst()) {
            postcount = cursor1.getInt(0);
        }
        cursor1.close();
        posttv=(TextView)findViewById(R.id.tvPosts);
        posttv.setText(String.valueOf(postcount));

        Cursor cursor2 = db1.rawQuery("select count(distinct(address)) from Detail",null);
        if (cursor2.moveToFirst()) {
            beencount = cursor2.getInt(0);
        }
        cursor2.close();
        beentv=(TextView)findViewById(R.id.tvBeen);
        beentv.setText(String.valueOf(beencount));

        mhelper2=new LikeDBHelper(this);
        db2=mhelper2.getReadableDatabase();

        Cursor cursor3 = db2.rawQuery("select count(*) from Likes",null);
        if (cursor3.moveToFirst()) {
            likecount = cursor3.getInt(0);
        }
        cursor3.close();
        liketv=(TextView)findViewById(R.id.tvLikes);
        liketv.setText(String.valueOf(likecount));
    }

    private void tempGridSetup(){
        mhelper1=new DetailDBHelper(this);
        db1=mhelper1.getReadableDatabase();
        Cursor cursor = db1.rawQuery("select * from Detail",null);
        ArrayList<String> imgURLs = new ArrayList<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex("Path"));
            imgURLs.add(path);
        }
        Collections.reverse(imgURLs);
        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(final ArrayList<String> imgURLs){
        GridView gridView = (GridView) findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);
        GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProfileActivity.this, LargeImageActivity.class);
                intent.putExtra("uri",imgURLs.get(i).toString() );
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initcount();
        tempGridSetup();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(ProfileActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }



    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(ProfileActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}