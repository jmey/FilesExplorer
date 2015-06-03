package com.example.filesexplorer.Utils;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public abstract class sortFileUtil {

    public static enum SortType { name, length, date, none };
    public static void triPar(SortType _type, List<File> _listFile )
    {
        if(_type == SortType.name)
        {
            Comparator<File> sortName = new Comparator<File>()
            {

                @Override
                public int compare(File _f1,  File _f2)
                {
                    if ( _f1==null)
                    {
                        if(_f2!=null) return -1;
                    }
                    if (_f2==null) return 1;

                    if(_f1.isDirectory() && !_f2.isDirectory()) return -1;
                    if (!_f1.isDirectory() && _f2.isDirectory()) return 1;

                    return _f1.getName().toUpperCase().compareTo(_f2.getName().toUpperCase());
                }
            };
            java.util.Collections.sort(_listFile, sortName);
        }
        else if(_type == SortType.length)
        {
            Comparator<File> sortLength = new Comparator<File>()
            {

                @Override
                public int compare(File _f1,  File _f2)
                {
                    if ( _f1==null)
                    {
                        if(_f2!=null) return -1;
                    }
                    if (_f2==null) return 1;

                    if(_f1.isDirectory() && !_f2.isDirectory()) return -1;
                    if (!_f1.isDirectory() && _f2.isDirectory()) return 1;

                    boolean resTest = _f1.length() < _f2.length();
                    int result = resTest ? 1 : -1;
                    return result;
                }
            };
            java.util.Collections.sort(_listFile, sortLength);
        }
        else if(_type == SortType.date)
        {
            Comparator<File> sortDate = new Comparator<File>()
            {
                @Override
                public int compare(File _f1,  File _f2)
                {
                    if ( _f1==null)
                    {
                        if(_f2!=null) return -1;
                    }
                    if (_f2==null) return 1;

                    if(_f1.isDirectory() && !_f2.isDirectory()) return -1;
                    if (!_f1.isDirectory() && _f2.isDirectory()) return 1;

                    Date f1Date = new Date(_f1.lastModified());
                    Date f2Date = new Date(_f2.lastModified());
                    boolean resTest = (f1Date.getTime() - f2Date.getTime()) < 0;
                    int result = resTest ? 1 : -1;
                    return result;
                }
            };
            java.util.Collections.sort(_listFile, sortDate);
        }
    }
}

