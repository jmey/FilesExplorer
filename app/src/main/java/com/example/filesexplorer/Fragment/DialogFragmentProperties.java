package com.example.filesexplorer.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.filesexplorer.Activity.MainActivity;
import com.example.filesexplorer.Model.FileAndroid;
import com.example.filesexplorer.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import android.media.MediaPlayer;

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
        SimpleDateFormat ft = new SimpleDateFormat ("E dd/MM/yyyy, HH:mm:ss");

        ((TextView) v.findViewById(R.id.file_name)).setText(file.getFile().getName());
        ((TextView) v.findViewById(R.id.file_folder_text)).setText(file.getFile().getParent());
        ((TextView) v.findViewById(R.id.file_size_text)).setText(humanReadableFileSize(file.getSize()));
        ((TextView) v.findViewById(R.id.file_modified_text)).setText(ft.format(file.getDate()));
        ((TextView) v.findViewById(R.id.file_hidden_text)).setText(file.getFile().isHidden() ? R.string.yes: R.string.no);

        if(file.isDirectory()) {
            String elts;
            if(file.getFile().list() == null) {
                elts = getResources().getString(R.string.unknown) ;
            }
            else {
                elts = file.getFile().list().length + " " + getResources().getString(R.string.elements) ;
            }

            ((TextView) v.findViewById(R.id.file_type_text)).setText(R.string.type_directory);
            ((TextView) v.findViewById(R.id.file_special_label)).setText(R.string.content);
            ((TextView) v.findViewById(R.id.file_special_text)).setText(elts);
        }
        else if(file.isPicture()){
            String file_resolution = String.valueOf(file.getBitmap().getWidth()) + " x " + String.valueOf(file.getBitmap().getHeight());

            ((TextView) v.findViewById(R.id.file_type_text)).setText(R.string.type_picture);
            ((TextView) v.findViewById(R.id.file_special_label)).setText(R.string.resolution);
            ((TextView) v.findViewById(R.id.file_special_text)).setText(file_resolution);
        }
        else if(file.isMusic() || file.isVideo()) {

            if(file.isMusic()) {
                ((TextView) v.findViewById(R.id.file_type_text)).setText(R.string.type_music);
            }
            else {
                ((TextView) v.findViewById(R.id.file_type_text)).setText(R.string.type_video);
            }

            ((TextView) v.findViewById(R.id.file_special_label)).setText(R.string.duration);
            ((TextView) v.findViewById(R.id.file_special_text)).setText(GetDurationFromMedia(file.getFile()));
        }
        else {
            String type_doc;

            if(file.isArchive()){
               type_doc = getResources().getString(R.string.type_archive);
            }
            else if(file.isDocument()){
                type_doc = getResources().getString(R.string.type_document);
            }
            else if(file.isSpreadsheet()){
                type_doc = getResources().getString(R.string.type_spreadsheet);
            }
            else if(file.isPresentation()){
                type_doc = getResources().getString(R.string.type_presentation);
            }
            else {
                type_doc = getResources().getString(R.string.type_unknown, file.getExtension());
            }

            ((TextView) v.findViewById(R.id.file_type_text)).setText(type_doc);
            v.findViewById(R.id.LL_special).setVisibility(LinearLayout.GONE);
        }

        getDialog().setTitle(getArguments().getInt("title"));
        return v;
    }

    public static String humanReadableFileSize(long size) {

        String[] units = new String[] { "o", "Ko", "Mo", "Go", "To" };
        if(size <= 0) {
            return "0 o";
        }

        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String GetDurationFromMedia(File file ) {
        MediaPlayer mp = new MediaPlayer().create(new Activity(), Uri.fromFile(file));
        int duration = mp.getDuration() / 1000;
        mp.reset();
        mp.release();

        int h = duration / 3600;
        int m = duration % 3600 / 60;
        int s = duration % 60;
        return (h<10?"0"+h:h) + ":" + (m<10?"0"+m:m) + ":" + (s<10?"0"+s:s);
    }
}
