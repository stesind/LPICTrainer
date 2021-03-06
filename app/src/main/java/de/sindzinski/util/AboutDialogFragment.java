package de.sindzinski.util;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;

import de.sindzinski.lpictrainer.R;

/**
 * Created by steffen on 16.08.13.
 */
public class AboutDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.about_dialog_title);
        builder.setMessage(R.string.copyright)
                .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
/*                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });*/
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
