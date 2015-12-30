package de.sindzinski.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import de.sindzinski.helper.Logger;
import de.sindzinski.lpictrainer.Question;

public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    private static DatabaseHelper sInstance;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "lpiTrainer";

    // Contacts table name
    private static final String TABLE_ENTRIES = "entries";

    // Contacts Table Columns names
    private static final String INDEX ="ID";
    private static final String TITLE ="title";
    private static final String TYPE ="type";
    private static final String POINTS = "points";
    private static final String TEXT = "text";
    private static final String ANTWORT1 = "antwort1";
    private static final String RICHTIG1 = "richtig1";
    private static final String ANTWORT2 = "antwort2";
    private static final String RICHTIG2 = "richtig2";
    private static final String ANTWORT3 = "antwort3";
    private static final String RICHTIG3 = "richtig3";
    private static final String ANTWORT4 = "antwort4";
    private static final String RICHTIG4 = "richtig4";
    private static final String ANTWORT5 = "antwort5";
    private static final String RICHTIG5 = "richtig5";

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
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ENTRIES + "("
                + INDEX + " INTEGER PRIMARY KEY, "
                + TITLE + " TEXT, "
                + TYPE + " TEXT, "
                + POINTS + " INTEGER, "
                + TEXT + " TEXT, "
                + ANTWORT1 + " TEXT, "
                + RICHTIG1 + " INTEGER, "
                + ANTWORT2 + " TEXT, "
                + RICHTIG2 + " INTEGER, "
                + ANTWORT3 + " TEXT, "
                + RICHTIG3 + " INTEGER, "
                + ANTWORT4 + " TEXT, "
                + RICHTIG4 + " INTEGER, "
                + ANTWORT5 + " TEXT, "
                + RICHTIG5 + " INTEGER "
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLiteException {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        // Create tables again
        onCreate(db);
    }

    // Wipe database
    public void onWipe()   {
        // Drop older table if existed

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
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
            values.put(INDEX, question.getIndex());
            values.put(TITLE, question.getTitle());
            values.put(TYPE, question.getType());
            values.put(POINTS, question.getPoints());
            values.put(TEXT, question.getText());
            values.put(ANTWORT1, question.getAntwort1());
            values.put(RICHTIG1, question.getRichtig1());
            values.put(ANTWORT2, question.getAntwort2());
            values.put(RICHTIG2, question.getRichtig2());
            values.put(ANTWORT3, question.getAntwort3());
            values.put(RICHTIG3, question.getRichtig3());
            values.put(ANTWORT4, question.getAntwort4());
            values.put(RICHTIG4, question.getRichtig4());
            values.put(ANTWORT5, question.getAntwort5());
            values.put(RICHTIG5, question.getRichtig5());
            // Inserting Row
            db.insert(TABLE_ENTRIES, null, values);
        }
    }

    // Getting single question
    public Question getEntry(int id) throws SQLiteException {

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            try (Cursor cursor = db.query(TABLE_ENTRIES, new String[]{INDEX,
                            TITLE, TYPE, POINTS, TEXT, ANTWORT1, RICHTIG1, ANTWORT2, RICHTIG2,
                            ANTWORT3, RICHTIG3, ANTWORT4, RICHTIG4, ANTWORT5, RICHTIG5},
                    INDEX + "=?", new String[]{String.valueOf(id)}, null, null, null, null)) {
                cursor.moveToFirst();
                return new Question(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4), cursor.getString(5), cursor.getInt(6) > 0, cursor.getString(7), cursor.getInt(8) > 0, cursor.getString(9), cursor.getInt(10) > 0,
                        cursor.getString(11), cursor.getInt(12) > 0, cursor.getString(13), cursor.getInt(14) > 0);
            } catch (SQLiteException e) {
                Logger.e(TAG, "Error reading database: " + e);
            }
        }
        return null;
    }

    // Getting All Entries
    public List<Question> getAllEntries() {
        List<Question> entryList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            try (Cursor cursor = db.rawQuery(selectQuery, null)) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Question question = new Question(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                                cursor.getString(4), cursor.getString(5), cursor.getInt(6) > 0, cursor.getString(7), cursor.getInt(8) > 0, cursor.getString(9), cursor.getInt(10) > 0,
                                cursor.getString(11), cursor.getInt(12) > 0, cursor.getString(13), cursor.getInt(14) > 0);
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
            values.put(INDEX, question.getIndex());
            values.put(TITLE, question.getTitle());
            values.put(TYPE, question.getType());
            values.put(POINTS, question.getPoints());
            values.put(TEXT, question.getText());
            values.put(ANTWORT1, question.getAntwort1());
            values.put(RICHTIG1, question.getRichtig1());
            values.put(ANTWORT2, question.getAntwort2());
            values.put(RICHTIG2, question.getRichtig2());
            values.put(ANTWORT3, question.getAntwort3());
            values.put(RICHTIG3, question.getRichtig3());
            values.put(ANTWORT4, question.getAntwort4());
            values.put(RICHTIG4, question.getRichtig4());
            values.put(ANTWORT5, question.getAntwort5());
            values.put(RICHTIG5, question.getRichtig5());
            // updating row
            return db.update(TABLE_ENTRIES, values, INDEX + " = ?",
                    new String[]{String.valueOf(question.getIndex())});
        }
    }

    // Deleting single question
    public void deleteEntry(Question question) throws SQLiteException  {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_ENTRIES, INDEX + " = ?",
                    new String[]{String.valueOf(question.getIndex())});
        }
    }


    // Getting entries Count
    public int getEntriesCount() throws SQLiteException {
        Cursor cursor = null;
        String countQuery = "SELECT  * FROM " + TABLE_ENTRIES;
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            cursor = db.rawQuery(countQuery, null);
            // return count
            return cursor.getCount();
        } finally {
            cursor.close();
        }
    }

}