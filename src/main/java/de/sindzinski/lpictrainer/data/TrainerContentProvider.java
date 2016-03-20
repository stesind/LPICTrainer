package de.sindzinski.lpictrainer.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import de.sindzinski.util.Logger;

/**
 * Created by steffen on 01.01.16.
 */
public class TrainerContentProvider extends ContentProvider {

    private final String TAG = "TrainerContentProvider";
    // database
    private QuestionDatabaseHelper database;


    // used for the UriMacher
    private static final int QUESTIONS = 10;
    private static final int QUESTIONS_ID = 20;
    private static final int QUESTIONS_FROM_TO = 30;
    private static final int ANSWERS = 110;
    private static final int ANSWERS_ID = 120;
    private static final int ANSWERS_FROM_TO = 130;
    // Creates a UriMatcher object.
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        /*
        * The calls to addURI() go here, for all of the content URI patterns that the provider
        * should recognize. For this snippet, only the calls for table 3 are shown.
        */
       /*
        * Sets the integer value for multiple rows in table BASE_PATH to QUESTIONS. Notice that no wildcard is used
        * in the path
        */
        sURIMatcher.addURI(TrainerContract.QuestionEntry.AUTHORITY, TrainerContract.QuestionEntry.BASE_PATH, QUESTIONS);
        sURIMatcher.addURI(TrainerContract.QuestionEntry.AUTHORITY, TrainerContract.QuestionEntry.BASE_PATH + "/#", QUESTIONS_ID);
        sURIMatcher.addURI(TrainerContract.QuestionEntry.AUTHORITY, TrainerContract.QuestionEntry.BASE_PATH + "/#/#", QUESTIONS_FROM_TO);

