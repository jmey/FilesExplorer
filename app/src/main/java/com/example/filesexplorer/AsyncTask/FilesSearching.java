package com.example.filesexplorer.AsyncTask;

import android.os.AsyncTask;

import com.example.filesexplorer.Activity.MainActivity;

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
        if (filesSearchingAsyncTask != null) {
            filesSearchingAsyncTask.cancel(true);
            filesSearchingAsyncTask = null;
        }

        filesSearchingAsyncTask = new FilesSearchingAsyncTask(activity, pattern);
        filesSearchingAsyncTask.execute();
    }

    class FilesSearchingAsyncTask extends AsyncTask<Void, Integer, ArrayList<File>> {
        private MainActivity activity;
        private String pattern;

        public FilesSearchingAsyncTask(MainActivity activity, String pattern) {
            this.activity = activity;
            this.pattern = pattern;
        }

        @Override
        protected ArrayList<File> doInBackground(Void... arg0) {

            if(!activity.getCurrentDirectory().isDirectory()) {
                return null;
            }

            ArrayList<File> res = new ArrayList<File>();
            ArrayList<File> nextDirectoryToSearch = new ArrayList<File>();
            nextDirectoryToSearch.add(activity.getCurrentDirectory());

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
                        else if(f.isFile() && f.getName().toLowerCase().contains(pattern.toLowerCase()))
                        {
                            res.add(f);
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
                activity.updateFragmentFiles(files, false);
            }
        }
    }
}
