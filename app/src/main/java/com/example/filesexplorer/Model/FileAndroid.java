package com.example.filesexplorer.Model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.filesexplorer.Interface.IObservable;
import com.example.filesexplorer.Interface.IObserver;
import com.example.filesexplorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FileAndroid implements IObservable {
    private File file;
    private long size;
    private int icon;
    private Bitmap bitmap;
    private Date date;
    private String extension;
    private boolean isBackDirectory;        // Peut-être à modifier ... sert à identifier le dossier nommé de retour
    private static HashMap<String, Integer> extensions;    // static pour ne garder qu'une seule instance
    private ArrayList<IObserver> observers;
    private static Context context;
    private static List<String> typesPicture;
    private static List<String> typesMusic;
    private static List<String> typesVideo;
    private static List<String> typesArchive;
    private static List<String> typesDocument;
    private static List<String> typesSpreadsheet;
    private static List<String> typesPresentation;

    public FileAndroid(Context context, File file) {
        this.file = file;
        this.size = file.length();
        this.date = new Date(file.lastModified());
        this.observers = new ArrayList<IObserver>();
        this.context = context;

        if (file.isDirectory()) {
            this.icon = R.drawable.icon_directory;
            extension = "";
        } else {
            if (extensions == null && context != null) {
                extensions = new HashMap<String, Integer>();
                String[] icons = context.getResources().getStringArray(R.array.list_icons);
                int resID;
                for (String icon : icons) {
                    String[] split = icon.split(";");
                    try {
                        resID = context.getResources().getIdentifier(split[1], "drawable", context.getPackageName());
                        extensions.put(split[0], resID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            String fichier = file.getName();
            extension = fichier.substring(fichier.lastIndexOf(".") + 1);
            Integer icon = extensions.get(extension);
            if (icon != null) {
                this.icon = icon;
            } else {
                this.icon = R.drawable.icon_unknown;
            }
        }
    }

    public FileAndroid(Context context, File file, boolean isBackDirectory) {
        this(context, file);
        this.isBackDirectory = isBackDirectory;
        this.icon = R.drawable.icon_back;
    }

    public boolean isPicture() {
        if (typesPicture == null) {
            typesPicture = Arrays.asList(context.getResources().getStringArray(R.array.list_types_picture));
        }
        return typesPicture.contains(extension);
    }

    public boolean isMusic() {
        if (typesMusic == null) {
            typesMusic = Arrays.asList(context.getResources().getStringArray(R.array.list_types_music));
        }
        return typesMusic.contains(extension);
    }

    public boolean isVideo() {
        if (typesVideo == null) {
            typesVideo = Arrays.asList(context.getResources().getStringArray(R.array.list_types_video));
        }
        return typesVideo.contains(extension);
    }

    public boolean isDocument() {
        if (typesDocument == null) {
            typesDocument = Arrays.asList(context.getResources().getStringArray(R.array.list_types_document));
        }
        return typesDocument.contains(extension);
    }

    public boolean isPresentation() {
        if (typesPresentation == null) {
            typesPresentation = Arrays.asList(context.getResources().getStringArray(R.array.list_types_presentation));
        }
        return typesPresentation.contains(extension);
    }

    public boolean isArchive() {
        if (typesArchive == null) {
            typesArchive = Arrays.asList(context.getResources().getStringArray(R.array.list_types_archive));
        }
        return typesArchive.contains(extension);
    }

    public boolean isSpreadsheet() {
        if (typesSpreadsheet == null) {
            typesSpreadsheet = Arrays.asList(context.getResources().getStringArray(R.array.list_types_spreadsheet));
        }
        return typesSpreadsheet.contains(extension);
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        notifyObservers();
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

    public ArrayList<IObserver> getObservers() {
        return observers;
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.refresh();
        }
    }
}
