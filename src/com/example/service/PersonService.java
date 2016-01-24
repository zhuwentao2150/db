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
		db.beginTransaction(); // 开启事务
		try{
			db.execSQL("update person set amount=amount-10 where personid=1");
			db.execSQL("update person set amount=amount+10 where personid=2");
			db.setTransactionSuccessful();	// 设置事务的标志为True
		}finally{
			// 万一以上代码出现例外，要确保以下语句及时被调用
			db.endTransaction();	// 结束事务，有两种情况：commit（提交），rollback（回滚）,
			// 事务的提交或回滚是由事务标志决定的，如果事务的标志为True，事务就会提交，否则回滚，默认情况下事务的标志为False
		}
	}
	
	
	// 增加
	public void save(Person person) {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();	// 取得数据库操作实例
		// db.execSQL("insert into person(name, phone) values('"+ person.getName() + "','"+ person.getPhone() +"')");
		// 数据库中的值为用户输入的值，如果用户输入的值为“li'min”，那么数据库组拼的时候就会产生问题，所以以上方法是不适用的，在这里我们最好是采用占位符的方式
		
		db.execSQL("insert into person(name, phone, amount) values(?,?,?)", 
				new Object[]{person.getName(), person.getPhone(), person.getAmount()});
	}
	
	// 删除
	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
		db.execSQL("delete from person where personid=?",
				new Object[]{id});
	}
	
	// 修改
	public void update(Person person) {
		SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
		db.execSQL("update person set name=?,phone=?,amount=? where personid=?",
				new Object[]{person.getName(), person.getPhone(), person.getAmount(), person.getId()});
	}
	
	// 根据id查找
	public Person find(Integer id) {
		// getReadableDatabase()方法中，首先会尝试调用getWritableDatabase()方法，当出现例外时（比如存储空间已经满了，那么就无法写入了），才会调用getReadableDatabase方法
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// 如果只是读数据，那么建议用此方法
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
	
	// 分页显示，offset：跳过多少条记录，maxResult：获取多少条数据
	public List<Person> getScrollData(int offset, int maxResult) {
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// 如果只是读数据，那么建议用此方法
		Cursor cursor = db.rawQuery("select * from person order by personid asc limit ?,?", 
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		while(cursor.moveToNext()) {	// 移动到下一条记录
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			persons.add(new Person(personid, name, phone, amount));
		}
		cursor.close();
		return persons;
	}
	// 返回Cursor对象的分页查找
	public Cursor getCursorScrollData(int offset, int maxResult) {
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// 如果只是读数据，那么建议用此方法
		Cursor cursor = db.rawQuery("select * from person order by personid asc limit ?,?", 
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		return cursor;
	}
	
	// 获取记录总数
	public long getCount() {
		SQLiteDatabase db = dbOpenhelper.getReadableDatabase();	// 如果只是读数据，那么建议用此方法
		Cursor cursor = db.rawQuery("select count(*) from person", null);	// 结果集中肯定有一条数据，最多一条，最少一条
		cursor.moveToFirst();	// 因为只有一条记录，所以只用移动到头部就可以了
		long result = cursor.getLong(0);	// 只有一个字段，所以为0
		cursor.close();
		return result;
	}
}
