package de.sindzinski.lpictrainer.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;

import de.sindzinski.lpictrainer.data.TrainerContract.QuestionEntry;

import de.sindzinski.util.Logger;
import de.sindzinski.lpictrainer.data.Question;

public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    private static DatabaseHelper sInstance;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "lpicTrainer.db";

    // Contacts table name
    private static final String TABLE_QUESTIONS = "questions";

    private static final String TAG = "LPITrainer-Database";

/*    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }*/

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteException {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + QuestionEntry.INDEX + " INTEGER PRIMARY KEY, "
                + QuestionEntry.TITLE + " TEXT NOT NULL, "
                + QuestionEntry.TYPE + " TEXT NOT NULL, "
                + QuestionEntry.POINTS + " INTEGER, "
                + QuestionEntry.TEXT + " TEXT NOT NULL, "
                + QuestionEntry.ANTWORT1 + " TEXT, "
                + QuestionEntry.RICHTIG1 + " INTEGER, "
                + QuestionEntry.ANTWORT2 + " TEXT, "
                + QuestionEntry.RICHTIG2 + " INTEGER, "
                + QuestionEntry.ANTWORT3 + " TEXT, "
                + QuestionEntry.RICHTIG3 + " INTEGER, "
                + QuestionEntry.ANTWORT4 + " TEXT, "
                + QuestionEntry.RICHTIG4 + " INTEGER, "
                + QuestionEntry.ANTWORT5 + " TEXT, "
                + QuestionEntry.RICHTIG5 + " INTEGER "
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLiteException {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        // Create tables again
        onCreate(db);
    }

    // Wipe database
    public void onWipe()   {
        // Drop older table if existed

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
            // Create tables again
            onCreate(db);
        } catch (SQLiteException e) {
            Logger.e(TAG, "Error wiping database: " + e);
        }
    }

    // Adding new question
    public void addEntry(Question question) throws SQLiteException {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(QuestionEntry.INDEX, question.getIndex());
            values.put(QuestionEntry.TITLE, question.getTitle());
            values.put(QuestionEntry.TYPE, question.getType());
            values.put(QuestionEntry.POINTS, question.getPoints());
            values.put(QuestionEntry.TEXT, question.getText());
            values.put(QuestionEntry.ANTWORT1, question.getAntwort1());
            values.put(QuestionEntry.RICHTIG1, question.getRichtig1());
            values.put(QuestionEntry.ANTWORT2, question.getAntwort2());
            values.put(QuestionEntry.RICHTIG2, question.getRichtig2());
            values.put(QuestionEntry.ANTWORT3, question.getAntwort3());
            values.put(QuestionEntry.RICHTIG3, question.getRichtig3());
            values.put(QuestionEntry.ANTWORT4, question.getAntwort4());
            values.put(QuestionEntry.RICHTIG4, question.getRichtig4());
            values.put(QuestionEntry.ANTWORT5, question.getAntwort5());
            values.put(QuestionEntry.RICHTIG5, question.getRichtig5());
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
        }
    }

    // Getting single question
    public Question getEntry(int id) throws SQLiteException {

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            try (Cursor cursor = db.query(TABLE_QUESTIONS, new String[]{QuestionEntry.INDEX,
                            QuestionEntry.TITLE, QuestionEntry.TYPE, QuestionEntry.POINTS,
                    QuestionEntry.TEXT, QuestionEntry.ANTWORT1, QuestionEntry.RICHTIG1,
                    QuestionEntry.ANTWORT2, QuestionEntry.RICHTIG2, QuestionEntry.ANTWORT3,
                            QuestionEntry.RICHTIG3, QuestionEntry.ANTWORT4, QuestionEntry.RICHTIG4,
                            QuestionEntry.ANTWORT5, QuestionEntry.RICHTIG5},
                    QuestionEntry.INDEX + "=?", new String[]{String.valueOf(id)}, null, null, null, null)) {
                cursor.moveToFirst();
                return new Question.Builder().setIndex(Integer.parseInt(cursor.getString(0)))
                        .setTitle(cursor.getString(1))
                        .setType(cursor.getString(2))
                        .setPoints(Integer.parseInt(cursor.getString(3)))
                        .setText(cursor.getString(4))
                        .setAntwort1(cursor.getString(5))
                        .setRichtig1(cursor.getInt(6) > 0)
                        .setAntwort2(cursor.getString(7))
                        .setRichtig2(cursor.getInt(8) > 0)
                        .setAntwort3(cursor.getString(9))
                        .setRichtig3(cursor.getInt(10) > 0)
                        .setAntwort4(cursor.getString(11))
                        .setRichtig4(cursor.getInt(12) > 0)
                        .setAntwort5(cursor.getString(13))
                        .setRichtig5(cursor.getInt(14) > 0)
                        .build();
            } catch (SQLiteException e) {
                Logger.e(TAG, "Error reading database: " + e);
            }
        }
        return null;
    }

    // Getting All Entries
    public List<Question> getAllEntries(String fileName) {
        List<Question> entryList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS;

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            try (Cursor cursor = db.rawQuery(selectQuery, null)) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Question question = new Question.Builder()
                                .setIndex(Integer.parseInt(cursor.getString(0)))
                                .setTitle(cursor.getString(1))
                                .setType(cursor.getString(2))
                                .setPoints(Integer.parseInt(cursor.getString(3)))
                                .setText(cursor.getString(4))
                                .setAntwort1(cursor.getString(5))
                                .setRichtig1(cursor.getInt(6) > 0)
                                .setAntwort2(cursor.getString(7))
                                .setRichtig2(cursor.getInt(8) > 0)
                                .setAntwort3(cursor.getString(9))
                                .setRichtig3(cursor.getInt(10) > 0)
                                .setAntwort4(cursor.getString(11))
                                .setRichtig4(cursor.getInt(12) > 0)
                                .setAntwort5(cursor.getString(13))
                                .setRichtig5(cursor.getInt(14) > 0)
                                .build();
                        // Adding contact to list
                        entryList.add(question);
                    } while (cursor.moveToNext());
                }
            } catch (SQLiteException e) {
                Logger.e(TAG, "Error reading database: " + e);
            }
        }
        // return contact list
        return entryList;
    }

    // Updating single question
    public int updateEntry(Question question) throws SQLiteException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(QuestionEntry.INDEX, question.getIndex());
            values.put(QuestionEntry.TITLE, question.getTitle());
            values.put(QuestionEntry.TYPE, question.getType());
            values.put(QuestionEntry.POINTS, question.getPoints());
            values.put(QuestionEntry.TEXT, question.getText());
            values.put(QuestionEntry.ANTWORT1, question.getAntwort1());
            values.put(QuestionEntry.RICHTIG1, question.getRichtig1());
            values.put(QuestionEntry.ANTWORT2, question.getAntwort2());
            values.put(QuestionEntry.RICHTIG2, question.getRichtig2());
            values.put(QuestionEntry.ANTWORT3, question.getAntwort3());
            values.put(QuestionEntry.RICHTIG3, question.getRichtig3());
            values.put(QuestionEntry.ANTWORT4, question.getAntwort4());
            values.put(QuestionEntry.RICHTIG4, question.getRichtig4());
            values.put(QuestionEntry.ANTWORT5, question.getAntwort5());
            values.put(QuestionEntry.RICHTIG5, question.getRichtig5());
            // updating row
            return db.update(TABLE_QUESTIONS, values, QuestionEntry.INDEX + " = ?",
                    new String[]{String.valueOf(question.getIndex())});
        }
    }

    // Deleting single question
    public void deleteEntry(Question question) throws SQLiteException  {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_QUESTIONS, QuestionEntry.INDEX + " = ?",
                    new String[]{String.valueOf(question.getIndex())});
        }
    }


    // Getting entries Count
    public int getEntriesCount() throws SQLiteException {
        Cursor cursor = null;
        String countQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            cursor = db.rawQuery(countQuery, null);
            // return count
            return cursor.getCount();
        } finally {
            cursor.close();
        }
    }

}