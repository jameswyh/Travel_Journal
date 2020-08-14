package com.example.james_000.traveljournal.Likes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LikeDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "like.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Likes";

    public LikeDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, name text, res text, position text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}