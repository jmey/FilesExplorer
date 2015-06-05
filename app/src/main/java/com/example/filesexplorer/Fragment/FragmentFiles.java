package com.example.filesexplorer.Fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.filesexplorer.Activity.MainActivity;
import com.example.filesexplorer.Adapter.AdapterFileEntryRow;
import com.example.filesexplorer.Enum.DisplayMode;
import com.example.filesexplorer.R;

public class FragmentFiles extends Fragment {
//	final private static String TAG =  FragmentFiles.class.getSimpleName();
	
	private ArrayList<Object> objectEntries;
	private AdapterFileEntryRow adapter;
    private DisplayMode mode;
    private GridView gridView;
    private ListView listView;

    public FragmentFiles() {
        this.mode = DisplayMode.GRID;
    }

    public FragmentFiles(DisplayMode mode) {
        this.mode = mode;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.fragment_files, container, false);
		
		if (objectEntries != null) {
			adapter = new AdapterFileEntryRow(getActivity(), objectEntries, mode); // Create the adapter
            gridView = (GridView) inflatedView.findViewById(R.id.gridEntrees);
			listView = (ListView) inflatedView.findViewById(R.id.listEntrees); // Recover the listview

            if (mode == DisplayMode.GRID) {
                gridView.setAdapter(adapter);
                gridView.setVisibility(View.VISIBLE);
            } else if (mode == DisplayMode.LIST) {
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
            }
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

    public void setDisplayMode(DisplayMode mode) {
        if (objectEntries != null && this.mode != mode) {
            adapter = new AdapterFileEntryRow(getActivity(), objectEntries, mode);

            if (mode == DisplayMode.GRID) {
                gridView.setAdapter(adapter);
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else if (mode == DisplayMode.LIST) {
                listView.setAdapter(adapter);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            this.mode = mode;
        }
    }

    public void toggleDisplayMode() {
        if (objectEntries != null) {

            this.mode = mode == DisplayMode.GRID ? DisplayMode.LIST : DisplayMode.GRID;
            adapter = new AdapterFileEntryRow(getActivity(), objectEntries, mode);

            if (mode == DisplayMode.GRID) {
                gridView.setAdapter(adapter);
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else if (mode == DisplayMode.LIST) {
                listView.setAdapter(adapter);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

            if (getActivity() instanceof MainActivity) {
                ((MainActivity)getActivity()).toggleButtonDisplayMode(mode);
            }
        }
    }
}