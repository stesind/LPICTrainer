package de.sindzinski.lpictrainer;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;

import de.sindzinski.database.DatabaseHandler;
import de.sindzinski.database.XmlParser;
import de.sindzinski.helper.Logger;

/**
 * Created by steffen on 20.12.15.
 */
public class LoadXmlAsyncTask extends AsyncTask<String, Integer, ArrayList<Question>> {
    private Context mContext;
    private static final String TAG = "LPITrainer AsyncTask";
    private ArrayList<Question> entries = null;

    public LoadXmlAsyncTask(Context context) {
        mContext = context;
    }

    protected ArrayList<Question> doInBackground(String... params) {
        //runs in background task
        Logger.i(TAG, "running in async background task: doInBackground ");
        try {
            return entries = loadXmlFromFile(params[0]);
        } catch (IOException ie){
            Logger.e(TAG, "Error: " +ie.toString() );
            return null;
        } catch (XmlPullParserException ie){
            Logger.e(TAG, "Error: " +ie.toString() );
            return null;
        }
    }

    private ArrayList<Question> loadXmlFromFile(String fileName) throws XmlPullParserException, IOException {

        XmlParser parser = new XmlParser();
        //ArrayList<Question> entries = null;
        InputStream is;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        Logger.i(TAG, "running in async background task: doInBackground ");

        try {
            // find out if asset or file
            if (fileName.indexOf('\\') == -1)
            {
                is = mContext.getAssets().open(fileName);
            }
            else
            {
                is = new FileInputStream(fileName);
            }
            bis = new BufferedInputStream(is);
            dis = new DataInputStream(bis);

            entries = parser.parse(dis);
            safeToSQL(entries);
            return entries;

        } catch (IOException ie) {
            Logger.e(TAG, "Error: " +ie.toString() );
            return null;
        } catch (XmlPullParserException ie){
            Logger.e(TAG, "Error: " +ie.toString() );
            return null;
        } catch (Exception ie){
            Logger.e(TAG, "Error: " +ie.toString() );
            return null;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (dis != null) {
                dis.close();
            }
        }
    }

    private void safeToSQL(ArrayList<Question> entries) {

        ListIterator it = entries.listIterator();

        DatabaseHandler db = new DatabaseHandler(mContext);

        db.onWipe();
        while (it.hasNext()) {
            Question question = (Question) it.next();
            db.addEntry(question);
        }
    }
}

