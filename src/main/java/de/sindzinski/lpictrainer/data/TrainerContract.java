package de.sindzinski.lpictrainer.data;

import android.provider.BaseColumns;

/**
 * Created by steffen on 18.02.16.
 */
public class TrainerContract {

    public static final class QuestionEntry implements BaseColumns {
        // Contacts QuestionTable Columns names
        public static final String INDEX ="ID";
        public static final String TITLE ="title";
        public static final String TYPE ="type";
        public static final String POINTS = "points";
        public static final String TEXT = "text";
        public static final String ANTWORT1 = "antwort1";
        public static final String RICHTIG1 = "richtig1";
        public static final String ANTWORT2 = "antwort2";
        public static final String RICHTIG2 = "richtig2";
        public static final String ANTWORT3 = "antwort3";
        public static final String RICHTIG3 = "richtig3";
        public static final String ANTWORT4 = "antwort4";
        public static final String RICHTIG4 = "richtig4";
        public static final String ANTWORT5 = "antwort5";
        public static final String RICHTIG5 = "richtig5";
    }

}
