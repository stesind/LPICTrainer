package de.sindzinski.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import de.sindzinski.lpictrainer.R;

/**
 * Created by steffen on 08.10.13.
 */
public class EulaDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int padding = getResources().getDimensionPixelSize(R.dimen.content_padding_normal);

        TextView eulaTextView = new TextView(getActivity());
        eulaTextView.setText(Html.fromHtml(getString(R.string.eula_text)));
        eulaTextView.setMovementMethod(LinkMovementMethod.getInstance());
        eulaTextView.setPadding(padding, padding, padding, padding);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_eula)
                .setView(eulaTextView)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}
