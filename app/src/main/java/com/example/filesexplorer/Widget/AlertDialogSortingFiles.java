package com.example.filesexplorer.Widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.filesexplorer.Activity.MainActivity;
import com.example.filesexplorer.Enum.SortCriterion;
import com.example.filesexplorer.Enum.SortSense;
import com.example.filesexplorer.R;

import java.io.File;

public class AlertDialogSortingFiles extends AlertDialog.Builder {

    private MainActivity activity;
    private String[] criteria;
    private int criterionPosition = 0;

    public AlertDialogSortingFiles(Context context, String[] criteria) {
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
                criterionPosition = item;
                activity.sortFragmentFilesBy(SortCriterion.get(item));
            }
            dialog.dismiss();
        }
    };
}
