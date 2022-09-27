package com.denis.music_rec.data.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.denis.music_rec.data.model.Colors;
import com.denis.music_rec.data.model.MusicInfo;


public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "music.db";
    private final static int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String strQuery1 = "CREATE TABLE IF NOT EXISTS " + MusicInfo.TABLENAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MusicInfo.TITLE + " TEXT, "
                + MusicInfo.ARTISTS + " TEXT, "
                + MusicInfo.RELEASE_DATE + " TEXT, "
                + MusicInfo.GENRE + " TEXT);";

        String strQuery2 = "CREATE TABLE IF NOT EXISTS " + Colors.TABLENAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Colors.Colors + " TEXT);";
        db.execSQL(strQuery1);
        db.execSQL(strQuery2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        onCreate(db);
    }

}
