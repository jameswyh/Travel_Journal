package com.example.james_000.traveljournal.New;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.james_000.traveljournal.Home.HomeFragment;
import com.example.james_000.traveljournal.Home.MainActivity;
import com.example.james_000.traveljournal.New.database.DetailDBHelper;
import com.example.james_000.traveljournal.New.database.DetailDao;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Utils.AppConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShowPicActivity extends Activity implements OnGetGeoCoderResultListener{

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private MapView mMapView;
    private GeoCoder mSearch;
    private BaiduMap mBaiduMap;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private List<PoiInfo> mList;
    boolean isFirstLoc = true; // 是否首次定位

    private ImageButton mGetMylocationBN;
    LatLng ll;
    LatLng ptCenter;

    private TextView textlocation;

    private int flag = 0;

    private ImageView img;
    private TextView post;
    private EditText caption;

//    private int picWidth;
//    private int picHeight;

    private DetailDao mdao;
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        TextView textView = findViewById(R.id.profileName);
        textView.setText("New Post");
        post = findViewById(R.id.post_tv);
        post.setText("Save");
        caption = findViewById(R.id.caption);
        mdao = new DetailDao(this);

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caption.getText().toString().trim().equals(""))
                {
                    Toast.makeText(ShowPicActivity.this,"Please Write a Caption",Toast.LENGTH_LONG).show();
                }
                else{
                    mdao.insert(caption.getText().toString(),"file://" + getIntent().getStringExtra(AppConstant.KEY.IMG_PATH),textlocation.getText().toString(),
                            ptCenter.latitude,ptCenter.longitude,format.format(new Date(System.currentTimeMillis())));
                    Intent intent = new Intent(ShowPicActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

//        picWidth = getIntent().getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
//        picHeight = getIntent().getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
        img = (ImageView)findViewById(R.id.img);
        img.setImageURI(Uri.parse(getIntent().getStringExtra(AppConstant.KEY.IMG_PATH)));
//        img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));

        textlocation = (TextView)findViewById(R.id.text_location);
        textlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    flag=1;
                }
                else{
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    flag=0;
                }

            }
        });

        mGetMylocationBN = (ImageButton) findViewById(R.id.getMyLocation);
        mGetMylocationBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });

        mRecyclerView= (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList=new ArrayList<>();
        adapter=new MyAdapter(this,mList);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnMyItemClickListener(new MyAdapter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int pos) {
                textlocation.setText(mList.get(pos).name);
                mRecyclerView.setVisibility(View.INVISIBLE);
                flag=0;
            }
        });

        //获取地图控件引用
        mMapView= (MapView) findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }
            @Override
            public void onMapStatusChangeStart(MapStatus arg0, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // TODO Auto-generated method stub
                ptCenter = mBaiduMap.getMapStatus().target; //获取地图中心点坐标
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void getMyLocation()
    {
//        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                .location(ll));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(17.0f);//地图缩放级别
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        //设置地图中心点坐标
        MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(result.getLocation());
        mBaiduMap.animateMapStatus(status);
        Toast.makeText(ShowPicActivity.this, result.getAddress(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ShowPicActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        List<PoiInfo> list=result.getPoiList();
        mList.clear();
        for (int i=0;i<list.size();i++){
            mList.add(list.get(i));
        }

        if(mList.size()!= 0) {
            textlocation.setText(mList.get(0).name);
            textlocation.setEnabled(true);
        }
        else {
            textlocation.setText("Unknown Location");
            textlocation.setEnabled(false);
        }

        adapter.notifyDataSetChanged();

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));//改变地图状态？
//        Toast.makeText(MainActivity.this, result.getAddressDetail().city + "  "+
//                        result.getAddressDetail().district +"  "+ result.getAddressDetail().street +
//                        result.getAddressDetail().streetNumber,
//                Toast.LENGTH_LONG).show();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                ptCenter = ll;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ll));
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(17.0f);//地图缩放级别
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }
}