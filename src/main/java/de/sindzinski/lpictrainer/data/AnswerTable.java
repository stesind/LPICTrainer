package de.sindzinski.lpictrainer.data;

import android.database.sqlite.SQLiteDatabase;

import de.sindzinski.util.Logger;

/**
 * Created by steffen on 01.01.16.
 */
public class AnswerTable {

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TrainerContract.AnswerEntry.TABLE_NAME + "("
            + TrainerContract.AnswerEntry._ID + " INTEGER PRIMARY KEY, "
            + TrainerContract.AnswerEntry.COLUMN_ID + " INTEGER , "
            + TrainerContract.AnswerEntry.COLUMN_CHECKED + " TEXT, "
            + TrainerContract.AnswerEntry.COLUMN_POINTS + " INTEGER, "
            + TrainerContract.AnswerEntry.COLUMN_ANSWER + " TEXT, "
            + TrainerContract.AnswerEntry.COLUMN_ANSWER1 + " TEXT, "
            + TrainerContract.AnswerEntry.COLUMN_ANSWER2 + " TEXT, "
            + TrainerContract.AnswerEntry.COLUMN_ANSWER3 + " TEXT, "
            + TrainerContract.AnswerEntry.COLUMN_ANSWER4 + " TEXT, "
            + TrainerContract.AnswerEntry.COLUMN_ANSWER5 + " TEXT, "
            + ")";
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Logger.w(AnswerTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TrainerContract.AnswerEntry.TABLE_NAME);
        onCreate(database);
    }
}
