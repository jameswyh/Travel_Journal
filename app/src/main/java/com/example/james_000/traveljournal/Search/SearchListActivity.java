package com.example.james_000.traveljournal.Search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.application.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
    java.text.DecimalFormat myformat2=new java.text.DecimalFormat("0");
    ListView listView;
    String[] pic;
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
    String lat1;
    String lon1;
    String kw;
    String res;
    LatLng point1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlist);

        TextView textView = (TextView) findViewById(R.id.profileName);
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) this.findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        pic = intent.getStringArrayExtra("pic");
        picture = (ArrayList<String[]>) intent.getSerializableExtra("picture");
        value = intent.getStringArrayExtra("name");
        summary = intent.getStringArrayExtra("summary");
        my_address = intent.getStringArrayExtra("my_address");
        opentime = intent.getStringArrayExtra("opentime");
        coupon = intent.getStringArrayExtra("coupon");
        attention = intent.getStringArrayExtra("attention");
        content = intent.getStringArrayExtra("content");
        lat = intent.getStringArrayExtra("lat");
        lon = intent.getStringArrayExtra("lon");
        kw = intent.getStringExtra("kw");
        res = intent.getStringExtra("res");


        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();
        Log.i("location", "latitude1:" + lat1
                + " longitude1:" + lon1 + "---");

        textView.setText(kw);
        MyApplication app = (MyApplication) getApplication();


        for (int i = 0; i < value.length; i++) {
            profileMap = new HashMap<>();
            profileMap.put("pic", pic[i]);
            profileMap.put("name", value[i]);
            profileMap.put("address", my_address[i]);
            point1 = app.getMyLatLng();
            LatLng point2 = new LatLng(Double.valueOf(Double.valueOf(lat[i])), Double.valueOf(lon[i]));
            Log.i("location", "latitude:" + lat1
                    + " longitude:" + lon1 + "---");
            profileMap.put("distance", "距您"+myformat.format(DistanceUtil.getDistance(point1, point2)/1000)+"千米");
            Log.i("location", "latitude:" + Double.valueOf(lat[i])+" longitude:" + Double.valueOf(lon[i])+ "---"+DistanceUtil.getDistance(point1, point2));
            profilesList.add(profileMap);
        }


        EuclidListAdapter adapter = new EuclidListAdapter(this, R.layout.list_item, profilesList);
        //实现列表的显示
        listView.setAdapter(adapter);
        Log.i("location999","444");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(SearchListActivity.this, SearchDetailActivity.class);
        String position = String.valueOf(i);
        intent.putExtra("position", position);
        intent.putExtra("name", value);
        intent.putExtra("summary", summary);
        intent.putExtra("my_address", my_address);
        intent.putExtra("opentime", opentime);
        intent.putExtra("picture", picture);
        intent.putExtra("coupon", coupon);
        intent.putExtra("attention", attention);
        intent.putExtra("content", content);
        intent.putExtra("lon", lon);
        intent.putExtra("lat", lat);
        intent.putExtra("lon1", lon1);
        intent.putExtra("lat1", lat1);
        intent.putExtra("kw", kw);
        intent.putExtra("mylocation",point1);
        intent.putExtra("res",res);
        startActivity(intent);
    }
}