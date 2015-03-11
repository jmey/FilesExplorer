package com.example.filesexplorer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
        ArrayList<Object> objectEntries = getListObjects(new File(getString(R.string.download_directory)));

        // Création du fragment contenant la liste des fichiers si celui-ci ne l'a pas encore été
        if (fragmentFiles == null) {
			fragmentFiles = new FragmentFiles();
			fragmentFiles.setEntriesList(objectEntries);
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container_files, fragmentFiles);
			transaction.commit();
        }
        getActionBar().setHomeButtonEnabled(true);
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
        if (fileAndroid.getExtension().equals("pdf")) {
            //ouvre le fichier (pour le moment for pdf only)
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(fileAndroid.getFile()),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        } else {
            // Lancer un intent selon le type de fichier
            Toast.makeText(this, "Lancement d'un intent... on verra plus tard", Toast.LENGTH_SHORT).show();
        }
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
        case android.R.id.home:
            openDirectory(new FileAndroid(new File(getString(R.string.download_directory))));
            break;
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
        super.onBackPressed();
    }
}
