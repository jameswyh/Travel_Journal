package com.example.james_000.traveljournal.Likes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.james_000.traveljournal.Likes.database.LikeDBHelper;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Search.EuclidListAdapter;
import com.example.james_000.traveljournal.Search.SearchBeanEntity;
import com.example.james_000.traveljournal.Search.SearchBodyEntity;
import com.example.james_000.traveljournal.Search.SearchDetailActivity;
import com.example.james_000.traveljournal.Search.SearchEntity;
import com.example.james_000.traveljournal.Search.SearchListEntity;
import com.example.james_000.traveljournal.Search.location;
import com.example.james_000.traveljournal.Search.picList;
import com.example.james_000.traveljournal.Utils.BottomNavigationViewHelper;
import com.example.james_000.traveljournal.application.MyApplication;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LikesActivity  extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "LikesActivity";
    private static final int ACTIVITY_NUM = 3;

    private LikeDBHelper mhelper;
    private SQLiteDatabase db;
    private ListView listView;

    java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");

    public String res;
    SearchEntity entity;
    Class<SearchEntity> entityClass = SearchEntity.class;

    List<Map<String, Object>> likesList;

    String[] lons;
    String[] lats;
    String[] names;
    String[] summarys;
    String[] contents;
    String[] attentions;
    String[] opentimes;
    String[] coupons;
    String[] my_addresss;
    String[] pic;
    LatLng point1;
    ArrayList<String[]> pictures;

    ArrayList<String[]> picture;
    String[] value;
    String[] summary;
    String[] my_address;
    String[] opentime;
    String[] coupon;
    String[] attention;
    String[] content;
    String[] lat;
    String[] lon;


    List<Map<String, Object>> likeList;
    int i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        Log.d(TAG, "onCreate: started.");
        listView = (ListView) this.findViewById(R.id.list_view_like);
        listView.setOnItemClickListener(this);
        initview();
        setupBottomNavigationView();
    }

    public void initview() {
        mhelper=new LikeDBHelper(this);
        db=mhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Likes",null);
        Map<String, Object> likeMap;

        likesList = new ArrayList<>();
        likeList = new ArrayList<>();
        while (cursor.moveToNext()){
            String res = cursor.getString(cursor.getColumnIndex("res"));
            String position = cursor.getString(cursor.getColumnIndex("position"));
            likeMap = new HashMap<>();
            likeMap.put("res", res);
            likeMap.put("position", position);
            likeList.add(likeMap);
        }
        picture = new ArrayList<String[]>(likeList.size());
        value = new String[likeList.size()];
        summary = new String[likeList.size()];
        my_address = new String[likeList.size()];
        opentime = new String[likeList.size()];
        coupon = new String[likeList.size()];
        attention = new String[likeList.size()];
        content = new String[likeList.size()];
        lat = new String[likeList.size()];
        lon = new String[likeList.size()];


        for(i=0;i<likeList.size();i++){
            Map<String, Object> likesMap;
            String responseData = likeList.get(i).get("res").toString();
            int j = Integer.valueOf(likeList.get(i).get("position").toString());


            Gson gs = new Gson();
            entity= gs.fromJson(responseData,entityClass);
            String code= entity.getShowapi_res_code();
            SearchBodyEntity showapi_res_body = entity.getShowapi_res_body();
            String retCode = showapi_res_body.getRet_code();
            SearchBeanEntity pagebean = showapi_res_body.getPagebean();
            Log.e("pagebean----",""+pagebean);
            Log.e("pagebeangetAllPages----",""+pagebean.getAllPages());
            Log.e("getContentList----",""+pagebean.getContentlist());

            List<SearchListEntity> contentList = pagebean.getContentlist();
            if (contentList.size() != 0) {
                Log.e("contentList----", "" + contentList);
                SearchListEntity[] contentlist_objects = new SearchListEntity[contentList.size()];
                location[] locations = new location[contentList.size()];
                lons = new String[contentList.size()];
                lats = new String[contentList.size()];
                names = new String[contentList.size()];
                summarys = new String[contentList.size()];
                contents = new String[contentList.size()];
                attentions = new String[contentList.size()];
                opentimes = new String[contentList.size()];
                coupons = new String[contentList.size()];
                my_addresss = new String[contentList.size()];
                pic = new String[contentList.size()];
                pictures = new ArrayList<String[]>(contentList.size());

                for (int i = 0; i < contentList.size(); i++) {
                    SearchListEntity contentlist_object = contentList.get(i);
                    contentlist_objects[i] = contentlist_object;
                    location location = contentlist_object.getLocation();
                    locations[i] = location;
                    String lon = location.getLon();
                    lons[i] = lon;
                    String lat = location.getLat();
                    lats[i] = lat;
                    String name = contentlist_object.getName();
                    names[i] = name;
                    String summary = contentlist_object.getSummary();
                    summarys[i] = summary;
                    String content = contentlist_object.getContent();
                    contents[i] = content;
                    String attention = contentlist_object.getAttention();
                    attentions[i] = attention;
                    String opentime = contentlist_object.getOpentime();
                    opentimes[i] = opentime;
                    String coupon = contentlist_object.getCoupon();
                    coupons[i] = coupon;
                    String my_address = contentlist_object.getAddress();
                    my_addresss[i] = my_address;
                    List<picList> picList1 = contentlist_object.getPicList();
                    if (picList1.size() != 0) {
                        pic[i] = picList1.get(0).getPicUrl();
                    }
                    List<picList> picList = contentlist_object.getPicList();
                    if (picList.size() != 0) {
                        String[] picturess = new String[picList.size()];
                        for (int k = 0; k < picList.size(); k++) {
                            picturess[k] = picList.get(k).getPicUrl();
                        }
                        pictures.add(i, picturess);
                    } else
                        pictures.add(i, null);

                }

                picture.add(pictures.get(j));
                value[i] = names[j];
                summary[i] = summarys[j];
                my_address[i] = my_addresss[j];
                opentime[i] = opentimes[j];
                coupon[i] = coupons[j];
                attention[i] = attentions[j];
                content[i] = contents[j];
                lat[i] = lats[j];
                lon[i] = lons[j];


                likesMap = new HashMap<>();
                likesMap.put("pic", pic[j]);
                likesMap.put("name", names[j]);
                likesMap.put("address", my_addresss[j]);
                MyApplication app = (MyApplication) getApplication();
                point1 = app.getMyLatLng();
                LatLng point2 = new LatLng(Double.valueOf(Double.valueOf(lats[j])), Double.valueOf(lons[j]));
//            Log.i("location", "latitude:" + lat1
//                    + " longitude:" + lon1 + "---");
                likesMap.put("distance", "距您" + myformat.format(DistanceUtil.getDistance(point1, point2) / 1000) + "千米");
                //            Log.i("location", "latitude:" + Double.valueOf(lat[i])+" longitude:" + Double.valueOf(lon[i])+ "---"+DistanceUtil.getDistance(point1, point2));
                likesList.add(likesMap);
            }

        }
        Collections.reverse(likesList);
        EuclidListAdapter adapter = new EuclidListAdapter(this, R.layout.list_item, likesList);
        //实现列表的显示
        listView.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();
        initview();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(LikesActivity.this, SearchDetailActivity.class);
        String position = String.valueOf(i);
        intent.putExtra("position", position);
        intent.putExtra("name", Reverse(value));
        intent.putExtra("summary", Reverse(summary));
        intent.putExtra("my_address", Reverse(my_address));
        intent.putExtra("opentime", Reverse(opentime));
        Collections.reverse(picture);
        intent.putExtra("picture", picture);
        intent.putExtra("coupon", Reverse(coupon));
        intent.putExtra("attention", Reverse(attention));
        intent.putExtra("content", Reverse(content));
        intent.putExtra("lon", Reverse(lon));
        intent.putExtra("lat", Reverse(lat));
        intent.putExtra("mylocation",point1);
        intent.putExtra("res",res);
        startActivity(intent);
    }

    private String[]Reverse(String[] String){
        for (int start = 0, end = String.length - 1; start < end; start++, end--) {
            String temp = String[end];
            String[end] = String[start];
            String[start] = temp;
        }
        return String;
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(LikesActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}