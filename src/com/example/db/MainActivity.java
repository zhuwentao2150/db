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
			Person person = (Person) lView.getItemAtPosition(position);	// ��������ֵȡ�ü����е�ĳ��Ԫ��
			Toast.makeText(getApplicationContext(), person.getId().toString(), 1).show();
			
		}
	}
	
	// ʹ���Զ����Adapter
	private void show3() {
		List<Person> persons = personService.getScrollData(0, 30);
		PersonAdapter adapter = new PersonAdapter(this, persons, R.layout.item);
		listview.setAdapter(adapter);
	}

	// ʹ��SimpleCursorAdapter
	private void show2() {
		Cursor cursor = personService.getCursorScrollData(0, 40);
		// ʹ�����SimpleCursorAdapter����������Ҫ�����ݿ�����һ����_id���ֶΣ����û������ֶΣ���ô������������޷�����
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, 
				new String[]{"name", "phone","amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
		listview.setAdapter(adapter);
	}
	
	// ʹ��SimpleAdapter
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
		// ��һ�������������Ķ��󣬵ڶ�����������Ҫ�󶨵����ݣ�������������������Ҫ�󶨵���һ�������ϣ����ĸ��������󶨵��������ƣ�������������󶨵��������ƶ�Ӧ����ʾ�ؼ�
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, 
				new String[]{"name", "phone","amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
		listview.setAdapter(adapter);
	}

}
