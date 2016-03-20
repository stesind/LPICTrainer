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

import de.sindzinski.util.Logger;
import de.sindzinski.lpictrainer.data.TrainerContract;

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
        Integer rowsDeleted = mContext.getContentResolver().delete(TrainerContract.QuestionEntry.CONTENT_URI, mSelection, mSelectionArgs);
        Logger.i(TAG, "Rows deleted: " + rowsDeleted);
        //add new questions
        ContentValues values = new ContentValues();
        while (it.hasNext()) {
            Question question = (Question) it.next();
            values.clear();
            values.put(TrainerContract.QuestionEntry.COLUMN_ID, question.index);
            values.put(TrainerContract.QuestionEntry.COLUMN_TITLE, question.title);
            values.put(TrainerContract.QuestionEntry.COLUMN_TYPE, question.type);
            values.put(TrainerContract.QuestionEntry.COLUMN_TEXT, question.text);
            values.put(TrainerContract.QuestionEntry.COLUMN_POINTS, question.points);
            values.put(TrainerContract.QuestionEntry.COLUMN_ANSWER1, question.answer1);
            values.put(TrainerContract.QuestionEntry.COLUMN_CORRECT1, question.correct1);
            values.put(TrainerContract.QuestionEntry.COLUMN_ANSWER2, question.answer2);
            values.put(TrainerContract.QuestionEntry.COLUMN_CORRECT2, question.correct2);
            values.put(TrainerContract.QuestionEntry.COLUMN_ANSWER3, question.answer3);
            values.put(TrainerContract.QuestionEntry.COLUMN_CORRECT3, question.correct3);
            values.put(TrainerContract.QuestionEntry.COLUMN_ANSWER4, question.answer4);
            values.put(TrainerContract.QuestionEntry.COLUMN_CORRECT4, question.correct4);
            values.put(TrainerContract.QuestionEntry.COLUMN_ANSWER5, question.answer5);
            values.put(TrainerContract.QuestionEntry.COLUMN_CORRECT5, question.correct5);

            QuestionUri = mContext.getContentResolver().insert(TrainerContract.QuestionEntry.CONTENT_URI, values);
            Logger.i(TAG, "Rows added: " + QuestionUri);
        }
    }
}

