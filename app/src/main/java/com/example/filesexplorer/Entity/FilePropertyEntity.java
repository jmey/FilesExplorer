package com.example.filesexplorer.Entity;

import com.example.filesexplorer.FileAndroid;

/**
 * Created by Dylan on 06/03/2015.
 */
public class FilePropertyEntity {

    private FileAndroid file;
    private double size;

    public FilePropertyEntity(FileAndroid _file) {
        this.file = _file;
    }
}
