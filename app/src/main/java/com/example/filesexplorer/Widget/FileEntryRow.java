package com.example.filesexplorer.Widget;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.filesexplorer.Activity.MainActivity;
import com.example.filesexplorer.Enum.DisplayMode;
import com.example.filesexplorer.Fragment.DialogFragmentProperties;
import com.example.filesexplorer.Interface.IObserver;
import com.example.filesexplorer.Model.FileAndroid;
import com.example.filesexplorer.AsyncTask.ProxyPicture;
import com.example.filesexplorer.R;
import com.example.filesexplorer.Utils.FileSizeFormater;

public class FileEntryRow extends RelativeLayout implements IObserver {
//	final private static String TAG =  FileEntryRow.class.getSimpleName();
	
	private RelativeLayout layoutItem;
	private Context context;

	private TextView fileNameTextView;
	private TextView fileSizeTextView;
	private ImageView fileIconView;
	private TextView dateTextView;
	private FileAndroid file;

	public FileEntryRow(Context context, DisplayMode mode) {
		super(context);
		this.context = context;

        if (mode == DisplayMode.GRID) {
            layoutItem = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.file_entry_grid, this);
        } else if (mode == DisplayMode.LIST) {
            layoutItem = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.file_entry_row, this);
        }

		initializeViews();
	}

	/**
	 * Recovers the different views
	 */
	private void initializeViews() {
		fileNameTextView = (TextView) layoutItem.findViewById(R.id.file_name);
		fileSizeTextView = (TextView) layoutItem.findViewById(R.id.file_size_label);
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
        if (file.isPicture()) {
            if (file.getBitmap() == null) {
                this.file.addObserver(this);
                ProxyPicture.getInstance().setPictureByAsyncTask(file);
            } else {
                this.file.removeObserver(this);
            }
        }
    }

	/**
	 * Displays the picture
	 */
	private void displayPicture() {
		if (fileIconView != null) {
            if (file.isPicture() && file.getBitmap() != null) {
                fileIconView.setImageBitmap(file.getBitmap());
            } else {
                fileIconView.setImageResource(file.getIcon());
            }
		}
	}
	
	private void displayFileName() {
		if (fileNameTextView != null) {
            fileNameTextView.setText(file.getFile().getName());
		}
	}

	private void displayFileSize() {
		if (fileSizeTextView != null) {
            if (!file.isDirectory()) {
                fileSizeTextView.setText(FileSizeFormater.format(file.getSize()));
            } else {
                fileSizeTextView.setText("");
            }
        }
	}
	
	/**
	 * Displays the date
	 */
	private void displayDate() {
		if (dateTextView != null) {
			SimpleDateFormat  simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
			dateTextView.setText(simpleFormat.format(file.getDate()));
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
        layoutItem.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Activity activity = (Activity)context;
                DialogFragmentProperties dialog = DialogFragmentProperties.newInstance(R.string.action_properties, file);
                dialog.show(activity.getFragmentManager(), "FragmentTransaction.add");
                return true;
            }
        });
    }

    @Override
    public void refresh() {
        displayPicture();
    }
}
