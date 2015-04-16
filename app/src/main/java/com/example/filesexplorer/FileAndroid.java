package com.example.filesexplorer;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class FileAndroid {

	private File file;
	private long size;
	private int icon;
	private Date date;
    private String extension;
	private boolean isBackDirectory;		// Peut-être à modifier ... sert à identifier le dossier nommé de retour
	private static HashMap<String, Integer> extensions;	// static pour ne garder qu'une seule instance 
	
	public FileAndroid(File file) {
		this.file = file;
		this.size = file.length();
		this.date = new Date(file.lastModified());
		
		if (file.isDirectory()) {
            this.icon = R.drawable.icon_directory;
            extension = "";
		} else {
			if (extensions == null) {
				extensions = new HashMap<String, Integer>();
				// Lit le fichier et ajoute
				// TODO : A sortir et mettre dans un fichier
				extensions.put("pdf", R.drawable.icon_pdf);
				extensions.put("png", R.drawable.icon_jpg);
				extensions.put("jpg", R.drawable.icon_jpg);
				extensions.put("mp3", R.drawable.icon_mp3);
				extensions.put("apk", R.drawable.icon_apk);
				extensions.put("ogg", R.drawable.icon_ogg);
			}
            String fichier = file.getName();
			extension = fichier.substring(fichier.lastIndexOf(".") + 1);
			Integer icon = extensions.get(extension);
			if (icon != null) {
				this.icon = icon;
			} else {
                this.icon = R.drawable.ic_launcher;
            }
		}
	}
	
	public FileAndroid(File file, boolean isBackDirectory) {
		this(file);
        this.isBackDirectory = isBackDirectory;
        this.icon = R.drawable.icon_back;
	}

    public boolean isPicture() {
        return getExtension().equals("png") || getExtension().equals("jpg");
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

	public boolean isBackDirectory() {
		return isBackDirectory;
	}

	public void setBackDirectory(boolean isBackDirectory) {
		this.isBackDirectory = isBackDirectory;
	}

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
