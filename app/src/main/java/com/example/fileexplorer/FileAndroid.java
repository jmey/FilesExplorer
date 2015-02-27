package com.example.fileexplorer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class FileAndroid {

	private File file;
	private long size;
	private int icon;
	private Date date;
	private boolean isParent;		// Peut-être à modifier ... sert à identifier le dossier nommé ".."
	private static HashMap<String, Integer> extensions;	// static pour ne garder qu'une seule instance 
	
	public FileAndroid(File file) {
		this.file = file;
		this.size = file.length();
		this.date = new Date(file.lastModified());
		
		if (file.isDirectory()) {
			this.icon = R.drawable.icon_directory;
		} else {
			if (extensions == null) {
				extensions = new HashMap<String, Integer>();
				extensions.put("pdf", R.drawable.icon_pdf);
				extensions.put("png", R.drawable.icon_jpg);
				extensions.put("jpg", R.drawable.icon_jpg);
				extensions.put("mp3", R.drawable.icon_mp3);
				extensions.put("apk", R.drawable.icon_apk);
				extensions.put("ogg", R.drawable.icon_ogg);
			}
			
			String fichier = file.getName();
			Integer icon = extensions.get(fichier.substring(fichier.lastIndexOf(".") + 1));
			if (icon != null) {
				this.icon = icon;
			} else {
				this.icon = R.drawable.ic_launcher;
			}
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
