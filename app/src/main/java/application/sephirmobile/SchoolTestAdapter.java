package application.sephirmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class SchoolTestAdapter extends TableAdapter<SchoolTest> {

    public static final DecimalFormat df = new DecimalFormat("#.00");

    public SchoolTestAdapter(@NonNull Context context, @NonNull List<SchoolTest> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SchoolTest schoolTest = getItem(position);
        String dateText = schoolTest.getDate().toString("dd.MM.yy");
        String subjectText = schoolTest.getSubject();
        String nameText = schoolTest.getName();
        String MarkText = df.format(schoolTest.getMark());
        convertView = getRow(convertView, dateText, subjectText, nameText, MarkText, null);
        return convertView;
    }

    @NonNull
    private View getRow(@Nullable View convertView, String dateText, String subjectText, String nameText, String markText, Float textSize) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.schooltest_table_layout, null, false);
        }
        TextView date = convertView.findViewById(R.id.date);
        TextView subject = convertView.findViewById(R.id.subject);
        TextView text = convertView.findViewById(R.id.text);
        TextView mark = convertView.findViewById(R.id.mark);

        if (textSize != null) {
            date.setTextSize(textSize);
            subject.setTextSize(textSize);
            text.setTextSize(textSize);
            mark.setTextSize(textSize);
        }

        date.setText(dateText);
        subject.setText(subjectText);
        text.setText(nameText);
        mark.setText(markText);
        return convertView;
    }

    @Override
    public View getColumns() {
        return getRow(null, "Date", "Subject", "Name", "Mark", 15f);
    }
}
