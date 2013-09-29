package de.sindzinski.lpitrainer;

import android.os.Parcelable;
import android.os.Parcel;
/**
 * Created by steffen on 13.08.13.
 */
public class Entry implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {

        outParcel.writeInt(index);
        outParcel.writeString(title);
        outParcel.writeString(type);
        outParcel.writeInt(points);
        outParcel.writeString(text);
        outParcel.writeString(antwort1);
        outParcel.writeByte((byte) (richtig1 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(antwort2);
        outParcel.writeByte((byte) (richtig2 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(antwort3);
        outParcel.writeByte((byte) (richtig3 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(antwort4);
        outParcel.writeByte((byte) (richtig4 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(antwort5);
        outParcel.writeByte((byte) (richtig5 ? 1 : 0));     //if myBoolean == true, byte == 1
        //myBoolean = in.readByte() == 1;     //myBoolean == true if byte == 1
    }

    public void readFromParcel(Parcel inParcel, int flags) {

        index=inParcel.readInt();
        title = inParcel.readString();
        type = inParcel.readString();
        points=inParcel.readInt();
        text = inParcel.readString();
        antwort1 = inParcel.readString();
        richtig1 = inParcel.readByte() == 1;     //myBoolean == true if byte == 1
        antwort2 = inParcel.readString();
        richtig2 = inParcel.readByte() == 1;     //myBoolean == true if byte == 1
        antwort3 = inParcel.readString();
        richtig3 = inParcel.readByte() == 1;     //myBoolean == true if byte == 1
        antwort4 = inParcel.readString();
        richtig4 = inParcel.readByte() == 1;     //myBoolean == true if byte == 1
        antwort5 = inParcel.readString();
        richtig5 = inParcel.readByte() == 1;     //myBoolean == true if byte == 1

    }
}
