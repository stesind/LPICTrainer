package de.sindzinski.de.sindzinski.database;

/**
 * Created by steffen on 09.08.13.
 */

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import de.sindzinski.lpictrainer.Entry;


public class XmlParser {
    // We don't use namespaces
    private static final String ns = null;

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public ArrayList parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            //because all data is on same leven we have to read differnetly
            //return readFeed(parser);
            return readQuestions(parser);
        } finally {
            in.close();
        }
    }

    //reads the feed at same level, not on a sublevel
    private ArrayList readQuestions(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList entries = new ArrayList();

        Integer index = 0;
        String title = null;
        String type = null;
        Integer points = 0;
        String text = null;
        String antwort1 = null;
        Boolean richtig1 = null;
        String antwort2 = null;
        Boolean richtig2 = null;
        String antwort3 = null;
        Boolean richtig3 = null;
        String antwort4 = null;
        Boolean richtig4 = null;
        String antwort5 = null;
        Boolean richtig5 = null;
        String tag = null;

        parser.require(XmlPullParser.START_TAG, ns, "rootnode");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("fragetitel")) {
                title = readTitle(parser);
            } else if (name.equals("fragetyp")) {
                type = readType(parser);
            } else if (name.equals("punkte")) {
                points = Integer.parseInt(readPoints(parser));
            } else if (name.equals("fragetext")) {
                text = android.text.Html.fromHtml(readFragetext(parser)).toString();

            } else if ((type != null) && (type.equals("auswahl")) && (name.equals("antwort"))) {

                tag = parser.getName();
                richtig1 = parser.getAttributeValue(null, "richtig").equals("ja");
                //richtig1 = parser.getAttributeValue(null, "richtig");
                antwort1 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                richtig2 = parser.getAttributeValue(null, "richtig").equals("ja");
                //richtig2 = parser.getAttributeValue(null, "richtig");
                antwort2 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                richtig3 = parser.getAttributeValue(null, "richtig").equals("ja");
                //richtig3 = parser.getAttributeValue(null, "richtig");
                antwort3 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                richtig4 = parser.getAttributeValue(null, "richtig").equals("ja");
                //richtig4 = parser.getAttributeValue(null, "richtig");
                antwort4 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                richtig5 = parser.getAttributeValue(null, "richtig").equals("ja");
                //richtig5 = parser.getAttributeValue(null, "richtig");
                antwort5 = android.text.Html.fromHtml(readText(parser)).toString();

                index++;
                entries.add(new Entry(index, title, type, points, text, antwort1, richtig1, antwort2, richtig2, antwort3, richtig3, antwort4, richtig4, antwort5, richtig5));
                title = null;
                type = null;
                points = null;
                text = null;
                antwort1 = null;
                richtig1 = null;
                antwort2 = null;
                richtig2 = null;
                antwort3 = null;
                richtig3 = null;
                antwort4 = null;
                richtig4 = null;
                antwort5 = null;
                richtig5 = null;
            } else if ( (type != null) && (type.equals("text")) && (name.equals("antwort"))) {
                antwort1 = android.text.Html.fromHtml(readText(parser)).toString();

                index++;
                entries.add(new Entry(index, title, type, points, text, antwort1, richtig1, antwort2, richtig2, antwort3, richtig3, antwort4, richtig4, antwort5, richtig5));
                title = null;
                type = null;
                points = null;
                text = null;
                antwort1 = null;
                richtig1 = null;
                antwort2 = null;
                richtig2 = null;
                antwort3 = null;
                richtig3 = null;
                antwort4 = null;
                richtig4 = null;
                antwort5 = null;
                richtig5 = null;
            } else {
                skip(parser);
            }
        }
        return entries;
    }

/*    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "rootnode");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("fragetitel")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }*/

/*    public static class Entry {
        public final String title;
        public final String type;
        public final String points;
        public final String text;
        public final String antwort1;
        public final Boolean richtig1;
        public final String antwort2;
        public final Boolean richtig2;
        public final String antwort3;
        public final Boolean richtig3;
        public final String antwort4;
        public final Boolean richtig4;
        public final String antwort5;
        public final Boolean richtig5;
        // We don't use namespaces
        private static final String ns = null;

        private Entry(String title, String type, String points, String text, String antwort1, Boolean richtig1, String antwort2, Boolean richtig2, String antwort3, Boolean richtig3, String antwort4, Boolean richtig4, String antwort5, Boolean richtig5) {
            this.title = title;
            this.type = type;
            this.points = points;
            this.text = text;
            this.antwort1 = antwort1;
            this.richtig1 = richtig1;
            this.antwort2 = antwort2;
            this.richtig2 = richtig2;
            this.antwort3 = antwort3;
            this.richtig3 = richtig3;
            this.antwort4 = antwort4;
            this.richtig4 = richtig4;
            this.antwort5 = antwort5;
            this.richtig5 = richtig5;
        }
    }*/

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    /*private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "fragetitel");
        String title = null;
        String type = null;
        String points = null;
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("fragetitel")) {
                title = readTitle(parser);
            } else if (name.equals("fragetyp")) {
                type = readType(parser);
            } else if (name.equals("punkte")) {
                points = readPoints(parser);
            } else if (name.equals("fragetext")) {
                text = readFragetext(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(title, type, points, text);
    }
*/
    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "fragetitel");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "fragetitel");
        return title;
    }

    // Processes type tags in the feed.
    private String readType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "fragetyp");
        String type = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "fragetyp");
        return type;
    }

    // Processes points tags in the feed.
    private String readPoints(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "punkte");
        String points = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "punkte");
        return points;
    }
    // Processes text tags in the feed.
    private String readFragetext(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "fragetext");
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "fragetext");
        return text;
    }

    // Processes link tags in the feed.
    private String readAntwort(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "antwort");
        String antwort = readText(parser);
        String richtig = parser.getAttributeValue(null, "richtig");
        return antwort;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}