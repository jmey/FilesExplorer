package com.example.filesexplorer.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.filesexplorer.AsyncTask.FilesSearching;
import com.example.filesexplorer.Utils.sortFileUtil;
import com.example.filesexplorer.Widget.AlertDialogRadioButton;
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
    private File currentDirectory;

    private AlertDialogRadioButton alertDialogSorting = null;
    private AlertDialogRadioButton alertDialogDisplaying = null;

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

        // Get the last directory saved in the preferences file
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        currentDirectory = new File(settings.getString("directory", "/sdcard/Download"));

        // Get the property to know if the preference is checked or not
        isHiddenFileShowed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_hidden_file", false);

        // Instantiate and add the fragment of files
        fragmentFiles = new FragmentFiles(DisplayMode.GRID);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_files, fragmentFiles);
        transaction.commit();

        // Fill the fragment with the files of the currentDirectory
        openDirectory(currentDirectory, sortFileUtil.SortType.none);

        //
        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setTitle(currentDirectory.getPath());
        }
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
                    updateFragmentFiles(new ArrayList<File>(), false);
                }
                return true;
            }
        });

        // Listener to load back the currentDirectory when out of the search mode
        MenuItemCompat.setOnActionExpandListener(itemSearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                openDirectory(new FileAndroid(activity, currentDirectory));
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
            openDirectory(new File(getString(R.string.download_directory)), sortFileUtil.SortType.none);
        } else if (id == R.id.action_about) {
            openAbout();
        } else if (id == R.id.action_settings) {
            Intent i = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        } else if (id == R.id.action_sorting) {
            openSorting();
        } else if (id == R.id.action_display_list) {
            fragmentFiles.setDisplayMode(DisplayMode.LIST);
        } else if (id == R.id.action_display_grid) {
            fragmentFiles.setDisplayMode(DisplayMode.GRID);
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
                    openDirectory(currentDirectory, sortFileUtil.SortType.none);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            openDirectory(new FileAndroid(this, currentDirectory.getParentFile()));
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void openSorting() {

        if (alertDialogSorting == null) {
            alertDialogSorting = new AlertDialogRadioButton(this, getResources().getStringArray(R.array.list_sorting_values), 0);
        }
        alertDialogSorting.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                switch (alertDialogSorting.getCriteriaPosition()) {
                    case 0:
                        openDirectory(currentDirectory, sortFileUtil.SortType.name);
                        break;
                    case 1:
                        openDirectory(currentDirectory, sortFileUtil.SortType.length);
                        break;
                    case 2:
                        openDirectory(currentDirectory, sortFileUtil.SortType.date);
                        break;
                    default:
                        break;
                }
            }
        });
        alertDialogSorting.show();
    }

    public void openDirectory(File file, sortFileUtil.SortType st) {
        File[] arrayFiles = file.listFiles();

        if (arrayFiles != null) {
            ArrayList<File> files = new ArrayList<File>();

            Collections.addAll(files, arrayFiles);

            currentDirectory = file;
            sortFileUtil.triPar(st,files);

            // From File[] to ArrayList<File> with adding the parent if it's not the root directory
            if (!file.getPath().equals("/")) {
                files.add(0,file.getParentFile());
            }
            boolean hasParent = false;
            if (files.size() > arrayFiles.length) { // We have had an element, which is the parent
                hasParent = true;
            }

            updateFragmentFiles(files, hasParent);
        } else {
            Toast.makeText(this, "Cannot open this directory for some reason", Toast.LENGTH_SHORT).show();
        }
    }

    // Surcharge
    public void openDirectory(FileAndroid fileAndroid) {
        openDirectory(fileAndroid.getFile(), sortFileUtil.SortType.none);
    }

    // Update the FragmentFiles with the ArrayList of File in parameter
    // If hasParent is at true, the first element of the ArrayList is this parent
    public void updateFragmentFiles(ArrayList<File> files, boolean hasParent) {
        try {
            ArrayList<Object> objects = new ArrayList<Object>();

            int i = 0;
            if (hasParent) {
                objects.add(new FileAndroid(this, files.get(0), true));
                i = 1;
            }

            while (i < files.size()) {
                if (isHiddenFileShowed || !files.get(i).isHidden()) {
                    objects.add(new FileAndroid(this, files.get(i)));
                }
                i++;
            }

            fragmentFiles.setEntriesList(objects);
            if (fragmentFiles.getAdapter() != null) {
                fragmentFiles.getAdapter().notifyDataSetChanged();
            }
            if (getActionBar() != null) {
                getActionBar().setTitle(currentDirectory.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception...", Toast.LENGTH_SHORT).show();
        }
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

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    private void openAbout() {
        DialogFragmentAbout dialog = DialogFragmentAbout.newInstance(R.string.action_about);
        dialog.show(getFragmentManager(), "FragmentTransaction.add");
    }
}
