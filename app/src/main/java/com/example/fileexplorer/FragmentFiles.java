package com.example.fileexplorer;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentFiles extends Fragment {
//	final private static String TAG =  FragmentFiles.class.getSimpleName();
	
	private ArrayList<Object> objectEntries;
	private AdapterFileEntryRow adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.fragment_files, container, false);
		
		if (objectEntries != null) {
			adapter = new AdapterFileEntryRow(getActivity(), objectEntries); // Create the adapter
			ListView listView = (ListView) inflatedView.findViewById(R.id.listEntrees); // Recover the listview
			listView.setAdapter(adapter);
		}
		
		return inflatedView;
	}

	public void setEntriesList(ArrayList<Object> objectEntries) {
		this.objectEntries = objectEntries;
		if (adapter != null) {
			adapter.setObjectEntries(objectEntries);
		}
	}

	public AdapterFileEntryRow getAdapter() {
		return adapter;
	}
}
