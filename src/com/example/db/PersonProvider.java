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
 * һ��URI�����¼�������ɣ�content://cn.zhuwentao.provider/person/10��scheme://��������authority/·��/10��
 * ContentProvider�������ṩ�ߣ���scheme����Android���涨��schemeΪ��content://
 * �����������Authority������Ψһ��ʶ���ContentProvider���ⲿ�����߿��Ը��������ʶ���ҵ���
 * ·����path������������ʾ����Ҫ���������ݣ�·���Ĺ���Ӧ����ҵ�����������
 * Ҫ����person����idΪ10�ļ�¼�����Թ���������·����/person/10
 * Ҫ����person����idΪ10�ļ�¼��name�ֶΣ�person/10/name
 * Ҫ����person�������м�¼�����Թ���������·����/person
 * Ҫ����xxx���еļ�¼�����Թ���������·����/xxx
 * ��ȻҪ���������ݲ�һ���������ݿ⣬Ҳ�������ļ���xml������������洢��ʽ������
 * Ҫ����xml�ļ���person�ڵ��µ�name�ڵ㣬���Թ���������·����/person/name
 * ���Ҫ��һ���ַ���ת����Uri������ʹ��Uri���е�parse()����������
 * Uri uri = Uri.parse("content://cn.zhuwentao.provider/person")
 * 
 */


public class PersonProvider extends ContentProvider {
	private DBOpenHelper dbOpenHelper;
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);	// ����Ĳ������ز�ƥ��ʱ��ƥ������NO_MATCHΪϵͳ�����ֵ��Ϊ-1
	private static final int PERSONS = 1;	// ƥ�����ʶ
	private static final int PERSON = 2;	// ƥ�����ʶ
	
	static {
		MATCHER.addURI("cn.zhuwentao.provider",	"person", PERSONS);	// ����������Ϊƥ����
		MATCHER.addURI("cn.zhuwentao.provider",	"person/#", PERSON);	// ����������Ϊƥ����
	}
	// ���ⲿӦ���������ṩ����ɾ������
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		switch(MATCHER.match(uri)) {
		case 1:
			num = db.delete("person", selection, selectionArgs);	// ɾ�����е�����
			break;
		case 2:
			long rowid = ContentUris.parseId(uri);	// �õ�·����������� 
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

	// ĿǰҪ�������ݵ��������ͣ�txt����������plain/text��html����������html/text)
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

	// ���ⲿӦ���������ṩ���в������ݣ��ڶ������������洢�����ֶε�ֵ
	public Uri insert(Uri uri, ContentValues values) {
		// Ҫ�����ݿ��в������ݣ���Ҫ�õ����ݿ�Ĳ���ʵ��
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		switch (MATCHER.match(uri)) {	// ���û����ݽ�����uri����ƥ��
		case 1:	// ��1��Ӧ��ƥ����
			long rowid = db.insert("person", "name", values); // �кž�������ֵ���ڶ�������������ݵ���null�ŵ���
			// Uri insertUri  = Uri.parse("content://cn.zhuwentao.provider/person/"+ rowid);
			// ����ʹ�����µĹ�����ʵ�����Ϸ����Ĺ��ܣ���ƴ·��
			Uri insertUri  = ContentUris.withAppendedId(uri, rowid);
			this.getContext().getContentResolver().notifyChange(uri, null);	// *****ͨ������������Է������ݵı仯֪ͨ*****
			return insertUri;
		default:	// ����û����ݹ�����uri�����ϣ���Ҫ���û�����
			throw new IllegalArgumentException("this is Unknow Uri" + uri);
		}
	}

	// ʵ��������ʱ�Ż�ִ��
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

	// ���ⲿӦ���������ṩ�����޸�����
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
