package com.example.filesexplorer.Utils;

import com.example.filesexplorer.Enum.SortCriterion;
import com.example.filesexplorer.Enum.SortSense;
import com.example.filesexplorer.Model.FileAndroid;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public abstract class FilesSorting {

    public static void sortBy(ArrayList<Object> _listFile, final SortCriterion _criterion, SortSense sense) {

        final int sortingSense = sense == SortSense.ASC ? 1 : -1;

        Comparator<Object> sortComparator = new Comparator<Object>() {
            @Override
            public int compare(Object _of1, Object _of2) {
                FileAndroid _f1 = null, _f2 = null;
                if (_of1 instanceof FileAndroid && _of2 instanceof FileAndroid) {
                    _f1 = (FileAndroid)_of1;
                    _f2 = (FileAndroid)_of2;
                } else {
                    return -1;
                }

                if (_f1 == null && _f2 != null) {
                    return -sortingSense;
                }
                if (_f2 == null) {
                    return sortingSense;
                }

                int result = 0;

                if(_f1.isDirectory() && !_f2.isDirectory()) return -1;
                if (!_f1.isDirectory() && _f2.isDirectory()) return 1;

                if (_criterion == SortCriterion.FILENAME) {
                    result = _f1.getFile().getName().toLowerCase().compareTo(_f2.getFile().getName().toLowerCase());
                } else if (_criterion == SortCriterion.SIZE) {
                    if (_f1.getFile().length() < _f2.getFile().length()) {
                        result = 1;
                    } else if (_f1.getFile().length() > _f2.getFile().length()) {
                        result = -1;
                    } else {
                        result = 0;
                    }
                } else if (_criterion == SortCriterion.DATE) {
                    long f1DateTime = _f1.getFile().lastModified();
                    long f2DateTime = _f2.getFile().lastModified();

                    if (f1DateTime < f2DateTime) {
                        result = -1;
                    } else if (f1DateTime > f2DateTime) {
                        result = 1;
                    } else {
                        result = 0;
                    }
                }

                return sortingSense*result;
            }
        };

        java.util.Collections.sort(_listFile, sortComparator);
    }
}