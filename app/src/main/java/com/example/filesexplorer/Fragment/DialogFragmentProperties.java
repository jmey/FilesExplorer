package com.example.filesexplorer.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.filesexplorer.Model.FileAndroid;
import com.example.filesexplorer.R;

public class DialogFragmentProperties extends DialogFragment {

    protected static FileAndroid file;

    public static DialogFragmentProperties newInstance(int title, FileAndroid filedroid) {
        DialogFragmentProperties dialog = new DialogFragmentProperties();
        Bundle args = new Bundle();
        args.putInt("title",  title);
        dialog.setArguments(args);
        file = filedroid;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_properties,  container, false);

        ((TextView) v.findViewById(R.id.file_name)).setText(file.getFile().getName());
        ((TextView) v.findViewById(R.id.file_folder_text)).setText(file.getFile().getAbsolutePath());
        ((TextView) v.findViewById(R.id.file_type_text)).setText(file.getExtension());
        ((TextView) v.findViewById(R.id.file_size_text)).setText(String.valueOf(file.getSize()));

        if(file.isDirectory()) {
            ((TextView) v.findViewById(R.id.file_special_label)).setText("Nombre de fichiers:");
            ((TextView) v.findViewById(R.id.file_special_text)).setText(String.valueOf(file.getFile().listFiles().length));
        }
        else {

        }

        getDialog().setTitle(getArguments().getInt("title"));
        return v;
    }

}
