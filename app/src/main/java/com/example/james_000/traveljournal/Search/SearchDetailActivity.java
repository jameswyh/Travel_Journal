package com.example.james_000.traveljournal.Search;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import com.example.james_000.traveljournal.Likes.database.LikeDBHelper;
import com.example.james_000.traveljournal.Likes.database.LikeDao;
import com.example.james_000.traveljournal.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchDetailActivity extends AppCompatActivity {
    private SliderLayout mSlider;
    private Button navi_btn;
    private Button like_btn;
    private String nametext;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ScrollView mScrollView;
    LatLng mylocation;
    String kw;
    String position;
    String res;
    private LikeDao mdao;
    private LikeDBHelper mhelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdetail);
        mSlider = (SliderLayout) findViewById(R.id.slider);
        navi_btn = (Button) findViewById(R.id.btn_navi);
        like_btn = (Button) findViewById(R.id.btn_like);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mdao = new LikeDao(this);
        mhelper=new LikeDBHelper(this);
        db=mhelper.getReadableDatabase();
        List<Map<String, Object>> profilesList = new ArrayList<>();
        Intent intent = getIntent();
        ArrayList<String[]> picture = (ArrayList<String[]>) intent.getSerializableExtra("picture");
        String[] value = intent.getStringArrayExtra("name");
        String[] summary = intent.getStringArrayExtra("summary");
        String[] my_address = intent.getStringArrayExtra("my_address");
        String[] opentime = intent.getStringArrayExtra("opentime");
        String[] coupon = intent.getStringArrayExtra("coupon");
        String[] attention = intent.getStringArrayExtra("attention");
        String[] content = intent.getStringArrayExtra("content");
        String[] lat = intent.getStringArrayExtra("lat");
        String[] lon = intent.getStringArrayExtra("lon");
        position = intent.getStringExtra("position");
        kw = intent.getStringExtra("kw");
        res = intent.getStringExtra("res");
        mylocation = (LatLng) intent.getParcelableExtra("mylocation");
        int p = Integer.valueOf(position);
        final TextView txt_name = (TextView) this.findViewById(R.id.text_name);
        nametext = new String();
        nametext+=value[p];
        txt_name.setText(nametext);

        final TextView txt_open = (TextView) this.findViewById(R.id.text_opentime);
        String opentext = new String();
        opentext+=opentime[p];
        txt_open.setText(replaceBlank(Html.fromHtml(opentext).toString()));
        if(opentext.equals("null")||opentext.equals("暂无")||opentext.equals("")){
            RelativeLayout openlayout = findViewById(R.id.openLayout);
            openlayout.setVisibility(View.GONE);
        }

        final TextView txt_address = (TextView) this.findViewById(R.id.text_address);
        String addresstext = new String();
        addresstext+=my_address[p];
        txt_address.setText(replaceBlank(Html.fromHtml(addresstext).toString()));
        if(addresstext.equals("null")||addresstext.equals("暂无")||addresstext.equals("")){
            RelativeLayout addresslayout = findViewById(R.id.addressLayout);
            addresslayout.setVisibility(View.GONE);
        }

        final TextView txt_summary = (TextView) this.findViewById(R.id.text_summary);
        String summarytext = new String();
        summarytext+=content[p];
        txt_summary.setText(replaceBlank(Html.fromHtml(summarytext).toString()));
        if(summarytext.equals("null")||summarytext.equals("暂无")||summarytext.equals("")){
            RelativeLayout summarylayout = findViewById(R.id.summaryLayout);
            summarylayout.setVisibility(View.GONE);
        }


        final TextView txt_coupon = (TextView) this.findViewById(R.id.text_coupon);
        String coupontext = new String();
        coupontext+=coupon[p];
        txt_coupon.setText(replaceBlank(Html.fromHtml(coupontext).toString()));
        if(coupontext.equals("null")||coupontext.equals("暂无")||coupontext.equals("")){
            RelativeLayout couponlayout = findViewById(R.id.couponLayout);
            couponlayout.setVisibility(View.GONE);
        }

        HashMap<String,String> urlMaps = new HashMap<>();
        if(picture.get(p)!=null)
            for (int i = 0; i < picture.get(p).length; i++) {
                urlMaps.put(nametext + i, picture.get(p)[i]);
            }


        for (String name : urlMaps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)//描述
                    .image(urlMaps.get(name))//image方法可以传入图片url、资源id、File
                    .setScaleType(BaseSliderView.ScaleType.Fit)//图片缩放类型
                    .setOnSliderClickListener(onSliderClickListener);//图片点击
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);//传入参数
            mSlider.addSlider(textSliderView);//添加一个滑动页面
        }

        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);//滑动动画
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认指示器样式
        mSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator2));//自定义指示器
        mSlider.setCustomAnimation(new DescriptionAnimation());//设置图片描述显示动画
        mSlider.setDuration(4000);//设置滚动时间，也是计时器时间
        mSlider.addOnPageChangeListener(onPageChangeListener);


        navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent();
                // 驾车路线规划
                i1.setData(Uri.parse("baidumap://map/direction?region=shanghai&destination="+nametext+"&mode=driving"));
                if (isApplicationAvilible(SearchDetailActivity.this,"com.baidu.BaiduMap.customization")||isApplicationAvilible(SearchDetailActivity.this,"com.baidu.BaiduMap")) {
                    startActivity(i1);
                } else {
                    Toast.makeText(SearchDetailActivity.this, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
                    Intent loction = new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.map.baidu.com/direction?origin=latlng:"+mylocation.latitude+","+mylocation.longitude+"|name:我的位置&destination="+nametext+"&mode=driving&region=上海&output=html"));
                    startActivity(loction);
                }
            }
        });

        final Drawable dislike = getResources().getDrawable(R.drawable.ic_dislike_red);
        dislike.setBounds(0, 0, dislike.getMinimumWidth(),dislike.getMinimumHeight());
        final Drawable like = getResources().getDrawable(R.drawable.ic_like_red);
        like.setBounds(0, 0, like.getMinimumWidth(),like.getMinimumHeight());

        int count = 0;
        Cursor cursor = db.rawQuery("SELECT count(*) FROM Likes WHERE name ='"+nametext+"'", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        if(count!=0){
            like_btn.setCompoundDrawables(null, like, null, null);
        }


        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                Cursor cursor = db.rawQuery("SELECT count(*) FROM Likes WHERE name ='"+nametext+"'", null);
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
                cursor.close();
                if(count==0){


                    mdao.insert(nametext,res, Integer.valueOf(position));
                    Toast.makeText(SearchDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    like_btn.setCompoundDrawables(null, like, null, null);
                }
                else{

                    db.delete("Likes", "name = ?", new String[]{nametext});
                    Toast.makeText(SearchDetailActivity.this, "收藏已取消", Toast.LENGTH_SHORT).show();
                    like_btn.setCompoundDrawables(null, dislike, null, null);
                }

            }
        });


        //获取地图控件引用
        mMapView= (MapView) findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(Double.valueOf(lat[p]),
                Double.valueOf(lon[p]))).zoom(17.0f);//地图缩放级别
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus arg0, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);

        OverlayOptions option = new MarkerOptions()
                .position(new LatLng(Double.valueOf(lat[p]),
                        Double.valueOf(lon[p])))
                .icon(bitmap);

        mBaiduMap.addOverlay(option);

        //滑动冲突
        View v = mMapView.getChildAt(0);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
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

    private BaseSliderView.OnSliderClickListener onSliderClickListener=new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
//            Toast.makeText(SearchDetailActivity.this,slider.getBundle().get("extra") + "",
//                    Toast.LENGTH_SHORT).show();
        }
    };

    //页面改变监听
    private ViewPagerEx.OnPageChangeListener onPageChangeListener=new ViewPagerEx.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            Log.d("ansen", "Page Changed: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    public static String replaceBlank(String src) {
        String dest = "";
        if (src != null) {
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(src);
            dest = matcher.replaceAll("");
        }
        return dest;
    }

    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }
}