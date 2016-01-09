package de.sindzinski.lpictrainer.data;

import android.os.Parcelable;
import android.os.Parcel;
/**
 * Created by steffen on 13.08.13.
 */
public class Question implements Parcelable {

    public Integer index;
    public String title;
    public String type;
    public Integer points;
    public String text;
    public String antwort1;
    public Boolean richtig1;
    public String antwort2;
    public Boolean richtig2;
    public String antwort3;
    public Boolean richtig3;
    public String antwort4;
    public Boolean richtig4;
    public String antwort5;
    public Boolean richtig5;

    private Question(Builder questionBuilder) {
        this.index = questionBuilder.index;
        this.title = questionBuilder.title;
        this.type = questionBuilder.type;
        this.points = questionBuilder.points;
        this.text = questionBuilder.text;
        this.antwort1 = questionBuilder.antwort1;
        this.richtig1 = questionBuilder.richtig1;
        this.antwort2 = questionBuilder.antwort2;
        this.richtig2 = questionBuilder.richtig2;
        this.antwort3 = questionBuilder.antwort3;
        this.richtig3 = questionBuilder.richtig3;
        this.antwort4 = questionBuilder.antwort4;
        this.richtig4 = questionBuilder.richtig4;
        this.antwort5 = questionBuilder.antwort5;
        this.richtig5 = questionBuilder.richtig5;
    }

 /*   private Question(Integer index, String title, String type, Integer points, String text, String antwort1, Boolean richtig1, String antwort2, Boolean richtig2, String antwort3, Boolean richtig3, String antwort4, Boolean richtig4, String antwort5, Boolean richtig5) {
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
    }*/

    //builder pattern static class
    public static class Builder {
        private Integer index;
        private String title;
        private String type;
        private Integer points;
        private String text;
        private String antwort1;
        private Boolean richtig1;
        private String antwort2;
        private Boolean richtig2;
        private String antwort3;
        private Boolean richtig3;
        private String antwort4;
        private Boolean richtig4;
        private String antwort5;
        private Boolean richtig5;
        private Parcel in;

        public Builder setIndex(Integer index) {
            this.index = index;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPoints(Integer points) {
            this.points = points;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setAntwort1(String antwort1) {
            this.antwort1 = antwort1;
            return this;
        }

        public Builder setRichtig1(Boolean richtig1) {
            this.richtig1 = richtig1;
            return this;
        }

        public Builder setAntwort2(String antwort2) {
            this.antwort2 = antwort2;
            return this;
        }

        public Builder setRichtig2(Boolean richtig2) {
            this.richtig2 = richtig2;
            return this;
        }

        public Builder setAntwort3(String antwort3) {
            this.antwort3 = antwort3;
            return this;
        }

        public Builder setRichtig3(Boolean richtig3) {
            this.richtig3 = richtig3;
            return this;
        }

        public Builder setAntwort4(String antwort4) {
            this.antwort4 = antwort4;
            return this;
        }

        public Builder setRichtig4(Boolean richtig4) {
            this.richtig4 = richtig4;
            return this;
        }

        public Builder setAntwort5(String antwort5) {
            this.antwort5 = antwort5;
            return this;
        }

        public Builder setRichtig5(Boolean richtig5) {
            this.richtig5 = richtig5;
            return this;
        }

        public Builder setIn(Parcel in) {
            this.in = in;
            return this;
        }

        public Question build() {
            return new Question(this);
        }
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

    public static final Parcelable.Creator<Question> CREATOR
            = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Builder().setIn(in).build();
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    //read from parcel
    private Question(Parcel in) {
        index=in.readInt();
        title = in.readString();
        type = in.readString();
        points=in.readInt();
        text = in.readString();
        antwort1 = in.readString();
        richtig1 = in.readByte() == 1;     //myBoolean == true if byte == 1
        antwort2 = in.readString();
        richtig2 = in.readByte() == 1;     //myBoolean == true if byte == 1
        antwort3 = in.readString();
        richtig3 = in.readByte() == 1;     //myBoolean == true if byte == 1
        antwort4 = in.readString();
        richtig4 = in.readByte() == 1;     //myBoolean == true if byte == 1
        antwort5 = in.readString();
        richtig5 = in.readByte() == 1;     //myBoolean == true if byte == 1
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
