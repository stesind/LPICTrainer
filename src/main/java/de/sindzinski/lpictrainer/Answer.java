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

    public Answer(Integer index, Boolean checked, String antwort, Boolean richtig1, Boolean richtig2, Boolean richtig3, Boolean richtig4, Boolean richtig5) {
        this.index = index;
        this.checked = checked;
        this.antwort = antwort;
        this.richtig1 = richtig1;
        this.richtig2 = richtig2;
        this.richtig3 = richtig3;
        this.richtig4 = richtig4;
        this.richtig5 = richtig5;
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
            return new Answer(in);
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
