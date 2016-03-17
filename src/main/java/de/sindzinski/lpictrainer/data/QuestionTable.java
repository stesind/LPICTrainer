package de.sindzinski.lpictrainer.data;

import android.database.sqlite.SQLiteDatabase;

import de.sindzinski.util.Logger;

/**
 * Created by steffen on 01.01.16.
 */
public class QuestionTable {

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TrainerContract.QuestionEntry.TABLE_NAME + "("
            + TrainerContract.QuestionEntry._ID + " INTEGER PRIMARY KEY, "
            + TrainerContract.QuestionEntry.COLUMN_ID + " INTEGER , "
            + TrainerContract.QuestionEntry.COLUMN_TITLE + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_TYPE + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_POINTS + " INTEGER, "
            + TrainerContract.QuestionEntry.COLUMN_TEXT + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_ANTWORT1 + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_RICHTIG1 + " INTEGER, "
            + TrainerContract.QuestionEntry.COLUMN_ANTWORT2 + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_RICHTIG2 + " INTEGER, "
            + TrainerContract.QuestionEntry.COLUMN_ANTWORT3 + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_RICHTIG3 + " INTEGER, "
            + TrainerContract.QuestionEntry.COLUMN_ANTWORT4 + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_RICHTIG4 + " INTEGER, "
            + TrainerContract.QuestionEntry.COLUMN_ANTWORT5 + " TEXT, "
            + TrainerContract.QuestionEntry.COLUMN_RICHTIG5 + " INTEGER "
            + ")";
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Logger.w(QuestionTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TrainerContract.QuestionEntry.TABLE_NAME);
        onCreate(database);
    }
}
