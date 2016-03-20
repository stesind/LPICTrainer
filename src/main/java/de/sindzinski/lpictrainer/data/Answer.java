package de.sindzinski.lpictrainer.data;

import android.os.Parcelable;
import android.os.Parcel;
/**
 * Created by steffen on 04.10.13.
 */
public class Answer implements Parcelable {
    public Integer index = 0;
    public boolean checked;
    public Integer points;
    public String answer;
    public boolean answer1;
    public boolean answer2;
    public boolean answer3;
    public boolean answer4;
    public boolean answer5;

/*    public ANSWER(Integer index, Boolean checked, String ANSWER, Boolean ANSWER1, Boolean ANSWER2, Boolean ANSWER3, Boolean ANSWER4, Boolean ANSWER5) {
        this.index = index;
        this.checked = checked;
        this.ANSWER = ANSWER;
        this.ANSWER1 = ANSWER1;
        this.ANSWER2 = ANSWER2;
        this.ANSWER3 = ANSWER3;
        this.ANSWER4 = ANSWER4;
        this.ANSWER5 = ANSWER5;
    }*/
private Answer(Builder builder) {
    this.index = builder.index;
    this.checked = builder.checked;
    this.points = builder.points;
    this.answer = builder.answer;
    this.answer1 = builder.answer1;
    this.answer2 = builder.answer2;
    this.answer3 = builder.answer3;
    this.answer4 = builder.answer4;
    this.answer5 = builder.answer5;
}
    //builder pattern public static class
    public static class Builder {
        private Integer index;
        private Boolean checked;
        private Integer points;
        private String answer;
        private Boolean answer1;
        private Boolean answer2;
        private Boolean answer3;
        private Boolean answer4;
        private Boolean answer5;
        private Parcel in;

        public Builder setIndex(Integer index) {
            this.index = index;
            return this;
        }

        public Builder setChecked(Boolean checked) {
            this.checked = checked;
            return this;
        }

        public Builder setPoints(Integer points) {
            this.points = points;
            return this;
        }
        public Builder setAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        public Builder setAnswer1(Boolean answer1) {
            this.answer1 = answer1;
            return this;
        }

        public Builder setAnswer2(Boolean answer2) {
            this.answer2 = answer2;
            return this;
        }

        public Builder setAnswer3(Boolean answer3) {
            this.answer3 = answer3;
            return this;
        }

        public Builder setAnswer4(Boolean answer4) {
            this.answer4 = answer4;
            return this;
        }

        public Builder setAnswer5(Boolean answer5) {
            this.answer5 = answer5;
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
        out.writeInt(points);
        out.writeString(answer);
        out.writeByte((byte) (answer1 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (answer2 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (answer3 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (answer4 ? 1 : 0));     //if myBoolean == true, byte == 1
        out.writeByte((byte) (answer5 ? 1 : 0));     //if myBoolean == true, byte == 1
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
        points=in.readInt();
        answer = in.readString();
        answer1 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer2 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer3 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer4 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer5 = in.readByte() == 1;     //myBoolean == true if byte == 1
    }

    /*    public static class ANSWER implements Serializable {

        public Integer index = 0;
        public boolean checked;
        public String ANSWER;
        public boolean ANSWER1;
        public boolean ANSWER2;
        public boolean ANSWER3;
        public boolean ANSWER4;
        public boolean ANSWER5;

        private ANSWER(Integer index, Boolean checked, String ANSWER, Boolean ANSWER1, Boolean ANSWER2, Boolean ANSWER3, Boolean ANSWER4, Boolean ANSWER5) {
            this.index = index;
            this.checked = checked;
            this.ANSWER = ANSWER;
            this.ANSWER1 = ANSWER1;
            this.ANSWER2 = ANSWER2;
            this.ANSWER3 = ANSWER3;
            this.ANSWER4 = ANSWER4;
            this.ANSWER5 = ANSWER5;
        }
    }*/
}
