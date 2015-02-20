package com.example.fileexplorer;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterFileEntryRow extends BaseAdapter {

	private Context context;
	private ArrayList<Object> objectEntries;
//	final private static String TAG =  AdapterRepertory.class.getSimpleName();

	public AdapterFileEntryRow(Context context, ArrayList<Object> objectEntries) {
		this.context = context;
		this.objectEntries = objectEntries;
	}
	
	/**
	 * Returns the view for the current row
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Recycles the view (doesn't initialize it each time, but just modifies the content)
		if (convertView == null) {
			convertView = new FileEntryRow(context);
		}
		((FileEntryRow)convertView).update((FileAndroid)objectEntries.get(position));

		return convertView;
	}
	
	/**
	 * Gets the number of objects
	 */
	@Override
	public int getCount() {
		return this.objectEntries.size();
	}

	/**
	 * Gets the object in the position of the parameter
	 */
	@Override
	public Object getItem(int position) {
		return objectEntries.get(position);
	}

	/**
	 * Gets the id of the object in the position in parameter - not working
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Gets the number of different type available
	 */
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	public ArrayList<Object> getObjectEntries() {
		return objectEntries;
	}

	public void setObjectEntries(ArrayList<Object> objectEntries) {
		this.objectEntries = objectEntries;
	}
}
