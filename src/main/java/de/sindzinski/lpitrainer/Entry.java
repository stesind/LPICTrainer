package de.sindzinski.lpitrainer;

/**
 * Created by steffen on 13.08.13.
 */
public class Entry {

    Integer index;
    String title;
    String type;
    Integer points;
    String text;
    String antwort1;
    Boolean richtig1;
    String antwort2;
    Boolean richtig2;
    String antwort3;
    Boolean richtig3;
    String antwort4;
    Boolean richtig4;
    String antwort5;
    Boolean richtig5;

    public Entry () {
    };

    public Entry(Integer index, String title, String type, Integer points, String text, String antwort1, Boolean richtig1, String antwort2, Boolean richtig2, String antwort3, Boolean richtig3, String antwort4, Boolean richtig4, String antwort5, Boolean richtig5) {
        this.index = index;
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

    public Integer getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Integer getPoints() {
        return points;
    }

    public String getText() {
        return text;
    }

    public String getAntwort1() {
        return antwort1;
    }

    public Boolean getRichtig1() {
        return richtig1;
    }

    public String getAntwort2() {
        return antwort2;
    }

    public Boolean getRichtig2() {
        return richtig2;
    }

    public String getAntwort3() {
        return antwort3;
    }

    public Boolean getRichtig3() {
        return richtig3;
    }

    public String getAntwort4() {
        return antwort4;
    }

    public Boolean getRichtig4() {
        return richtig4;
    }

    public String getAntwort5() {
        return antwort5;
    }

    public Boolean getRichtig5() {
        return richtig5;
    }
}
