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
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_ANSWER1 = "answer1";
        public static final String COLUMN_CORRECT1 = "correct1";
        public static final String COLUMN_ANSWER2 = "answer2";
        public static final String COLUMN_CORRECT2 = "correct2";
        public static final String COLUMN_ANSWER3 = "answer3";
        public static final String COLUMN_CORRECT3 = "correct3";
        public static final String COLUMN_ANSWER4 = "answer4";
        public static final String COLUMN_CORRECT4 = "correct4";
        public static final String COLUMN_ANSWER5 = "answer5";
        public static final String COLUMN_CORRECT5 = "correct5";

        public static final String AUTHORITY = "de.sindzinski.lpictrainer.provider";

        public static final String BASE_PATH = "questions";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/" + BASE_PATH);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/questions";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/questions";

    }
    public static final class AnswerEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "answers";

        // Contacts AnswerTable Columns names
        public Integer index = 0;
        public boolean checked;
        public Integer points;
        public String answer;
        public boolean correct1;
        public boolean correct2;
        public boolean correct3;
        public boolean correct4;
        public boolean correct5;

        public static final String COLUMN_ID ="ID";
        public static final String COLUMN_POINTS ="points";
        public static final String COLUMN_CHECKED ="checked";
        public static final String COLUMN_ANSWER ="answer";
        public static final String COLUMN_ANSWER1 = "answer1";
        public static final String COLUMN_ANSWER2 = "answer2";
        public static final String COLUMN_ANSWER3 = "answer3";
        public static final String COLUMN_ANSWER4 = "answer4";
        public static final String COLUMN_ANSWER5 = "answer5";

        public static final String AUTHORITY = "de.sindzinski.lpictrainer.provider";

        public static final String BASE_PATH = "answers";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/" + BASE_PATH);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/answers";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/answers";

    }
}
