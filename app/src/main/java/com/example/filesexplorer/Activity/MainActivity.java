package com.example.filesexplorer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.filesexplorer.Widget.AlertDialogRadioButton;
import com.example.filesexplorer.Enum.DisplayMode;
import com.example.filesexplorer.Model.FileAndroid;
import com.example.filesexplorer.Fragment.DialogFragmentAbout;
import com.example.filesexplorer.Fragment.FragmentFiles;
import com.example.filesexplorer.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
	
//	private final String TAG = getClass().getSimpleName();
    private MainActivity activity;
	private static FragmentFiles fragmentFiles;
    File currentDirectory;

    private AlertDialogRadioButton alertDialogSorting = null;
    private AlertDialogRadioButton alertDialogDisplaying = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        // Création du fragment contenant la liste des fichiers si celui-ci ne l'a pas encore été
        if (fragmentFiles == null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            currentDirectory = new File(settings.getString("directory", "/sdcard/Download"));

            ArrayList<Object> objectEntries = getListObjects(currentDirectory);
			fragmentFiles = new FragmentFiles(DisplayMode.GRID);
			fragmentFiles.setEntriesList(objectEntries);

            if (getActionBar() != null) {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setTitle(currentDirectory.getPath());
            }
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_files, fragmentFiles);
        transaction.commit();
    }

    /**
     * Renvoie la liste des fichiers et dossiers contenus dans le dossier en paramètre
     * @param directory
     * @return une liste de File
     */
    public ArrayList<Object> getListObjects(File directory) {
    	ArrayList<Object> res = new ArrayList<Object>();
    	
    	if (directory.exists()) {
    		if (!directory.getPath().equals("/")) {	// Bah oui, la racine n'a pas de parent la pauvre :|
                FileAndroid backDirectory = new FileAndroid(this, directory.getParentFile(), true);
    			res.add(backDirectory);
    		}
    		File[] files = directory.listFiles();
	    	for (File file : files) {
	    		res.add(new FileAndroid(this, file));
	    	}
    	}
    	
    	return res;
    }

    public void openDirectory(FileAndroid fileAndroid) {
    	try {
	    	ArrayList<Object> objects = getListObjects(fileAndroid.getFile());
	    	fragmentFiles.setEntriesList(objects);
	    	fragmentFiles.getAdapter().notifyDataSetChanged();
            currentDirectory = fileAndroid.getFile();
            if (getActionBar() != null) {
                getActionBar().setTitle(fileAndroid.getFile().getPath());
            }
    	} catch (Exception e) {
    		// Trouver pourquoi certains dossiers soulèvent une exception ... problème d'autorisation ?
    		Toast.makeText(this, "Ca marche pas...", Toast.LENGTH_SHORT).show();
    	}
    }

    public void openFile(FileAndroid fileAndroid) {
        String parentDirectory = fileAndroid.getFile().getParentFile().getAbsolutePath();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("directory", parentDirectory);
        editor.commit();

        Intent intent = new Intent();
        intent.putExtra("file", fileAndroid.getFile().getAbsolutePath());
        MainActivity.this.setResult(RESULT_OK, intent);
        MainActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void openAbout() {
    	DialogFragmentAbout dialog = DialogFragmentAbout.newInstance(R.string.action_about);
    	dialog.show(getFragmentManager(), "FragmentTransaction.add");
    }

    private static final int RESULT_SETTINGS = 1;

    private void openSettings() {
    	Intent i = new Intent(this, UserSettingsActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }

    private void openSorting() {
        if (alertDialogSorting == null) {
            alertDialogSorting = new AlertDialogRadioButton(this, getResources().getStringArray(R.array.list_sorting_values), 0);
        }
        alertDialogSorting.show();
    }
    
    private void openDisplaying() {
        if (alertDialogDisplaying == null) {
            alertDialogDisplaying = new AlertDialogRadioButton(this, getResources().getStringArray(R.array.list_displaying_values), 0);
        }
        alertDialogDisplaying.show();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            openDirectory(new FileAndroid(this, new File(getString(R.string.download_directory))));
        } else if (id == R.id.action_about) {
            openAbout();
        } else if (id == R.id.action_settings) {
            openSettings();
        } else if (id == R.id.action_sorting) {
            openSorting();
        } else if (id == R.id.action_displaying) {
            openDisplaying();
        } else if (id == R.id.action_display_list) {
            fragmentFiles.setDisplayMode(DisplayMode.LIST);
        } else if (id == R.id.action_display_grid) {
            fragmentFiles.setDisplayMode(DisplayMode.GRID);
        } else if (id == R.id.action_search) {
            openSearch();

        } else {
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openSearch() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SETTINGS:
                //showUserSettings();
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
}
