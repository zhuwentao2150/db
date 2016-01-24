package com.example.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.domain.Person;
import com.example.service.DBOpenHelper;
import com.example.service.PersonService;

public class PersonServiceTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";
	
	public void testCreateDB() throws Exception {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
		dbOpenHelper.getWritableDatabase();	// 第一次调用这个方法就会自动创建数据库
	}
	
	public void testSave() throws Exception {
		PersonService service = new PersonService(this.getContext());
			Person person = new Person("zhuwentao", "15949585756", 100);
			service.save(person);
	}
	public void testDelete() throws Exception {
		PersonService service = new PersonService(this.getContext());
		service.delete(41);
	}
	public void testUpdate() throws Exception {
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		person.setName("hahaha");
		service.update(person);
	}
	public void testFind() throws Exception {
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		Log.i(TAG, person.toString());
	}
	public void testScrollData() throws Exception {
		PersonService service = new PersonService(this.getContext());
		List<Person> persons = service.getScrollData(40, 50);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	public void testCount() throws Exception {
		PersonService service = new PersonService(this.getContext());
		long result = service.getCount();
		Log.i(TAG, result+"");
	}
	public void testPayment() throws Exception {
		PersonService service = new PersonService(this.getContext());
		service.payment();
	}
	public void testUpdateAmount() throws Exception	{
		PersonService service = new PersonService(this.getContext());
		Person person1 = service.find(1);
		Person person2 = service.find(2);
		person1.setAmount(100);
		person2.setAmount(50);
		service.update(person1);
		service.update(person2);
	}
}
