package com.example.filesexplorer.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filesexplorer.Model.FileAndroid;
import com.example.filesexplorer.R;

import java.io.File;


public class MainApplicationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int FILE_CHOOSER_REQUEST = 1;

    public void button_explorer(View view) {
        Toast.makeText(this, "Ouverture de l'explorateur", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", true);
        startActivityForResult(intent, FILE_CHOOSER_REQUEST);
    }

    public void button_go(View view) {
        TextView tv = (TextView) findViewById(R.id.filename);
        File file = new File(tv.getText().toString());
        if (file.exists()) {
            FileAndroid fileAndroid = new FileAndroid(this, file);
            String extension = fileAndroid.getExtension();
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            boolean lanceIntent = true;

            if (extension != null) {
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String sMime = mime.getMimeTypeFromExtension(extension);
                if (sMime != null) {
                    target.setDataAndType(Uri.fromFile(fileAndroid.getFile()), sMime);
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
                Toast.makeText(this, "Ce fichier n'est pas géré", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Veuillez sélectionner un fichier", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (resultCode == RESULT_OK) {
                TextView tv = (TextView) findViewById(R.id.filename);
                tv.setText(data.getStringExtra("file"));
            }
        }
    }
}
