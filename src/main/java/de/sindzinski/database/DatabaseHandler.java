package de.sindzinski.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;

import de.sindzinski.lpictrainer.Entry;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
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


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    public void onWipe() throws SQLiteException {
        // Drop older table if existed

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);

        // Create tables again
        onCreate(db);

    }

    // Adding new entry
    public void addEntry(Entry entry) throws SQLiteException {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INDEX, entry.getIndex());
        values.put(TITLE, entry.getTitle());
        values.put(TYPE, entry.getType());
        values.put(POINTS, entry.getPoints());
        values.put(TEXT, entry.getText());
        values.put(ANTWORT1, entry.getAntwort1());
        values.put(RICHTIG1, entry.getRichtig1());
        values.put(ANTWORT2, entry.getAntwort2());
        values.put(RICHTIG2, entry.getRichtig2());
        values.put(ANTWORT3, entry.getAntwort3());
        values.put(RICHTIG3, entry.getRichtig3());
        values.put(ANTWORT4, entry.getAntwort4());
        values.put(RICHTIG4, entry.getRichtig4());
        values.put(ANTWORT5, entry.getAntwort5());
        values.put(RICHTIG5, entry.getRichtig5());
        // Inserting Row
        db.insert(TABLE_ENTRIES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single entry
    Entry getEntry(int id) throws SQLiteException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ENTRIES, new String[] { INDEX,
                TITLE, TYPE, POINTS, TEXT, ANTWORT1, RICHTIG1, ANTWORT2, RICHTIG2, ANTWORT3, RICHTIG3, ANTWORT4, RICHTIG4, ANTWORT5, RICHTIG5 }, INDEX + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Entry entry = new Entry(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                cursor.getString(4), cursor.getString(5), cursor.getInt(6)>0, cursor.getString(7), cursor.getInt(8)>0, cursor.getString(9),cursor.getInt(10)>0,
                cursor.getString(11),cursor.getInt(12)>0, cursor.getString(13),cursor.getInt(14)>0);
        return entry;
    }

    // Getting All Entries
    public List<Entry> getAllEntries() throws SQLiteException {
        List<Entry> entrytList = new ArrayList<Entry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4), cursor.getString(5), cursor.getInt(6)>0, cursor.getString(7), cursor.getInt(8)>0, cursor.getString(9),cursor.getInt(10)>0,
                        cursor.getString(11),cursor.getInt(12)>0, cursor.getString(13),cursor.getInt(14)>0);
                // Adding contact to list
                entrytList.add(entry);
            } while (cursor.moveToNext());
        }

        // return contact list
        return entrytList;
    }

    // Updating single entry
    public int updateEntry(Entry entry) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INDEX, entry.getIndex());
        values.put(TITLE, entry.getTitle());
        values.put(TYPE, entry.getType());
        values.put(POINTS, entry.getPoints());
        values.put(TEXT, entry.getText());
        values.put(ANTWORT1, entry.getAntwort1());
        values.put(RICHTIG1, entry.getRichtig1());
        values.put(ANTWORT2, entry.getAntwort2());
        values.put(RICHTIG2, entry.getRichtig2());
        values.put(ANTWORT3, entry.getAntwort3());
        values.put(RICHTIG3, entry.getRichtig3());
        values.put(ANTWORT4, entry.getAntwort4());
        values.put(RICHTIG4, entry.getRichtig4());
        values.put(ANTWORT5, entry.getAntwort5());
        values.put(RICHTIG5, entry.getRichtig5());
        // updating row
        return db.update(TABLE_ENTRIES, values, INDEX + " = ?",
                new String[] { String.valueOf(entry.getIndex()) });
    }

    // Deleting single entry
    public void deleteEntry(Entry entry) throws SQLiteException  {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRIES, INDEX + " = ?",
                new String[] { String.valueOf(entry.getIndex()) });
        db.close();
    }


    // Getting entries Count
    public int getEntriesCount() throws SQLiteException {
        String countQuery = "SELECT  * FROM " + TABLE_ENTRIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}