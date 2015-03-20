package com.example.filesexplorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.filesexplorer.Activity.SettingsActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
	
//	private final String TAG = getClass().getSimpleName();
	FragmentFiles fragmentFiles;

    private AlertDialogRadioButton alertDialogSorting = null;
    private AlertDialogRadioButton alertDialogDisplaying = null;

    private static String[] sortingCriterias = new String[] {"Name", "Size", "Category", "Date"}; // todo : put in strings.xml
    private static String[] displayingCriterias = new String[] {"List", "Tree", "Grid"}; // todo : put in strings.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        ArrayList<Object> objectEntries = getListObjects(Environment.getExternalStorageDirectory());
        ArrayList<Object> objectEntries = getListObjects(new File("/storage/sdcard0/Download"));

        // Création du fragment contenant la liste des fichiers si celui-ci ne l'a pas encore été
        if (fragmentFiles == null) {
			fragmentFiles = new FragmentFiles();
			fragmentFiles.setEntriesList(objectEntries);
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container_files, fragmentFiles);
			transaction.commit();
        }
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
    			res.add(new FileAndroid(directory.getParentFile(), true));
    		}
    		File[] files = directory.listFiles();
	    	for (File file : files) {
	    		res.add(new FileAndroid(file));
	    	}
    	}
    	
    	return res;
    }

    public void openDirectory(FileAndroid fileAndroid) {
    	try {
	    	ArrayList<Object> objects = getListObjects(fileAndroid.getFile());
	    	fragmentFiles.setEntriesList(objects);
	    	fragmentFiles.getAdapter().notifyDataSetChanged();
	    	getActionBar().setTitle(fileAndroid.getFile().getPath());
    	} catch (Exception e) {
    		// Trouver pourquoi certains dossiers soulèvent une exception ... problème d'autorisation ?
    		Toast.makeText(this, "Ca marche pas...", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void openFile(FileAndroid fileAndroid) {
    	// Lancer un intent selon le type de fichier
    	Toast.makeText(this, "Lancement d'un intent... on verra plus tard", Toast.LENGTH_SHORT).show();
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
    
    private void openSettings() {
       // Intent intent = new Intent(this, SettingsActivity.class);
       // startActivity(intent);
    }

    private void openSorting() {
        if (alertDialogSorting == null) {
            alertDialogSorting = new AlertDialogRadioButton(this, sortingCriterias, 0);
        }
        alertDialogSorting.show();
    }
    
    private void openDisplaying() {
        if (alertDialogDisplaying == null) {
            alertDialogDisplaying = new AlertDialogRadioButton(this, displayingCriterias, 0);
        }
        alertDialogDisplaying.show();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
        case R.id.action_about:
        	openAbout();
        	break;
        case R.id.action_settings:
        	openSettings();
        	break;
        case R.id.action_sorting:
        	openSorting();
        	break;
        case R.id.action_displaying:
        	openDisplaying();
        	break;
        default:
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
}
