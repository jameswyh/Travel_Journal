package com.example.james_000.traveljournal.Home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.james_000.traveljournal.New.database.DetailDBHelper;
import com.example.james_000.traveljournal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment{
//    private static final String TAG = "HomeFragment";
    private DetailDBHelper mhelper;
    private SQLiteDatabase db;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) view.findViewById(R.id.home_listview);
        initview();
        return view;
    }

    public void initview(){
        mhelper=new DetailDBHelper(getActivity());
        db=mhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Detail",null);
        Map<String, Object> detailMap;
        List<Map<String, Object>> detailList = new ArrayList<>();
        while (cursor.moveToNext()){
            String caption = cursor.getString(cursor.getColumnIndex("Caption"));
            String path = cursor.getString(cursor.getColumnIndex("Path"));
            String address = cursor.getString(cursor.getColumnIndex("Address"));
            String time = cursor.getString(cursor.getColumnIndex("Time"));
            String id = cursor.getString(cursor.getColumnIndex("Id"));
            detailMap = new HashMap<>();
            detailMap.put("caption", caption);
            detailMap.put("path", path);
            detailMap.put("address", address);
            detailMap.put("time", time);
            detailMap.put("id", id);
            detailList.add(detailMap);
        }
        Collections.reverse(detailList);
        HomeAdapter adapter = new HomeAdapter(getActivity(), R.layout.home_listitem, detailList);
        //实现列表的显示
        listView.setAdapter(adapter);
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }
}
