package com.example.db;

import com.example.service.DBOpenHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/*
 * 一般URI由以下几部份组成，content://cn.zhuwentao.provider/person/10（scheme://主机名或authority/路径/10）
 * ContentProvider（内容提供者）的scheme已由Android所规定，scheme为：content://
 * 主机名（或叫Authority）用于唯一标识这个ContentProvider，外部调用者可以根据这个标识来找到它
 * 路径（path）可以用来表示我们要操作的数据，路径的构建应根据业务而定，如下
 * 要操作person表中id为10的记录，可以构建这样的路径：/person/10
 * 要操作person表中id为10的记录的name字段：person/10/name
 * 要操作person表中所有记录，可以构建这样的路径：/person
 * 要操作xxx表中的记录，可以构建这样的路径：/xxx
 * 当然要操作的数据不一定来自数据库，也可以是文件、xml或网络等其它存储方式，如下
 * 要操作xml文件中person节点下的name节点，可以构建这样的路径：/person/name
 * 如果要把一个字符串转换成Uri，可以使用Uri类中的parse()方法，如下
 * Uri uri = Uri.parse("content://cn.zhuwentao.provider/person")
 * 
 */


public class PersonProvider extends ContentProvider {
	private DBOpenHelper dbOpenHelper;
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);	// 里面的参数返回不匹配时的匹配数，NO_MATCH为系统建议的值，为-1
	private static final int PERSONS = 1;	// 匹配码标识
	private static final int PERSON = 2;	// 匹配码标识
	
	static {
		MATCHER.addURI("cn.zhuwentao.provider",	"person", PERSONS);	// 第三个参数为匹配码
		MATCHER.addURI("cn.zhuwentao.provider",	"person/#", PERSON);	// 第三个参数为匹配码
	}
	// 供外部应用往内容提供者中删除数据
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		switch(MATCHER.match(uri)) {
		case 1:
			num = db.delete("person", selection, selectionArgs);	// 删除所有的数据
			break;
		case 2:
			long rowid = ContentUris.parseId(uri);	// 得到路径后面的数字 
			String where = "personid=" + rowid;
			if(selection != null && !"".equals(selection.trim())){
				where += " and " + selection;
			}
			num = db.delete("person", where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("this is Unknow Uri:" + uri);
		}
		return num;
	}

	// 目前要操作数据的内容类型（txt：内容类型plain/text，html：内容类型html/text)
	public String getType(Uri uri) {
		switch (MATCHER.match(uri)) {
		case 1:
			return "vnd.android.cursor.dir/person";
		case 2:
			return "vnd.android.cursor.item/person";
		default:
			throw new IllegalArgumentException("this is Unknow Uri:" + uri);
		}
	}

	// 供外部应用往内容提供者中插入数据，第二个参数用来存储各个字段的值
	public Uri insert(Uri uri, ContentValues values) {
		// 要往数据库中操作数据，就要得到数据库的操作实例
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		switch (MATCHER.match(uri)) {	// 对用户传递进来的uri进行匹配
		case 1:	// 与1对应的匹配码
			long rowid = db.insert("person", "name", values); // 行号就是主键值，第二个参数如果传递的是null才调用
			// Uri insertUri  = Uri.parse("content://cn.zhuwentao.provider/person/"+ rowid);
			// 可以使用以下的工具类实现以上方法的功能，组拼路径
			Uri insertUri  = ContentUris.withAppendedId(uri, rowid);
			this.getContext().getContentResolver().notifyChange(uri, null);	// *****通过这个方法可以发出数据的变化通知*****
			return insertUri;
		default:	// 如果用户传递过来的uri不符合，则要给用户报错
			throw new IllegalArgumentException("this is Unknow Uri" + uri);
		}
	}

	// 实例被调用时才会执行
	public boolean onCreate() {
		dbOpenHelper = new DBOpenHelper(this.getContext());
		return true;
	}

	// 
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		switch (MATCHER.match(uri)) {
		case 1:
			// return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
			// return db.query("person", null, null, null, null, null, null);
			return db.rawQuery("select * from person", null);
		case 2:
			long rowid = ContentUris.parseId(uri);
			String where = "personid=" + rowid;
			if(selection != null && !"".equals(selection.trim())){
				where += " and " + selection;
			}
			return db.query("person", projection, where, selectionArgs, null, null, sortOrder);
		default:
			throw new IllegalArgumentException("this is Unknow Uri:" + uri);
		}
	}

	// 供外部应用往内容提供者中修改数据
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		switch (MATCHER.match(uri)) {
		case 1:
			num = db.update("person", values, selection, selectionArgs);
			break;
		case 2:
			long rowid = ContentUris.parseId(uri);
			String where = "personid=" + rowid;
			if(selection != null && !"".equals(selection.trim())){
				where += " and " + selection;
			}
			num = db.update("person", values, where, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("this is Unknow Uri:" + uri);
		}
		return num;
	}

}
