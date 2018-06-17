package application.sephirmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMark;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMarks;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class AverageSubjectMarksAdapter extends TableAdapter<AverageSubjectMarks> {

    public static final DecimalFormat MARK_FORMATTER = new DecimalFormat("#.00");

    public AverageSubjectMarksAdapter(@NonNull Context context, @NonNull List<AverageSubjectMarks> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AverageSubjectMarks averageMarks = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.averagesubjectmarks_table_layout, null, false);
        }
        TextView schoolClassView = convertView.findViewById(R.id.schoolclass);
        TextView markView = convertView.findViewById(R.id.mark);
        FrameLayout columns = convertView.findViewById(R.id.columns);
        LinearLayout rows = convertView.findViewById(R.id.rows);
        rows.removeAllViews();
        columns.removeAllViews();

        schoolClassView.setText(averageMarks.getSchoolClass().getName());
        markView.setText(MARK_FORMATTER.format(averageMarks.getAverageMark()));

        columns.addView(getRowColumns());
        for (AverageSubjectMark averageMark : averageMarks.getAsList()) {
            rows.addView(getRow(averageMark.getSubject(), String.valueOf(averageMark.getAverageMark()), String.valueOf(averageMark.getTestAmount()), String.valueOf(averageMark.getAverageClassMark()), null));
        }
        return convertView;
    }

    @Override
    public View getColumns() {
        View view = new View(getContext());
        view.setVisibility(View.GONE);
        return view;
    }


    private View getRow(String subjectText, String averageMarkText, String testAmountText, String averageClassMarkText, Float textSize) {
        View convertView = LayoutInflater.from(getContext())
                .inflate(R.layout.averagesubjectmark_table_layout, null);
        TextView subjectView = convertView.findViewById(R.id.subject);
        TextView averageMarkView = convertView.findViewById(R.id.mark);
        TextView testAmountView = convertView.findViewById(R.id.testAmount);
        TextView averageClassMarkView = convertView.findViewById(R.id.classMark);

        if (textSize != null) {
            subjectView.setTextSize(textSize);
            averageMarkView.setTextSize(textSize);
            testAmountView.setTextSize(textSize);
            averageClassMarkView.setTextSize(textSize);
        }

        subjectView.setText(subjectText);
        averageMarkView.setText(averageMarkText);
        testAmountView.setText(testAmountText);
        averageClassMarkView.setText(averageClassMarkText);
        return convertView;
    }

    public View getRowColumns() {
        return getRow("Subject", "Mark", "Test Amount", "Class Mark", 15f);
    }
}
