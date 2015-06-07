package com.example.filesexplorer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filesexplorer.AsyncTask.FilesSearching;
import com.example.filesexplorer.Enum.SortCriterion;
import com.example.filesexplorer.Widget.AlertDialogSortingFiles;
import com.example.filesexplorer.Enum.DisplayMode;
import com.example.filesexplorer.Model.FileAndroid;
import com.example.filesexplorer.Fragment.DialogFragmentAbout;
import com.example.filesexplorer.Fragment.FragmentFiles;
import com.example.filesexplorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int RESULT_SETTINGS = 1;
	
//	private final String TAG = getClass().getSimpleName();
    private MainActivity activity;
	private static FragmentFiles fragmentFiles;
    private static File currentDirectory;
    private Menu menu;

    // UI
    private TextView textViewChemin;
    private TextView textViewNoResultFound;
    private ProgressBar progressBarSearching;
    private FrameLayout frameLayout;

    private AlertDialogSortingFiles alertDialogSorting = null;

    private boolean isHiddenFileShowed;

    /** If the activity is called by another application to get a string file path result, mode_library = true
     *  If the explorer is called by itself, mode_library is at false. False by default
     **/
    private boolean mode_library = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        progressBarSearching = (ProgressBar)findViewById(R.id.progress_bar_searching);
        frameLayout = (FrameLayout)findViewById(R.id.fragment_container_files);
        textViewNoResultFound = (TextView) findViewById(R.id.textview_noresultfound);
        textViewChemin = (TextView) findViewById(R.id.textViewChemin);

        if (currentDirectory == null) {
            // Get the last directory saved in the preferences file
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            currentDirectory = new File(settings.getString("directory", Environment.getExternalStorageDirectory().getAbsolutePath()));
        }

        // Init the alertDialog with the sorting list
        alertDialogSorting = new AlertDialogSortingFiles(this, getResources().getStringArray(R.array.list_sorting_values));

        // Get the property to know if the preference is checked or not
        isHiddenFileShowed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_hidden_file", false);

        // Instantiate and add the fragment of files
        if (fragmentFiles == null) {
            fragmentFiles = new FragmentFiles();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_files, fragmentFiles);
        transaction.commit();

        // Fill the fragment with the files of the currentDirectory
        openDirectory(currentDirectory);

        // If a search is running, it cancels
        FilesSearching.getInstance().cancelSearching();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        if(i!= null) {
            mode_library = i.getBooleanExtra("mode", false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)itemSearch.getActionView();

        // Listener to search files at each character entered
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    FilesSearching.getInstance().searchFilesByAsyncTask(activity, newText);
                } else {
                    openDirectory(currentDirectory);
                    showFragmentFiles();
                    FilesSearching.getInstance().cancelSearching();
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!currentDirectory.getPath().equals("/")) {
                openDirectory(currentDirectory.getParentFile());
            }
        } else if (id == R.id.action_about) {
            DialogFragmentAbout dialog = DialogFragmentAbout.newInstance(R.string.action_about);
            dialog.show(getFragmentManager(), "FragmentTransaction.add");
        } else if (id == R.id.action_settings) {
            Intent i = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        } else if (id == R.id.action_sorting) {
            alertDialogSorting.show();
        } else if (id == R.id.action_sorting_sense) {
            fragmentFiles.reverseSortingSense();
        } else if (id == R.id.action_display) {
            fragmentFiles.toggleDisplayMode();
        } else if (id == R.id.action_search_all_documents) {

        } else if (id == R.id.action_search_all_images) {

        } else if (id == R.id.action_search_all_musics) {

        } else if (id == R.id.action_search_all_videos) {

        } else {
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SETTINGS:
                boolean prefHiddenFileShowed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_hidden_file", false);
                if (prefHiddenFileShowed != isHiddenFileShowed) {
                    isHiddenFileShowed = prefHiddenFileShowed;
                    openDirectory(currentDirectory);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            openDirectory(currentDirectory.getParentFile());
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.quit_confirmation)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }

    public void openDirectory(File file) {
        File[] arrayFiles = file.listFiles();

        if (arrayFiles != null) {
            currentDirectory = file;

            ArrayList<File> files = new ArrayList<>();
            Collections.addAll(files, arrayFiles);
            updateFragmentFiles(files);

            if (getActionBar() != null) {
                if (currentDirectory.getPath().equals("/")) {
                    getActionBar().setHomeButtonEnabled(false);
                    getActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    getActionBar().setHomeButtonEnabled(true);
                    getActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        } else {
            Toast.makeText(this, "Cannot open this directory for some reason", Toast.LENGTH_SHORT).show();
        }
    }

    // Update the FragmentFiles with the ArrayList of File in parameter
    // If hasParent is at true, the first element of the ArrayList is this parent
    public void updateFragmentFiles(ArrayList<File> files) {
        try {
            ArrayList<Object> objects = new ArrayList<>();

            for (File file : files) {
                if (isHiddenFileShowed || !file.isHidden()) {
                    objects.add(new FileAndroid(this, file));
                }
            }

            fragmentFiles.setEntriesList(objects, true);
            if (fragmentFiles.getAdapter() != null) {
                fragmentFiles.getAdapter().notifyDataSetChanged();
            }

            TextView textChemin  = (TextView) findViewById(R.id.textViewChemin);
            if(textChemin != null) {
                // Si la racine ne pas mettre de '/' derrière le nom du dossier
                textChemin.setText( currentDirectory.getPath().equals("/") ?
                        currentDirectory.getPath() :
                        currentDirectory.getPath()+"/");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception...", Toast.LENGTH_SHORT).show();
        }
    }

    public void sortFragmentFilesBy(SortCriterion sortCriterion) {
        fragmentFiles.sortBy(sortCriterion);
    }

    public void openFile(FileAndroid fileAndroid) {
        // Save the latest directory opened
        String parentDirectory = fileAndroid.getFile().getParentFile().getAbsolutePath();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("directory", parentDirectory);
        editor.commit();

        // Action
        if(this.mode_library) {
            Intent intent = new Intent();
            intent.putExtra("file", fileAndroid.getFile().getAbsolutePath());
            MainActivity.this.setResult(RESULT_OK, intent);
            MainActivity.this.finish();
        } else {
            File file = fileAndroid.getFile();
            if (file.exists()) {
                FileAndroid fileToOpen = new FileAndroid(this, file);
                String extension = fileToOpen.getExtension();
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                boolean lanceIntent = true;

                if (extension != null) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String sMime = mime.getMimeTypeFromExtension(extension);
                    if (sMime != null) {
                        target.setDataAndType(Uri.fromFile(fileToOpen.getFile()), sMime);
                    } else {
                        lanceIntent = false;
                    }
                } else {
                    lanceIntent = false;
                }

                if (lanceIntent) {
                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                } else {
                    Toast.makeText(this, R.string.not_handle, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showFragmentFiles() {
        progressBarSearching.setVisibility(View.GONE);
        textViewNoResultFound.setVisibility(View.GONE);

        textViewChemin.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void showProgressBarSearching() {
        progressBarSearching.setVisibility(View.VISIBLE);

        textViewChemin.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        textViewNoResultFound.setVisibility(View.GONE);
    }

    public void showTextViewNoResultFound() {
        progressBarSearching.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        textViewChemin.setVisibility(View.GONE);

        textViewNoResultFound.setVisibility(View.VISIBLE);
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public void toggleButtonDisplayMode(DisplayMode mode) {
        if (mode == DisplayMode.LIST) {
            menu.findItem(R.id.action_display).setIcon(R.drawable.icon_grid);
        } else if (mode == DisplayMode.GRID) {
            menu.findItem(R.id.action_display).setIcon(R.drawable.icon_list);
        }
    }

    public void onTextCheminDoubleClick() {
    }
}
