package de.sindzinski.lpictrainer;

import android.os.Parcelable;
import android.os.Parcel;
/**
 * Created by steffen on 04.10.13.
 */
public class Answer implements Parcelable {
    public Integer index = 0;
    public boolean checked;
    public String antwort;
    public boolean richtig1;
    public boolean richtig2;
    public boolean richtig3;
    public boolean richtig4;
    public boolean richtig5;

/*    public Answer(Integer index, Boolean checked, String antwort, Boolean richtig1, Boolean richtig2, Boolean richtig3, Boolean richtig4, Boolean richtig5) {
        this.index = index;
        this.checked = checked;
        this.antwort = antwort;
        this.richtig1 = richtig1;
        this.richtig2 = richtig2;
        this.richtig3 = richtig3;
        this.richtig4 = richtig4;
        this.richtig5 = richtig5;
    }*/
private Answer(Builder builder) {
    this.index = builder.index;
    this.checked = builder.checked;
    this.antwort = builder.antwort;
    this.richtig1 = builder.richtig1;
    this.richtig2 = builder.richtig2;
    this.richtig3 = builder.richtig3;
    this.richtig4 = builder.richtig4;
    this.richtig5 = builder.richtig5;
}
    //builder pattern public static class
    public static class Builder {
        private Integer index;
        private Boolean checked;
        private String antwort;
        private Boolean richtig1;
        private Boolean richtig2;
        private Boolean richtig3;
        private Boolean richtig4;
        private Boolean richtig5;
        private Parcel in;

        public Builder setIndex(Integer index) {
            this.index = index;
            return this;
        }

        public Builder setChecked(Boolean checked) {
            this.checked = checked;
            return this;
        }

        public Builder setAntwort(String antwort) {
            this.antwort = antwort;
            return this;
        }

        public Builder setRichtig1(Boolean richtig1) {
            this.richtig1 = richtig1;
            return this;
        }

        public Builder setRichtig2(Boolean richtig2) {
            this.richtig2 = richtig2;
            return this;
        }

        public Builder setRichtig3(Boolean richtig3) {
            this.richtig3 = richtig3;
            return this;
        }

        public Builder setRichtig4(Boolean richtig4) {
            this.richtig4 = richtig4;
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

        public Answer build() {
            return new Answer(this);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(index);
        out.writeByte((byte) (checked ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeString(antwort);
        out.writeByte((byte) (richtig1 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (richtig2 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (richtig3 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (richtig4 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (richtig5 ? 1 : 0));     //if myBoolean == true, byte == 1
    }

    public static final Answer.Creator<Answer> CREATOR
            = new Parcelable.Creator<Answer>() {
        public Answer createFromParcel(Parcel in) {
            return new Builder().setIn(in).build();
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    private Answer(Parcel in) {

        index=in.readInt();
        checked = in.readByte() == 1;     //myBoolean == true if byte == 1
        antwort = in.readString();
        richtig1 = in.readByte() == 1;     //myBoolean == true if byte == 1
        richtig2 = in.readByte() == 1;     //myBoolean == true if byte == 1
        richtig3 = in.readByte() == 1;     //myBoolean == true if byte == 1
        richtig4 = in.readByte() == 1;     //myBoolean == true if byte == 1
        richtig5 = in.readByte() == 1;     //myBoolean == true if byte == 1
    }

    /*    public static class Answer implements Serializable {

        public Integer index = 0;
        public boolean checked;
        public String antwort;
        public boolean richtig1;
        public boolean richtig2;
        public boolean richtig3;
        public boolean richtig4;
        public boolean richtig5;

        private Answer(Integer index, Boolean checked, String antwort, Boolean richtig1, Boolean richtig2, Boolean richtig3, Boolean richtig4, Boolean richtig5) {
            this.index = index;
            this.checked = checked;
            this.antwort = antwort;
            this.richtig1 = richtig1;
            this.richtig2 = richtig2;
            this.richtig3 = richtig3;
            this.richtig4 = richtig4;
            this.richtig5 = richtig5;
        }
    }*/
}
