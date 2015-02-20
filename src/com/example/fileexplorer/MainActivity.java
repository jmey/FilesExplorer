package com.example.fileexplorer;

import java.io.File;
import java.util.ArrayList;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
//	private final String TAG = getClass().getSimpleName();
	FragmentFiles fragmentFiles;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<Object> objectEntries = getListObjects(Environment.getExternalStorageDirectory());
        
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
