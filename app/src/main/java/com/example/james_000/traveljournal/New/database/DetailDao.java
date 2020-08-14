package com.example.james_000.traveljournal.New.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.james_000.traveljournal.Search.Bean.SearchHistorysBean;

import java.util.ArrayList;

public class DetailDao {
    // 列定义
    private final String[] DETAIL_COLUMNS = new String[] {"Id", "Caption","Path","Address","Lat","Lon","Time"};

    private Context context;
    private DetailDBHelper detailsDBHelper;

    public DetailDao(Context context) {
        this.context = context;
        detailsDBHelper = new DetailDBHelper(context);
    }
    public void insert(String Caption, String Path, String Address, double Lat, double Lon, String Time) {
        ContentValues cv=new ContentValues();
        cv.put("Caption", Caption);
        cv.put("Path", Path);
        cv.put("Address", Address);
        cv.put("Lat", Lat);
        cv.put("Lon", Lon);
        cv.put("Time", Time);

        // 开启事务
        detailsDBHelper.getWritableDatabase().beginTransaction();
        try{
            detailsDBHelper.getWritableDatabase().insert("Detail", "Caption", cv);
            detailsDBHelper.getWritableDatabase().setTransactionSuccessful();
        }
        catch(Exception ex){
        } finally {
        detailsDBHelper.getWritableDatabase().endTransaction();
        }
    }

//    public ArrayList<SearchHistorysBean> findAll(){
//        ArrayList<SearchHistorysBean> data = new ArrayList<SearchHistorysBean>();;
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.query("t_historywords", null, null, null, null, null, "updatetime desc");
//        while(cursor.moveToNext()){
//            SearchHistorysBean searchDBData = new SearchHistorysBean();
//            searchDBData._id =cursor.getInt(cursor.getColumnIndex("_id"));
//            searchDBData.historyword = cursor.getString(cursor.getColumnIndex("historyword"));
//            searchDBData.updatetime = cursor.getLong(cursor.getColumnIndex("updatetime"));
//            data.add(searchDBData);
//        }
//        cursor.close();
//        db.close();
//        return data;
//    }


}
