package de.sindzinski.lpictrainer.data;

/**
 * Created by steffen on 09.08.13.
 */

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.provider.BaseColumns;

import de.sindzinski.lpictrainer.data.Question;


public class XmlParser {
    // We don't use namespaces
    private static final String ns = null;

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "question";
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
        String answer1 = null;
        Boolean correct1 = null;
        String answer2 = null;
        Boolean correct2 = null;
        String answer3 = null;
        Boolean correct3 = null;
        String answer4 = null;
        Boolean correct4 = null;
        String answer5 = null;
        Boolean correct5 = null;
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
                correct1 = parser.getAttributeValue(null, "richtig").equals("ja");
                //correct1 = parser.getAttributeValue(null, "correct");
                answer1 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                correct2 = parser.getAttributeValue(null, "richtig").equals("ja");
                //correct2 = parser.getAttributeValue(null, "correct");
                answer2 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                correct3 = parser.getAttributeValue(null, "richtig").equals("ja");
                //correct3 = parser.getAttributeValue(null, "correct");
                answer3 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                correct4 = parser.getAttributeValue(null, "richtig").equals("ja");
                //correct4 = parser.getAttributeValue(null, "correct");
                answer4 = android.text.Html.fromHtml(readText(parser)).toString();
                do {
                    parser.next();
                } while (parser.getEventType() != XmlPullParser.START_TAG);
                tag = parser.getName();
                correct5 = parser.getAttributeValue(null, "richtig").equals("ja");
                //correct5 = parser.getAttributeValue(null, "correct");
                answer5 = android.text.Html.fromHtml(readText(parser)).toString();

                index++;
                entries.add(new Question.Builder()
                        .setIndex(index)
                        .setTitle(title)
                        .setType(type)
                        .setPoints(points)
                        .setText(text)
                        .setAnswer1(answer1)
                        .setCorrect1(correct1)
                        .setAnswer2(answer2)
                        .setCorrect2(correct2)
                        .setAnswer3(answer3)
                        .setCorrect3(correct3)
                        .setAnswer4(answer4)
                        .setCorrect4(correct4)
                        .setAnswer5(answer5)
                        .setCorrect5(correct5)
                        .build());
                title = null;
                type = null;
                points = null;
                text = null;
                answer1 = null;
                correct1 = null;
                answer2 = null;
                correct2 = null;
                answer3 = null;
                correct3 = null;
                answer4 = null;
                correct4 = null;
                answer5 = null;
                correct5 = null;
            } else if ( (type != null) && (type.equals("text")) && (name.equals("antwort"))) {
                answer1 = android.text.Html.fromHtml(readText(parser)).toString();

                index++;
                entries.add(new Question.Builder()
                        .setIndex(index)
                        .setTitle(title)
                        .setType(type)
                        .setPoints(points)
                        .setText(text)
                        .setAnswer1(answer1)
                        .setCorrect1(correct1)
                        .setAnswer2(answer2)
                        .setCorrect2(correct2)
                        .setAnswer3(answer3)
                        .setCorrect3(correct3)
                        .setAnswer4(answer4)
                        .setCorrect4(correct4)
                        .setAnswer5(answer5)
                        .setCorrect5(correct5)
                        .build());
                title = null;
                type = null;
                points = null;
                text = null;
                answer1 = null;
                correct1 = null;
                answer2 = null;
                correct2 = null;
                answer3 = null;
                correct3 = null;
                answer4 = null;
                correct4 = null;
                answer5 = null;
                correct5 = null;
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
            // Starts by looking for the question tag
            if (name.equals("fragetitel")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }*/

/*    public static class Question {
        public final String title;
        public final String type;
        public final String points;
        public final String text;
        public final String answer1;
        public final Boolean correct1;
        public final String answer2;
        public final Boolean correct2;
        public final String answer3;
        public final Boolean correct3;
        public final String answer4;
        public final Boolean correct4;
        public final String answer5;
        public final Boolean correct5;
        // We don't use namespaces
        private static final String ns = null;

        private Question(String title, String type, String points, String text, String answer1, Boolean correct1, String answer2, Boolean correct2, String answer3, Boolean correct3, String answer4, Boolean correct4, String answer5, Boolean correct5) {
            this.title = title;
            this.type = type;
            this.points = points;
            this.text = text;
            this.answer1 = answer1;
            this.correct1 = correct1;
            this.answer2 = answer2;
            this.correct2 = correct2;
            this.answer3 = answer3;
            this.correct3 = correct3;
            this.answer4 = answer4;
            this.correct4 = correct4;
            this.answer5 = answer5;
            this.correct5 = correct5;
        }
    }*/

    // Parses the contents of an question. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    /*private Question readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
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
        return new Question(title, type, points, text);
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
    private String readANSWER(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "answer");
        String answer = readText(parser);
        String correct = parser.getAttributeValue(null, "correct");
        return answer;
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