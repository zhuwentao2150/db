package com.example.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.domain.Person;

public class PersonService {
	private DBOpenHelper dbOpenhelper;

	public PersonService(Context context) {
		this.dbOpenhelper = new DBOpenHelper(context);
	}
	
	public void payment() {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
		db.beginTransaction(); // ��������
		try{
			db.execSQL("update person set amount=amount-10 where personid=1");
			db.execSQL("update person set amount=amount+10 where personid=2");
			db.setTransactionSuccessful();	// ��������ı�־ΪTrue
		}finally{
			// ��һ���ϴ���������⣬Ҫȷ��������估ʱ������
			db.endTransaction();	// �������������������commit���ύ����rollback���ع���,
			// ������ύ��ع����������־�����ģ��������ı�־ΪTrue������ͻ��ύ������ع���Ĭ�����������ı�־ΪFalse
		}
	}
	
	
	// ����
	public void save(Person person) {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();	// ȡ�����ݿ����ʵ��
		// db.execSQL("insert into person(name, phone) values('"+ person.getName() + "','"+ person.getPhone() +"')");
		// ���ݿ��е�ֵΪ�û������ֵ������û������ֵΪ��li'min������ô���ݿ���ƴ��ʱ��ͻ�������⣬�������Ϸ����ǲ����õģ���������������ǲ���ռλ���ķ�ʽ
		
		db.execSQL("insert into person(name, phone, amount) values(?,?,?)", 
				new Object[]{person.getName(), person.getPhone(), person.getAmount()});
	}
	
	// ɾ��
	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
		db.execSQL("delete from person where personid=?",
				new Object[]{id});
	}
	
	// �޸�
	public void update(Person person) {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
		db.execSQL("update person set name=?,phone=?,amount=? where personid=?",
				new Object[]{person.getName(), person.getPhone(), person.getAmount(), person.getId()});
	}
	
	// ����id����
	public Person find(Integer id) {
		// getReadableDatabase()�����У����Ȼ᳢�Ե���getWritableDatabase()����������������ʱ������洢�ռ��Ѿ����ˣ���ô���޷�д���ˣ����Ż����getReadableDatabase����
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// ���ֻ�Ƕ����ݣ���ô�����ô˷���
		Cursor cursor = db.rawQuery("select * from person where personid=?", new String[]{id.toString()});
		if(cursor.moveToFirst()) {
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			return new Person(personid, name, phone, amount);
		}
		cursor.close();
		return null;
	}
	
	// ��ҳ��ʾ��offset��������������¼��maxResult����ȡ����������
	public List<Person> getScrollData(int offset, int maxResult) {
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// ���ֻ�Ƕ����ݣ���ô�����ô˷���
		Cursor cursor = db.rawQuery("select * from person order by personid asc limit ?,?", 
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		while(cursor.moveToNext()) {	// �ƶ�����һ����¼
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			persons.add(new Person(personid, name, phone, amount));
		}
		cursor.close();
		return persons;
	}
	// ����Cursor����ķ�ҳ����
	public Cursor getCursorScrollData(int offset, int maxResult) {
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// ���ֻ�Ƕ����ݣ���ô�����ô˷���
		Cursor cursor = db.rawQuery("select * from person order by personid asc limit ?,?", 
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		return cursor;
	}
	
	// ��ȡ��¼����
	public long getCount() {
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// ���ֻ�Ƕ����ݣ���ô�����ô˷���
		Cursor cursor = db.rawQuery("select count(*) from person", null);	// ������п϶���һ�����ݣ����һ��������һ��
		cursor.moveToFirst();	// ��Ϊֻ��һ����¼������ֻ���ƶ���ͷ���Ϳ�����
		long result = cursor.getLong(0);	// ֻ��һ���ֶΣ�����Ϊ0
		cursor.close();
		return result;
	}
}
