package com.example.filesexplorer.Utils;

import android.content.Context;

import com.example.filesexplorer.R;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class FileType {

    public final static int PICTURE = 1;
    public final static int DOCUMENT = 2;
    public final static int VIDEO = 3;
    public final static int MUSIC = 4;

    private static List<String> typesPicture;
    private static List<String> typesDocument;
    private static List<String> typesVideo;
    private static List<String> typesMusic;

    public static boolean isTypeFile(Context context, File file, int type) {
        if (type == PICTURE) {
            return isPicture(context, file);
        } else if (type == DOCUMENT) {
            return isDocument(context, file);
        } else if (type == VIDEO) {
            return isVideo(context, file);
        } else if (type == MUSIC) {
            return isMusic(context, file);
        }

        return false;
    }

    public static boolean isPicture(Context context, File file) {
        if (typesPicture == null) {
            typesPicture = Arrays.asList(context.getResources().getStringArray(R.array.list_types_picture));
        }
        String extension = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
        return typesPicture.contains(extension);
    }

    public static boolean isDocument(Context context, File file) {
        if (typesDocument == null) {
            typesDocument = Arrays.asList(context.getResources().getStringArray(R.array.list_types_document));
        }
        String extension = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
        return typesDocument.contains(extension);
    }

    public static boolean isVideo(Context context, File file) {
        if (typesVideo == null) {
            typesVideo = Arrays.asList(context.getResources().getStringArray(R.array.list_types_video));
        }
        String extension = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
        return typesVideo.contains(extension);
    }

    public static boolean isMusic(Context context, File file) {
        if (typesMusic == null) {
            typesMusic = Arrays.asList(context.getResources().getStringArray(R.array.list_types_music));
        }
        String extension = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
        return typesMusic.contains(extension);
    }
}
