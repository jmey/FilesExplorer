package com.example.filesexplorer.Utils;

import com.example.filesexplorer.Enum.SortCriterion;
import com.example.filesexplorer.Enum.SortSense;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public abstract class FilesSorting {
	
    public static void sortBy(List<File> _listFile, final SortCriterion _criterion, SortSense sense) {

        final int sortingSense = sense == SortSense.ASC ? 1 : -1;
	
		Comparator<File> sortComparator = new Comparator<File>() {
			@Override
			public int compare(File _f1, File _f2) {
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
					result = _f1.getName().toLowerCase().compareTo(_f2.getName().toLowerCase());
				} else if (_criterion == SortCriterion.SIZE) {
                    if (_f1.length() < _f2.length()) {
                        result = 1;
                    } else if (_f1.length() > _f2.length()) {
                        result = -1;
                    } else {
                        result = 0;
                    }
				} else if (_criterion == SortCriterion.DATE) {
				    Date f1Date = new Date(_f1.lastModified());
                    Date f2Date = new Date(_f1.lastModified());
                    boolean resTest = (f1Date.getTime() - f2Date.getTime()) < 0;
                    result = resTest ? 1 : -1;
				}
				
				return sortingSense*result;
			}
		};
		
		java.util.Collections.sort(_listFile, sortComparator);
	}
}