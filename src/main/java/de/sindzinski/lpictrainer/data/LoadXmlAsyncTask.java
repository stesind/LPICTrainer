package de.sindzinski.lpictrainer.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;

import de.sindzinski.helper.Logger;
import de.sindzinski.lpictrainer.Question;

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
            //safeToSQL(entries);
            safeToProvider(entries);
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

    private void safeToProvider(ArrayList<Question> entries) {
        ListIterator it = entries.listIterator();
        Uri QuestionUri;
        //delete all old
        String mSelection = null;
        String[] mSelectionArgs = null;
        Integer rowsDeleted = mContext.getContentResolver().delete(QuestionContentProvider.CONTENT_URI, mSelection, mSelectionArgs);
        Logger.i(TAG, "Rows deleted: " + rowsDeleted);
        //add new questions
        ContentValues values = new ContentValues();
        while (it.hasNext()) {
            Question question = (Question) it.next();
            values.clear();
            values.put(QuestionTable.COLUMN_ID, question.index);
            values.put(QuestionTable.COLUMN_TITLE, question.title);
            values.put(QuestionTable.COLUMN_TYPE, question.type);
            values.put(QuestionTable.COLUMN_TEXT, question.text);
            values.put(QuestionTable.COLUMN_POINTS, question.points);
            values.put(QuestionTable.COLUMN_ANTWORT1, question.antwort1);
            values.put(QuestionTable.COLUMN_RICHTIG1, question.richtig1);
            values.put(QuestionTable.COLUMN_ANTWORT2, question.antwort2);
            values.put(QuestionTable.COLUMN_RICHTIG2, question.richtig2);
            values.put(QuestionTable.COLUMN_ANTWORT3, question.antwort3);
            values.put(QuestionTable.COLUMN_RICHTIG3, question.richtig3);
            values.put(QuestionTable.COLUMN_ANTWORT4, question.antwort4);
            values.put(QuestionTable.COLUMN_RICHTIG4, question.richtig4);
            values.put(QuestionTable.COLUMN_ANTWORT5, question.antwort5);
            values.put(QuestionTable.COLUMN_RICHTIG5, question.richtig5);

            QuestionUri = mContext.getContentResolver().insert(QuestionContentProvider.CONTENT_URI, values);
            Logger.i(TAG, "Rows added: " + QuestionUri);
        }
    }

    private void safeToSQL(ArrayList<Question> entries) {

        ListIterator it = entries.listIterator();

        DatabaseHelper db = DatabaseHelper.getInstance(mContext);

        db.onWipe();
        while (it.hasNext()) {
            Question question = (Question) it.next();
            db.addEntry(question);
        }
    }
}

