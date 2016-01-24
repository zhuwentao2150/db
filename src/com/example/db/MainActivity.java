package com.example.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.adapter.PersonAdapter;
import com.example.domain.Person;
import com.example.service.PersonService;

public class MainActivity extends Activity {
	private ListView listview;
	
	private PersonService personService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		personService = new PersonService(this);
		
		listview = (ListView) this.findViewById(R.id.listview);
		listview.setOnItemClickListener(new ItemClickListener());
		// show();
		// show2();
		show3();
	}
	
	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long arg3) {
			ListView lView = (ListView) parent;
			Person person = (Person) lView.getItemAtPosition(position);	// 根据索引值取得集合中的某个元素
			Toast.makeText(getApplicationContext(), person.getId().toString(), 1).show();
			
		}
	}
	
	// 使用自定义的Adapter
	private void show3() {
		List<Person> persons = personService.getScrollData(0, 30);
		PersonAdapter adapter = new PersonAdapter(this, persons, R.layout.item);
		listview.setAdapter(adapter);
	}

	// 使用SimpleCursorAdapter
	private void show2() {
		Cursor cursor = personService.getCursorScrollData(0, 40);
		// 使用这个SimpleCursorAdapter适配器，需要在数据库中有一个“_id”字段，如果没有这个字段，那么这个适配器就无法工作
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, 
				new String[]{"name", "phone","amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
		listview.setAdapter(adapter);
	}
	
	// 使用SimpleAdapter
	private void show() {
		List<Person> persons = personService.getScrollData(0, 20);
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for(Person person : persons) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("name", person.getName());
			item.put("phone", person.getPhone());
			item.put("amount", person.getAmount());
			item.put("id", person.getId());
			data.add(item);
		}
		// 第一个参数：上下文对象，第二个参数：需要绑定的数据，第三个参数：数据需要绑定到哪一个界面上，第四个参数：绑定的数据名称，第五个参数：绑定的数据名称对应的显示控件
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, 
				new String[]{"name", "phone","amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
		listview.setAdapter(adapter);
	}

}
