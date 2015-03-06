package com.example.filesexplorer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DialogFragmentAbout extends DialogFragment {
	public static DialogFragmentAbout newInstance(int title) {
		DialogFragmentAbout dialog = new DialogFragmentAbout();
		Bundle args = new Bundle();
		args.putInt("title",  title);
		dialog.setArguments(args);
		return dialog;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dialog_about,  container, false);
		
		
		getDialog().setTitle(getArguments().getInt("title"));
		
		return v;
	}

}