        sURIMatcher.addURI(TrainerContract.AnswerEntry.AUTHORITY, TrainerContract.AnswerEntry.BASE_PATH, ANSWERS);
        sURIMatcher.addURI(TrainerContract.AnswerEntry.AUTHORITY, TrainerContract.AnswerEntry.BASE_PATH + "/#", ANSWERS_ID);
        sURIMatcher.addURI(TrainerContract.AnswerEntry.AUTHORITY, TrainerContract.AnswerEntry.BASE_PATH + "/#/#", ANSWERS_FROM_TO);

    }

    @Override
    public boolean onCreate() {
        database = new QuestionDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(TrainerContract.QuestionEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case QUESTIONS:
                //if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case QUESTIONS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(TrainerContract.QuestionEntry.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //selection = selection + "_ID = " uri.getLastPathSegment();
                break;
            case QUESTIONS_FROM_TO:
                //better to user selection and args in calling method
                queryBuilder.appendWhere( TrainerContract.QuestionEntry.COLUMN_ID + ">=" + uri.getPathSegments().get(uri.getPathSegments().size()-2));
                queryBuilder.appendWhere(" AND ");
                queryBuilder.appendWhere(TrainerContract.QuestionEntry.COLUMN_ID + "<=" + uri.getLastPathSegment());

/*                queryBuilder.appendWhere(TrainerContract.QuestionEntry.COLUMN_ID + "<="
                        + uri.getLastPathSegment());*/
                break;
            case ANSWERS:
                //if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case ANSWERS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(TrainerContract.AnswerEntry.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //selection = selection + "_ID = " uri.getLastPathSegment();
                break;
            case ANSWERS_FROM_TO:
                //better to user selection and args in calling method
                queryBuilder.appendWhere( TrainerContract.AnswerEntry.COLUMN_ID + ">=" + uri.getPathSegments().get(uri.getPathSegments().size()-2));
                queryBuilder.appendWhere(" AND ");
                queryBuilder.appendWhere(TrainerContract.AnswerEntry.COLUMN_ID + "<=" + uri.getLastPathSegment());

/*                queryBuilder.appendWhere(TrainerContract.QuestionEntry.COLUMN_ID + "<="
                        + uri.getLastPathSegment());*/
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = null;
            try {
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, null);
                // make sure that potential listeners are getting notified
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

            } catch (SQLiteException e) {
                Logger.e(TAG, "Error reading database: " + e);
            }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        Uri result;
        switch (uriType) {
            case QUESTIONS:
                id = sqlDB.insert(TrainerContract.QuestionEntry.TABLE_NAME, null, values);
                result = Uri.parse(TrainerContract.QuestionEntry.BASE_PATH + "/" + id);
                break;
            case ANSWERS:
                id = sqlDB.insert(TrainerContract.AnswerEntry.TABLE_NAME, null, values);
                result = Uri.parse(TrainerContract.QuestionEntry.BASE_PATH + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
            case QUESTIONS:
                rowsDeleted = sqlDB.delete(TrainerContract.QuestionEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case QUESTIONS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(TrainerContract.QuestionEntry.TABLE_NAME,
                            TrainerContract.QuestionEntry.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(TrainerContract.QuestionEntry.TABLE_NAME,
                            TrainerContract.QuestionEntry.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case ANSWERS:
                rowsDeleted = sqlDB.delete(TrainerContract.AnswerEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case ANSWERS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(TrainerContract.AnswerEntry.TABLE_NAME,
                            TrainerContract.AnswerEntry.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(TrainerContract.AnswerEntry.TABLE_NAME,
                            TrainerContract.AnswerEntry.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
            case QUESTIONS:
                rowsUpdated = sqlDB.update(TrainerContract.QuestionEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case QUESTIONS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TrainerContract.QuestionEntry.TABLE_NAME,
                            values,
                            TrainerContract.QuestionEntry.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TrainerContract.QuestionEntry.TABLE_NAME,
                            values,
                            TrainerContract.QuestionEntry.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case ANSWERS:
                rowsUpdated = sqlDB.update(TrainerContract.AnswerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ANSWERS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TrainerContract.AnswerEntry.TABLE_NAME,
                            values,
                            TrainerContract.AnswerEntry.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TrainerContract.AnswerEntry.TABLE_NAME,
                            values,
                            TrainerContract.AnswerEntry.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {
                TrainerContract.QuestionEntry._ID,
                TrainerContract.QuestionEntry.COLUMN_ID,
                TrainerContract.QuestionEntry.COLUMN_TITLE,
                TrainerContract.QuestionEntry.COLUMN_TYPE,
                TrainerContract.QuestionEntry.COLUMN_POINTS,
                TrainerContract.QuestionEntry.COLUMN_TEXT,
                TrainerContract.QuestionEntry.COLUMN_ANSWER1,
                TrainerContract.QuestionEntry.COLUMN_ANSWER1,
                TrainerContract.QuestionEntry.COLUMN_CORRECT1,
                TrainerContract.QuestionEntry.COLUMN_ANSWER2,
                TrainerContract.QuestionEntry.COLUMN_CORRECT2,
                TrainerContract.QuestionEntry.COLUMN_ANSWER3,
                TrainerContract.QuestionEntry.COLUMN_CORRECT3,
                TrainerContract.QuestionEntry.COLUMN_ANSWER4,
                TrainerContract.QuestionEntry.COLUMN_CORRECT4,
                TrainerContract.QuestionEntry.COLUMN_ANSWER5,
                TrainerContract.QuestionEntry.COLUMN_CORRECT5,
                TrainerContract.AnswerEntry.COLUMN_ID,
                TrainerContract.AnswerEntry.COLUMN_POINTS,
                TrainerContract.AnswerEntry.COLUMN_CHECKED,
                TrainerContract.AnswerEntry.COLUMN_ANSWER,
                TrainerContract.AnswerEntry.COLUMN_ANSWER1,
                TrainerContract.AnswerEntry.COLUMN_ANSWER2,
                TrainerContract.AnswerEntry.COLUMN_ANSWER3,
                TrainerContract.AnswerEntry.COLUMN_ANSWER4,
                TrainerContract.AnswerEntry.COLUMN_ANSWER5
                 };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
