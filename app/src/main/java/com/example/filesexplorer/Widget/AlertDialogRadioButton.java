package com.example.filesexplorer.Widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.example.filesexplorer.R;

public class AlertDialogRadioButton extends AlertDialog.Builder {

    private String[] criterias;
    private int criteriaPosition;

    public AlertDialogRadioButton(Context context, String[] criterias, int position) {
        super(context);
        this.criterias = criterias;
        this.criteriaPosition = position;
        this.setTitle(R.string.action_displaying);
    }

    // TODO : sortir et mettre le listener en paramètre
    public void createTheDialog() {
        this.setSingleChoiceItems(criterias, criteriaPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                criteriaPosition = item;

                switch (item) {
                    case 0:
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }

                dialog.dismiss();
            }
        });
        this.create();
    }

    @NonNull
    @Override
    public AlertDialog show() {
        createTheDialog();
        return super.show();
    }
}
