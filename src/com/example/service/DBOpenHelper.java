package com.example.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "itcast.db", null, 3);	// <��>/databases/
	}

	@Override
	public void onCreate(SQLiteDatabase db) {	// ���ݿ��һ�α�������ʱ�����
		db.execSQL("CREATE TABLE person(" +
				"personid integer primary key autoincrement, " +
				"name varchar(20), " +
				"phone VARCHAR(20) NULL" +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {	// ���ݿ�İ汾�ŷ����仯��ʱ�򱻵���
		db.execSQL("ALTER TABLE person ADD amount integer");
	}

}
