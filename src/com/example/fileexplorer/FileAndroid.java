package com.example.fileexplorer;

import java.io.File;
import java.util.Date;

public class FileAndroid {

	private File file;
	private long size;
	private int icon;
	private Date date;
	private boolean isParent;		// Peut-être à modifier ... sert à identifier le dossier nommé ".."
	
	public FileAndroid(File file) {
		this.file = file;
		this.size = file.length();
		this.date = new Date(file.lastModified());
		if (file.isDirectory()) {
			this.icon = R.drawable.icon_directory;
		} else {
			this.icon = 0;
		}
	}
	
	public FileAndroid(File file, boolean isParent) {
		this(file);
		this.isParent = isParent;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean isDirectory() {
		return file.isDirectory();
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
}
