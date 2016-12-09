package de.sindzinski.lpictrainer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by steffen on 01.01.16.
 */
public class TrainerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lpicTrainer.db";
    private static final int DATABASE_VERSION = 1;

    public TrainerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        QuestionTable.onCreate(database);
        AnswerTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        QuestionTable.onUpgrade(database, oldVersion, newVersion);
        AnswerTable.onUpgrade(database, oldVersion, newVersion);
    }


}

