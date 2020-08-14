package com.example.james_000.traveljournal.Home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.james_000.traveljournal.New.database.DetailDBHelper;
import com.example.james_000.traveljournal.Profile.LargeImageActivity;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Utils.UniversalImageLoader;
import com.example.james_000.traveljournal.application.MyApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment{
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mlocationClient;
    private MylocationListener mlistener;
    private Context context;

    private double mLatitude;
    private double mLongitude;
    private float mCurrentX;
    private LatLng latLng;

    private static int clickflag = 0;

    private ImageButton mGetMylocationBN;
    private MyOrientationListener myOrientationListener;
    private MyLocationConfiguration.LocationMode locationMode;

    private ProgressBar mProgressBar;

    private View view;
    private View InfoWindowView;

    private DetailDBHelper mhelper;
    private SQLiteDatabase db;
    private LocalBroadcastManager broadcastManager;

    public int flag = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)view.findViewById(R.id.bmapView);
        this.context = getActivity();
        mGetMylocationBN = (ImageButton) view.findViewById(R.id.getMyLocation);
        mGetMylocationBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
        initView();
        initLocation();
        setMarker();
        registerReceiver();

        return view;
    }
    private void initView() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设定初始化地图时的比例,当前为500米
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(6.0f);
        mBaiduMap.setMapStatus(msu);

    }

    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mlocationClient = new LocationClient(context);
        mlistener = new MylocationListener();

        //注册监听器
        mlocationClient.registerLocationListener(mlistener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        int span = 1000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        mlocationClient.setLocOption(mOption);

        myOrientationListener = new MyOrientationListener(context);

        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
    }


    public void setMarker(){
        mBaiduMap.clear();

        mhelper=new DetailDBHelper(getActivity());
        db=mhelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("select * from Detail",null);

        while (cursor.moveToNext()) {
            final View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_infowindow_view, null);
            final ImageView InfoWindowImage = (ImageView) markerView.findViewById(R.id.infowindow_image);
            final LatLng point = new LatLng(cursor.getDouble(cursor.getColumnIndex("Lat")), cursor.getDouble(cursor.getColumnIndex("Lon")));
            final String imgpath = cursor.getString(cursor.getColumnIndex("Path"));


            Glide.with(getActivity().getApplicationContext())
                    .load(imgpath.substring(7))
                    .asBitmap()
                    .error(R.drawable.nicolas_cage)
                    .override(250,250)
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                              @Override
                              public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                  InfoWindowImage.setImageBitmap(bitmap);
                                  BitmapDescriptor pic = BitmapDescriptorFactory.fromView(markerView);
                                  MarkerOptions overlayOptions = new MarkerOptions()
                                          .position(point)
                                          .icon(pic)
                                          .animateType(MarkerOptions.MarkerAnimateType.grow);//设置marker从地上生长出来的动画
                                  Marker marker = (Marker)mBaiduMap.addOverlay(overlayOptions);
                                  Bundle bundle = new Bundle();
                                  bundle.putString("uri",imgpath);
                                  marker.setExtraInfo(bundle);
                              }});

            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(getActivity(), LargeImageActivity.class);
                    Bundle bundle = marker.getExtraInfo();
                    String path = bundle.getString("uri");
                    intent.putExtra("uri",path);
                    startActivity(intent);

                    return false;
                }
            });

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if(!mlocationClient.isStarted())
        {
            mlocationClient.start();
        }
        myOrientationListener.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        if(mlocationClient.isStarted()) {
            mBaiduMap.setMyLocationEnabled(false);
            mlocationClient.stop();
            myOrientationListener.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        setMarker();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public void getMyLocation()
    {
        LatLng latLng=new LatLng(mLatitude,mLongitude);
        MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    //所有的定位信息都通过接口回调来实现
    public class MylocationListener implements BDLocationListener
    {
        //定位请求回调接口
        private boolean isFirstIn=true;
        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
            //MyLocationData 定位数据,定位数据建造器
            /*
             * 可以通过BDLocation配置如下参数
             * 1.accuracy 定位精度
             * 2.latitude 百度纬度坐标
             * 3.longitude 百度经度坐标
             * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
             * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
             * 6.direction GPS定位时方向角度
             *
             *
             * */
            mLatitude= bdLocation.getLatitude();
            mLongitude=bdLocation.getLongitude();

            Log.i("MYLOCATION111","经度"+mLatitude+"纬度"+mLongitude);



            LatLng latLng=new LatLng(mLatitude,mLongitude);
            MyApplication app = (MyApplication) getActivity().getApplication();
            app.setMyLatLng(latLng);
            Log.i("MYLOCATION","经度"+String.valueOf(app.getMyLatLng().latitude)+"纬度"+String.valueOf(app.getMyLatLng().longitude));
            MyLocationData data= new MyLocationData.Builder()
                    .direction(mCurrentX)//设定图标方向
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(mLatitude)//百度纬度坐标
                    .longitude(mLongitude)//百度经度坐标
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationData(data);
            //配置定位图层显示方式,三个参数的构造器
            /*
             * 1.定位图层显示模式
             * 2.是否允许显示方向信息
             * 3.用户自定义定位图标
             *
             * */
            MyLocationConfiguration configuration
                    =new MyLocationConfiguration(locationMode,false,null);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationConfigeration(configuration);
        }
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("jerry");
        broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter);
    }


    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            if ("yes".equals(change)) {
                // 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
                new Handler().post(new Runnable() {
                    public void run() {
                        setMarker();
                    }
                });
            }
        }
    };

    /**
     * 注销广播
     */
    @Override
    public void onDetach() {
        super.onDetach();
        broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
    }
}
