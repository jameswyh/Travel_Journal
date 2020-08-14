package com.example.james_000.traveljournal.Search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.james_000.traveljournal.R;
import com.example.james_000.traveljournal.Search.Bean.SearchHistorysBean;
import com.example.james_000.traveljournal.Search.database.SearchHistorysDao;
import com.example.james_000.traveljournal.Utils.BottomNavigationViewHelper;
import com.example.james_000.traveljournal.application.MyApplication;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity  extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;

    public static final int UPDATE_TEXT = 1;
    public String neend = null;
    public String responseData;
    public String res;
    //声明控件
    private EditText et_search_keyword;
    private Button btn_search;
    private TextView tv_back;
    private TextView tv_clear;
    private LinearLayout ll_content;
    private ListView lv_history_word;
    //实现历史搜索记录
    private SearchHistorysDao dao;
    private ArrayList<SearchHistorysBean> historywordsList = new ArrayList<SearchHistorysBean>();
    private SearchHistoryAdapter mAdapter;
    private int count;

    double latitude,longitude;


    SearchEntity entity;
    Class<SearchEntity> entityClass = SearchEntity.class;


    protected Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    Log.e("responseData----",""+responseData);
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
                        String[] lons = new String[contentList.size()];
                        String[] lats = new String[contentList.size()];
                        String[] names = new String[contentList.size()];
                        String[] summarys = new String[contentList.size()];
                        String[] contents = new String[contentList.size()];
                        String[] attentions = new String[contentList.size()];
                        String[] opentimes = new String[contentList.size()];
                        String[] coupons = new String[contentList.size()];
                        String[] my_addresss = new String[contentList.size()];
                        String[] pic = new String[contentList.size()];
                        String lon1 = new String();
                        String lat1 = new String();
                        String kw = new String();
                        ArrayList<String[]> pictures = new ArrayList<String[]>(contentList.size());

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
                            if(picList1.size()!=0) {
                                pic[i] = picList1.get(0).getPicUrl();
                            }
                            List<picList> picList = contentlist_object.getPicList();
                            if(picList.size()!=0) {
                                String[] picturess = new String[picList.size()];
                                for (int j = 0; j < picList.size(); j++) {
                                    picturess[j] = picList.get(j).getPicUrl();
                                }
                                pictures.add(i, picturess);
                            }
                            else
                                pictures.add(i, null);

                        }
//                        lon1=String.valueOf(longitude);
//                        lat1=String.valueOf(latitude);
                        kw=et_search_keyword.getText().toString();


                        Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                        intent.putExtra("name", names);
                        intent.putExtra("summary", summarys);
                        intent.putExtra("my_address", my_addresss);
                        intent.putExtra("opentime", opentimes);
                        intent.putExtra("picture", pictures);
                        intent.putExtra("pic", pic);
                        intent.putExtra("coupon", coupons);
                        intent.putExtra("attention", attentions);
                        intent.putExtra("content", contents);
                        intent.putExtra("lon", lons);
                        intent.putExtra("lat", lats);
                        intent.putExtra("lon1", lon1);
                        intent.putExtra("lat1", lat1);
                        intent.putExtra("kw", kw);
                        intent.putExtra("res",responseData);
                        startActivity(intent);
                    }else {
                        Toast.makeText(SearchActivity.this,"NO RESULT",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Log.d(TAG, "onCreate: started.");

        MyApplication app = (MyApplication) getApplication();
        Log.i("LLLOCATION",""+app.getMyLatLng().longitude);


        dao = new SearchHistorysDao(this);
        historywordsList = dao.findAll();
        initView();
        setupBottomNavigationView();
    }

    private void initView(){
        et_search_keyword = (EditText) findViewById(R.id.et_search_keyword);

        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);

        ll_content = (LinearLayout) findViewById(R.id.llLayout);
        lv_history_word = (ListView) findViewById(R.id.lv_history_word);
        mAdapter = new SearchHistoryAdapter(historywordsList);
        count = mAdapter.getCount();
        if (count > 0) {

            tv_clear.setVisibility(View.VISIBLE);
        } else {
            tv_clear.setVisibility(View.GONE);
        }
        lv_history_word.setAdapter(mAdapter);
        lv_history_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SearchHistorysBean bean = (SearchHistorysBean) mAdapter.getItem(position);

                et_search_keyword.setText(bean.historyword);

                mAdapter.notifyDataSetChanged();
                btn_search.performClick();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_search:
                final String searchWord = et_search_keyword.getText().toString().trim();
                if (TextUtils.isEmpty(searchWord)) {
                    ToastUtils.show(this, "No Keyword");
                } else {
                    dao.addOrUpdate(searchWord);
                    historywordsList.clear();
                    historywordsList.addAll(dao.findAll());
                    mAdapter.notifyDataSetChanged();
                    tv_clear.setVisibility(View.VISIBLE);


                    final String address  =  "http://route.showapi.com/268-1?showapi_appid=85952&keyword="+ searchWord +"&proId=&cityId=&areaId=&page=&showapi_sign=c4fa6c4b153f4fc39b6ef270947e0d2d";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(address)
                                        .build();
                                Response response = client.newCall(request).execute();
                                responseData = response.body().string();
                                Log.i("res",responseData);
                                Message message = new Message();
                                message.what = UPDATE_TEXT;
                                mHandler.sendEmptyMessage(message.what);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }

                break;
            case R.id.tv_clear:
                dao.deleteAll();
                historywordsList.clear();
                mAdapter.notifyDataSetChanged();
                tv_clear.setVisibility(View.GONE);
                break;
            default:
                break;

        }
    }


    class SearchHistoryAdapter extends BaseAdapter {

        private ArrayList<SearchHistorysBean> historywordsList;

        public SearchHistoryAdapter(ArrayList<SearchHistorysBean> historywordsList) {
            this.historywordsList = historywordsList;
        }

        @Override
        public int getCount() {

            return historywordsList == null ? 0 : historywordsList.size();
        }

        @Override
        public Object getItem(int position) {

            return historywordsList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_history_word, null);
                holder.tv_word = (TextView) convertView.findViewById(R.id.tv_search_record);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_word.setText(historywordsList.get(position).toString());

            return convertView;
        }

    }

    class ViewHolder {
        TextView tv_word;
    }


    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);}
}

