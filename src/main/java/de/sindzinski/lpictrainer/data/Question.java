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
    public String answer1;
    public Boolean correct1;
    public String answer2;
    public Boolean correct2;
    public String answer3;
    public Boolean correct3;
    public String answer4;
    public Boolean correct4;
    public String answer5;
    public Boolean correct5;

    private Question(Builder questionBuilder) {
        this.index = questionBuilder.index;
        this.title = questionBuilder.title;
        this.type = questionBuilder.type;
        this.points = questionBuilder.points;
        this.text = questionBuilder.text;
        this.answer1 = questionBuilder.answer1;
        this.correct1 = questionBuilder.correct1;
        this.answer2 = questionBuilder.answer2;
        this.correct2 = questionBuilder.correct2;
        this.answer3 = questionBuilder.answer3;
        this.correct3 = questionBuilder.correct3;
        this.answer4 = questionBuilder.answer4;
        this.correct4 = questionBuilder.correct4;
        this.answer5 = questionBuilder.answer5;
        this.correct5 = questionBuilder.correct5;
    }

 /*   private Question(Integer index, String title, String type, Integer points, String text, String answer1, Boolean correct1, String answer2, Boolean correct2, String answer3, Boolean correct3, String answer4, Boolean correct4, String answer5, Boolean correct5) {
        this.index = index;
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
    }*/

    //builder pattern static class
    public static class Builder {
        private Integer index;
        private String title;
        private String type;
        private Integer points;
        private String text;
        private String answer1;
        private Boolean correct1;
        private String answer2;
        private Boolean correct2;
        private String answer3;
        private Boolean correct3;
        private String answer4;
        private Boolean correct4;
        private String answer5;
        private Boolean correct5;
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

        public Builder setAnswer1(String answer1) {
            this.answer1 = answer1;
            return this;
        }

        public Builder setCorrect1(Boolean correct1) {
            this.correct1 = correct1;
            return this;
        }

        public Builder setAnswer2(String answer2) {
            this.answer2 = answer2;
            return this;
        }

        public Builder setCorrect2(Boolean correct2) {
            this.correct2 = correct2;
            return this;
        }

        public Builder setAnswer3(String answer3) {
            this.answer3 = answer3;
            return this;
        }

        public Builder setCorrect3(Boolean correct3) {
            this.correct3 = correct3;
            return this;
        }

        public Builder setAnswer4(String answer4) {
            this.answer4 = answer4;
            return this;
        }

        public Builder setCorrect4(Boolean correct4) {
            this.correct4 = correct4;
            return this;
        }

        public Builder setAnswer5(String answer5) {
            this.answer5 = answer5;
            return this;
        }

        public Builder setCorrect5(Boolean correct5) {
            this.correct5 = correct5;
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
        outParcel.writeString(answer1);
        outParcel.writeByte((byte) (correct1 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(answer2);
        outParcel.writeByte((byte) (correct2 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(answer3);
        outParcel.writeByte((byte) (correct3 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(answer4);
        outParcel.writeByte((byte) (correct4 ? 1 : 0));     //if myBoolean == true, byte == 1
        outParcel.writeString(answer5);
        outParcel.writeByte((byte) (correct5 ? 1 : 0));     //if myBoolean == true, byte == 1
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
        answer1 = in.readString();
        correct1 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer2 = in.readString();
        correct2 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer3 = in.readString();
        correct3 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer4 = in.readString();
        correct4 = in.readByte() == 1;     //myBoolean == true if byte == 1
        answer5 = in.readString();
        correct5 = in.readByte() == 1;     //myBoolean == true if byte == 1
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

    public String getANSWER1() {
        return answer1;
    }

    public Boolean getCORRECT1() {
        return correct1;
    }

    public String getANSWER2() {
        return answer2;
    }

    public Boolean getCORRECT2() {
        return correct2;
    }

    public String getANSWER3() {
        return answer3;
    }

    public Boolean getCORRECT3() {
        return correct3;
    }

    public String getANSWER4() {
        return answer4;
    }

    public Boolean getCORRECT4() {
        return correct4;
    }

    public String getANSWER5() {
        return answer5;
    }

    public Boolean getCORRECT5() {
        return correct5;
    }

}
