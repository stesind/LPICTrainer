package de.sindzinski.lpictrainer;

import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by steffen on 17.10.13.
 */
public class Trial {

    public static boolean checkTrial(final Context context, boolean notify) {

        boolean isTrial = context.getPackageManager().checkSignatures(
                context.getPackageName(), "de.sindzinski.lpictrainer.key") != PackageManager.SIGNATURE_MATCH;

        if (isTrial) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            long firstStart = sharedPref.getLong("date_firstlaunch_trial", -1);

            if (firstStart == -1) {
                firstStart = System.currentTimeMillis();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("date_firstlaunch_trial", firstStart);
                editor.commit();
            }

            if (!notify && System.currentTimeMillis() > firstStart + (R.integer.trial_days * 24 * 60 * 60 * 1000)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.trial_expired);
                builder.setMessage(R.string.trial_expired_message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.trial_purchase, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        openUnlockUrl(context);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                long daysLeft = Math.round((firstStart + (R.integer.trial_days * 24 * 60 * 60 * 1000) - System.currentTimeMillis()) / (24 * 60 * 60 * 1000));

                if (notify) {
                    Toast.makeText(context, R.string.trial_mode_prompt + Long.valueOf(daysLeft).toString(), Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        } else if (notify) {
            //toast(R.string.trial_thanks);
        }
        return false;
    }

    private static void openUnlockUrl(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=de.sindzinski.lpictrainer.key"));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ae) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=de.sindzinski.lpictrainer.key"));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.error_other_error, Toast.LENGTH_SHORT).show();

            }
        }
    }
/*    import android.app.backup.BackupAgentHelper;
    import android.app.backup.SharedPreferencesBackupHelper;

    public class MyBackupAgent extends BackupAgentHelper {
        // The names of the SharedPreferences groups that the application maintains.  These
        // are the same strings that are passed to getSharedPreferences(String, int).
        static final String PREFS_DISPLAY = "displayprefs";
        static final String PREFS_SCORES = "highscores";

        // An arbitrary string used within the BackupAgentHelper implementation to
        // identify the SharedPreferenceBackupHelper's data.
        static final String MY_PREFS_BACKUP_KEY = "myprefs";

        // Simply allocate a helper and install it
        void onCreate() {
            SharedPreferencesBackupHelper helper =
                    new SharedPreferencesBackupHelper(this, PREFS_DISPLAY, PREFS_SCORES);
            addHelper(MY_PREFS_BACKUP_KEY, helper);
        }
    }*/
}
