package de.sindzinski.lpictrainer.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by steffen on 18.02.16.
 */
public class TrainerContract {

    public static final class QuestionEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "questions";

        // Contacts QuestionTable Columns names

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

        public static final String AUTHORITY = "de.sindzinski.lpictrainer.provider";

        public static final String BASE_PATH = "questions";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/" + BASE_PATH);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/questions";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/questions";



    }

}
