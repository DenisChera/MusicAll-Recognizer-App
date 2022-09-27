package com.denis.music_rec.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.denis.music_rec.data.model.Colors;
import com.denis.music_rec.data.model.Music;
import com.denis.music_rec.data.model.MusicInfo;

import java.util.ArrayList;

public class DBManager {
	private DatabaseHelper myDBHelper = null;

	private DBManager(Context context) {
		if(myDBHelper == null) {
			myDBHelper = new DatabaseHelper(context);
		}
	}
	private static Context mycontext;
	public static void setContext(Context context) {
		mycontext = context.getApplicationContext();
	}
	private static DBManager s_instance = null;
	public static DBManager getManager() {
		if(s_instance == null) {
			s_instance = new DBManager(mycontext);
		}
		synchronized (s_instance) {
			return s_instance;
		}
	}

	public long insertMusicInfo(Music detail) {
		ContentValues val = new ContentValues();
		val.put(MusicInfo.TITLE, detail.getSongTitle());
		val.put(MusicInfo.ARTISTS, detail.getSongArtist());
		val.put(MusicInfo.RELEASE_DATE, detail.getSongReleaseDate());
		val.put(MusicInfo.GENRE, detail.getSongGenre());

		try {
			SQLiteDatabase database = myDBHelper.getWritableDatabase();
			return database.insert(MusicInfo.TABLENAME, null, val);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public long insertColors(Colors detail) {
	ContentValues val = new ContentValues();
	val.put(Colors.Colors, detail.color);


	try {
		SQLiteDatabase myDB = myDBHelper.getWritableDatabase();
		return myDB.insert(Colors.TABLENAME, null, val);
	} catch(Exception e) {
		e.printStackTrace();
	}
	return -1;
}

	public ArrayList<Integer> getColor(){
		SQLiteDatabase sqLiteDatabase = this.myDBHelper.getReadableDatabase();
		ArrayList<Integer>arrayList = new ArrayList<>();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Colors.TABLENAME, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			arrayList.add(cursor.getInt(1));
			cursor.moveToNext();
		}
		return arrayList;

	}


	public ArrayList<Music> getMusicList(){
		SQLiteDatabase myDB = myDBHelper.getReadableDatabase();
		ArrayList<Music> lstTasks = new ArrayList<>();
		Cursor cursor = myDB.query(MusicInfo.TABLENAME, new String[] {
				"id",
				MusicInfo.TITLE,
				MusicInfo.ARTISTS,
				MusicInfo.RELEASE_DATE,
				MusicInfo.GENRE
		}, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Music task = new Music();
			task.setId(cursor.getInt(0));
			task.setSongTitle(cursor.getString(1));
			task.setSongArtist(cursor.getString(2));
			task.setSongReleaseDate(cursor.getString(3));
			task.setSongGenre(cursor.getString(4));

			lstTasks.add(task);
			cursor.moveToNext();
		}
		cursor.close();
		//db.close();
		return lstTasks;
	}

	public void deleteMusicInfo(Music info) {
		SQLiteDatabase myDB = myDBHelper.getWritableDatabase();
		long ret = myDB.delete(MusicInfo.TABLENAME, MusicInfo.TITLE + "=?", new String[]{info.getSongTitle()});

		//db.close();
	}


	public void deleteDuplicate(String id) {
		SQLiteDatabase myDB = myDBHelper.getWritableDatabase();
		long res = myDB.delete(MusicInfo.TABLENAME, "id=?", new String[]{id});
		//db.close();
	}

	public void deleteItem(String id) {
		SQLiteDatabase myDB = myDBHelper.getWritableDatabase();
		long res = myDB.delete(MusicInfo.TABLENAME, "id=?", new String[]{id});
		if(res == -1){
			Toast.makeText(mycontext, "Failed to delete this item!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(mycontext, "Item successfully deleted!", Toast.LENGTH_SHORT).show();
		}
		//db.close();
	}

	public void deleteAllData() {
		SQLiteDatabase myDB = myDBHelper.getWritableDatabase();
		if(getMusicList().isEmpty()==true){
			Toast.makeText(mycontext, "The history is already empty!", Toast.LENGTH_SHORT).show();
		}
		else{
			myDB.execSQL("DELETE FROM " + MusicInfo.TABLENAME);
			Toast.makeText(mycontext, "All data has been deleted successfully!", Toast.LENGTH_SHORT).show();
		}

	}

	public void deleteAllColors() {
		SQLiteDatabase myDB = myDBHelper.getWritableDatabase();
		if(getColor().isEmpty()==false) {
			myDB.execSQL("DELETE FROM " + Colors.TABLENAME);
		}

	}

}
