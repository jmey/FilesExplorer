package com.example.filesexplorer;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	
//	private final String TAG = getClass().getSimpleName();
	FragmentFiles fragmentFiles;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        ArrayList<Object> objectEntries = getListObjects(Environment.getExternalStorageDirectory());
        ArrayList<Object> objectEntries = getListObjects(new File("/storage/sdcard0/Download"));
        
        if (fragmentFiles == null) {
			fragmentFiles = new FragmentFiles();
			fragmentFiles.setEntriesList(objectEntries);
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container_files, fragmentFiles);
			transaction.commit();
        }
    }

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
    		// Trouver pourquoi certains dossiers soul�vent une exception ... probl�me d'autorisation ?
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
    	
    }
    
    private void openSorting() {
    	
    }
    
    private void openDisplaying() {
    	
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
