package de.sindzinski.lpictrainer.old;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.sindzinski.lpictrainer.R;
import de.sindzinski.lpictrainer.data.TrainerContract;

/**
 * Created by steffen on 22.02.16.
 */

public class TestCursorAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {

        public final TextView textView_question;
        public final TextView textView_current;


        public final CheckBox checkBox1;
        public final CheckBox checkBox2;
        public final CheckBox checkBox3;
        public final CheckBox checkBox4;
        public final CheckBox checkBox5;
        public final EditText editText1;

        public ViewHolder(View view) {

            textView_question = (TextView) view.findViewById(R.id.textView_current);
            textView_current = (TextView) view.findViewById(R.id.textView_current);

            checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
            checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
            checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
            checkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
            checkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
            editText1 = (EditText) view.findViewById(R.id.editText1);

        }
    }

    public TestCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_pager_question, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.textView_current.setText(Integer.toString(cursor.getPosition()) + "/" + Integer.toString(cursor.getCount()));
//       textView_current.setText(it.nextIndex()-1+"/"+entries.size());

        viewHolder.textView_question.setText(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TITLE));
//TODO the view holder does not keep the default answers, needs to implement
//                    .setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TEXT)))
//                    .setANSWER1(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)))
//                    .setCORRECT1(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT1)) > 0)
//                    .setANSWER2(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER2)))
//                    .setCORRECT2(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT2)) > 0)
//                    .setANSWER3(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER3)))
//                    .setCORRECT3(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT3)) > 0)
//                    .setANSWER4(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER4)))
//                    .setCORRECT4(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT4)) > 0)
//                    .setANSWER5(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER5)))
//                    .setCORRECT5(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER5)) > 0)

        viewHolder.checkBox1.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)));
        viewHolder.checkBox2.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)));
        viewHolder.checkBox3.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)));
        viewHolder.checkBox4.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)));
        viewHolder.checkBox5.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)));
        viewHolder.editText1.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TEXT)));
    }

}