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

import de.sindzinski.helper.Logger;

/**
 * Created by steffen on 01.01.16.
 */
public class QuestionContentProvider extends ContentProvider {

    private final String TAG = "QuestionContentProvider";
    // database
    private QuestionDatabaseHelper database;

    private static final String AUTHORITY = "de.sindzinski.lpictrainer.provider";

    private static final String BASE_PATH = "questions";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/questions";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/questions";

    // used for the UriMacher
    private static final int QUESTIONS = 10;
    private static final int QUESTIONS_ID = 20;
    private static final int QUESTIONS_FROM_TO = 30;
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
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, QUESTIONS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", QUESTIONS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#/#", QUESTIONS_FROM_TO);
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
        queryBuilder.setTables(QuestionTable.TABLE_QUESTION);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case QUESTIONS:
                //if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case QUESTIONS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(QuestionTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //selection = selection + "_ID = " uri.getLastPathSegment();
                break;
            case QUESTIONS_FROM_TO:
                //better to user selection and args in calling method
                queryBuilder.appendWhere( QuestionTable.COLUMN_ID + ">=" + uri.getPathSegments().get(uri.getPathSegments().size()-2));
                queryBuilder.appendWhere(" AND ");
                queryBuilder.appendWhere(QuestionTable.COLUMN_ID + "<=" + uri.getLastPathSegment());

/*                queryBuilder.appendWhere(QuestionTable.COLUMN_ID + "<="
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
        switch (uriType) {
            case QUESTIONS:
                id = sqlDB.insert(QuestionTable.TABLE_QUESTION, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case QUESTIONS:
                rowsDeleted = sqlDB.delete(QuestionTable.TABLE_QUESTION, selection,
                        selectionArgs);
                break;
            case QUESTIONS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(QuestionTable.TABLE_QUESTION,
                            QuestionTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(QuestionTable.TABLE_QUESTION,
                            QuestionTable.COLUMN_ID + "=" + id
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
        switch (uriType) {
            case QUESTIONS:
                rowsUpdated = sqlDB.update(QuestionTable.TABLE_QUESTION,
                        values,
                        selection,
                        selectionArgs);
                break;
            case QUESTIONS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(QuestionTable.TABLE_QUESTION,
                            values,
                            QuestionTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(QuestionTable.TABLE_QUESTION,
                            values,
                            QuestionTable.COLUMN_ID + "=" + id
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
                QuestionTable.COLUMN_TITLE,
                QuestionTable.COLUMN_TYPE,
                QuestionTable.COLUMN_POINTS,
                QuestionTable.COLUMN_TEXT,
                QuestionTable.COLUMN_ANTWORT1,
                QuestionTable.COLUMN_RICHTIG1,
                QuestionTable.COLUMN_ANTWORT2,
                QuestionTable.COLUMN_RICHTIG2,
                QuestionTable.COLUMN_ANTWORT3,
                QuestionTable.COLUMN_RICHTIG3,
                QuestionTable.COLUMN_ANTWORT4,
                QuestionTable.COLUMN_RICHTIG4,
                QuestionTable.COLUMN_ANTWORT5,
                QuestionTable.COLUMN_RICHTIG5,
                QuestionTable.COLUMN_ID };
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
