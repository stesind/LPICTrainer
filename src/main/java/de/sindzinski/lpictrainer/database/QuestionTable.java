package de.sindzinski.lpictrainer.database;

import android.database.sqlite.SQLiteDatabase;

import de.sindzinski.helper.Logger;

/**
 * Created by steffen on 01.01.16.
 */
public class QuestionTable {
    // table name
    public static final String TABLE_QUESTION = "questions";
    // Database table
    public static final String COLUMN_ID ="ID";
    public static final String COLUMN_TITLE ="title";
    public static final String COLUMN_TYPE ="type";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_ANTWORT1 = "antwort1";
    public static final String COLUMN_RICHTIG1 = "richtig1";
    public static final String COLUMN_ANTWORT2 = "antwort2";
    public static final String COLUMN_RICHTIG2 = "richtig2";
    public static final String COLUMN_ANTWORT3 = "antwort3";
    public static final String COLUMN_RICHTIG3 = "richtig3";
    public static final String COLUMN_ANTWORT4 = "antwort4";
    public static final String COLUMN_RICHTIG4 = "richtig4";
    public static final String COLUMN_ANTWORT5 = "antwort5";
    public static final String COLUMN_RICHTIG5 = "richtig5";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_QUESTION + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_TYPE + " TEXT, "
            + COLUMN_POINTS + " INTEGER, "
            + COLUMN_TEXT + " TEXT, "
            + COLUMN_ANTWORT1 + " TEXT, "
            + COLUMN_RICHTIG1 + " INTEGER, "
            + COLUMN_ANTWORT2 + " TEXT, "
            + COLUMN_RICHTIG2 + " INTEGER, "
            + COLUMN_ANTWORT3 + " TEXT, "
            + COLUMN_RICHTIG3 + " INTEGER, "
            + COLUMN_ANTWORT4 + " TEXT, "
            + COLUMN_RICHTIG4 + " INTEGER, "
            + COLUMN_ANTWORT5 + " TEXT, "
            + COLUMN_RICHTIG5 + " INTEGER "
            + ")";
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Logger.w(QuestionTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        onCreate(database);
    }
}
