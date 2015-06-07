package com.example.filesexplorer.AsyncTask;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.filesexplorer.Activity.MainActivity;
import com.example.filesexplorer.Utils.FileType;

import java.io.File;
import java.util.ArrayList;

public class FilesSearching {

    private FilesSearchingAsyncTask filesSearchingAsyncTask;

    private static class FileSearchingHolder {
        private static FilesSearching fileSearchingInstance;
    }

    private FilesSearching() {}

    public static FilesSearching getInstance() {
        if (FileSearchingHolder.fileSearchingInstance == null) {
            FileSearchingHolder.fileSearchingInstance = new FilesSearching();
        }
        return FileSearchingHolder.fileSearchingInstance;
    }

    public void searchFilesByAsyncTask(MainActivity activity, String pattern) {
        cancelSearching();
        filesSearchingAsyncTask = new FilesSearchingAsyncTask(activity, pattern);
        filesSearchingAsyncTask.execute();
    }

    public void searchFilesByType(MainActivity activity, int type) {
        cancelSearching();
        filesSearchingAsyncTask = new FilesSearchingAsyncTask(activity, type);
        filesSearchingAsyncTask.execute();
    }

    public void cancelSearching() {
        if (filesSearchingAsyncTask != null) {
            filesSearchingAsyncTask.cancel(true);
            filesSearchingAsyncTask = null;
        }
    }



    class FilesSearchingAsyncTask extends AsyncTask<Void, Integer, ArrayList<File>> {
        private MainActivity activity;
        private String pattern = "";
        private int type = -1;

        public FilesSearchingAsyncTask(MainActivity activity, String pattern) {
            this.activity = activity;
            this.pattern = pattern;
        }

        public FilesSearchingAsyncTask(MainActivity activity, int type) {
            this.activity = activity;
            this.type = type;
        }

        public void onPreExecute() {
            activity.showProgressBarSearching();
        }

        @Override
        protected ArrayList<File> doInBackground(Void... arg0) {

            ArrayList<File> res = new ArrayList<File>();
            ArrayList<File> nextDirectoryToSearch = new ArrayList<File>();
            if (type != -1) {
                nextDirectoryToSearch.add(Environment.getExternalStorageDirectory());
            } else {
                nextDirectoryToSearch.add(activity.getCurrentDirectory());
            }

            while(!nextDirectoryToSearch.isEmpty())
            {
                if (isCancelled()) { return null; }
                File directory = nextDirectoryToSearch.get(0);
                if(directory.listFiles() != null)
                {
                    for(File f : directory.listFiles())
                    {
                        if(f.isDirectory())
                        {
                            nextDirectoryToSearch.add(f);
                        }
                        else if(f.isFile())
                        {
                            if (type != -1 && FileType.isTypeFile(activity, f, type) || type == -1 && f.getName().toLowerCase().contains(pattern.toLowerCase())) {
                                res.add(f);
                            }
                        }
                    }
                }
                nextDirectoryToSearch.remove(0);
            }

            return res;
        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            if (files != null) {

                if (files.size() == 0) {
                    activity.showTextViewNoResultFound();
                } else {
                    activity.updateFragmentFiles(files);
                    activity.showFragmentFiles();
                }
            }
        }
    }
}
