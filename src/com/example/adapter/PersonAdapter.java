package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.db.R;
import com.example.domain.Person;

public class PersonAdapter extends BaseAdapter {
	private List<Person> persons;	// Ҫ�󶨵�����
	private int resource;	// �󶨵���Ŀ����
	private LayoutInflater inflater;
	
	public PersonAdapter(Context context, List<Person> persons, int resource){
		this.persons = persons;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	// ȡ��ϵͳ�ڲ��Ĳ���������
	}
	
	public int getCount() {
		return persons.size();
	}

	public Object getItem(int arg0) {
		return persons.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewCache cache = new ViewCache();
		if(arg1 == null) {	// �Ƿ�Ϊ��һ�δ���
			arg1 = inflater.inflate(resource, null);	// ָ��Ҫʹ����һ��XML�ļ������ɽ��棬������Ŀ�������
			cache.nameView = (TextView) arg1.findViewById(R.id.name);
			cache.phoneView = (TextView) arg1.findViewById(R.id.phone);
			cache.amountView = (TextView) arg1.findViewById(R.id.amount);
			arg1.setTag(cache);
		} else {
			cache = (ViewCache) arg1.getTag();
		}
		Person person = persons.get(arg0);	// ȡ��Ҫ�󶨵�����
		// �������ʵ�����ݰ�
		cache.nameView.setText(person.getName());
		cache.phoneView.setText(person.getPhone());
		cache.amountView.setText(person.getAmount().toString());
		return arg1;
	}
	
	private static  class ViewCache {
		TextView nameView;
		TextView phoneView;
		TextView amountView;
	}

}
