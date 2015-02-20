package com.example.fileexplorer;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FileEntryRow extends RelativeLayout {
//	final private static String TAG =  FileEntryRow.class.getSimpleName();
	
	private RelativeLayout layoutItem;
	private Context context;

	private TextView fileNameTextView;
	private TextView fileSizeTextView;
	private ImageView fileIconView;
	private TextView dateTextView;
	private FileAndroid file;

	public FileEntryRow(Context context) {
		super(context);
		this.context = context;
		layoutItem = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.file_entry_row, this);
		initializeViews();
	}

	/**
	 * Recovers the different views
	 */
	private void initializeViews() {
		fileNameTextView = (TextView) layoutItem.findViewById(R.id.file_name);
		fileSizeTextView = (TextView) layoutItem.findViewById(R.id.file_size);
		fileIconView = (ImageView) layoutItem.findViewById(R.id.file_icon);
		dateTextView = (TextView) layoutItem.findViewById(R.id.file_date);
	}

	public void update(FileAndroid file) {
		this.file = file;
		displayPicture();
		displayFileName();
		displayFileSize();
		displayDate();
		setListeners();
	}

	/**
	 * Displays the picture
	 */
	private void displayPicture() {
		if (fileIconView != null) {
			if (file.getIcon() != 0) {
				fileIconView.setImageResource(file.getIcon());
			} else {
				fileIconView.setImageResource(R.drawable.ic_launcher);
			}
		}
	}
	
	private void displayFileName() {
		if (fileNameTextView != null) {
			if (file.isParent()) {
				fileNameTextView.setText("..");
			} else {
				fileNameTextView.setText(file.getFile().getName());
			}
		}
	}

	private void displayFileSize() {
		if (fileSizeTextView != null && !file.isDirectory()) {
			fileSizeTextView.setText(file.getSize() + " Ko");	// A modifier
		} else {
			fileSizeTextView.setText("");
		}
	}
	
	/**
	 * Displays the date
	 */
	private void displayDate() {
		if (dateTextView != null) {
			Date d = file.getDate();
			dateTextView.setText(d.toString());		// A modifier
		}
	}

	/**
	 * Sets the different listeners of this layout
	 */
	private void setListeners() {
		layoutItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Activity activity = (Activity)context;
				if (activity instanceof MainActivity) {
					if (file.isDirectory()) {
						((MainActivity)activity).openDirectory(file);
					} else {
						((MainActivity)activity).openFile(file);
					}
					
				}
			}
		});
	}
}
