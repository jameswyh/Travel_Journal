package com.example.james_000.traveljournal.Likes.database;

import android.content.ContentValues;
import android.content.Context;

public class LikeDao {

    private Context context;
    private LikeDBHelper likesDBHelper;

    public LikeDao(Context context) {
        this.context = context;
        likesDBHelper = new LikeDBHelper(context);
    }
    public void insert(String name, String res, int position) {
        ContentValues cv=new ContentValues();
        cv.put("name", name);
        cv.put("res", res);
        cv.put("position", position);
        // 开启事务
        likesDBHelper.getWritableDatabase().beginTransaction();
        try{
            likesDBHelper.getWritableDatabase().insert("Likes", "name", cv);
            likesDBHelper.getWritableDatabase().setTransactionSuccessful();
        }
        catch(Exception ex){
        } finally {
            likesDBHelper.getWritableDatabase().endTransaction();
        }
    }
}
