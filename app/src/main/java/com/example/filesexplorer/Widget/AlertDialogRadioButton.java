package com.example.filesexplorer.Widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.filesexplorer.Activity.MainActivity;
import com.example.filesexplorer.Enum.SortType;
import com.example.filesexplorer.R;

import java.io.File;

public class AlertDialogRadioButton extends AlertDialog.Builder {

    private MainActivity activity;
    private String[] criteria;
    private int criterionPosition = 0;
    private File directory;
    private SortType sortType = SortType.name;

    public AlertDialogRadioButton(Context context, String[] criteria) {
        super(context);
        this.setTitle(R.string.action_displaying);
        this.criteria = criteria;
        try {
            activity = (MainActivity)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AlertDialog show() {
        this.setSingleChoiceItems(criteria, criterionPosition, onClickListener);
        this.create();
        return super.show();
    }

    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item) {
            if (criterionPosition != item) {
                switch (criterionPosition = item) {
                    case 0:
                        activity.openDirectory(directory, SortType.name);
                        sortType = SortType.name;
                        break;
                    case 1:
                        activity.openDirectory(directory, SortType.length);
                        sortType = SortType.length;
                        break;
                    case 2:
                        activity.openDirectory(directory, SortType.date);
                        sortType = SortType.date;
                        break;
                    default:
                        break;
                }
            }
            dialog.dismiss();
        }
    };

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public SortType getSortType() {
        return sortType;
    }
}
