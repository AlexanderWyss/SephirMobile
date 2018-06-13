package application.sephirmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class AnnouncedTestAdapter extends ArrayAdapter<AnnouncedTest> {
    public AnnouncedTestAdapter(@NonNull Context context, @NonNull List<AnnouncedTest> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AnnouncedTest announcedTest = getItem(position);
        String dateText = announcedTest.getDate().toString("dd.MM.yy");
        String nameText = announcedTest.getName();
        String schoolClassText = String.valueOf(announcedTest.getSchoolClass());
        String subjectText = announcedTest.getSubject();
        convertView = getRow(convertView, dateText, schoolClassText, nameText, subjectText, null);
        return convertView;
    }

    @NonNull
    private View getRow(@Nullable View convertView, String dateText, String schoolClassText, String nameText, String subjectText, Float textSize) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.announcedtest_table_layout, null, false);
        }
        TextView date = convertView.findViewById(R.id.date);
        TextView schoolClass = convertView.findViewById(R.id.schoolclass);
        TextView name = convertView.findViewById(R.id.name);
        TextView subject = convertView.findViewById(R.id.subject);

        if (textSize != null) {
            date.setTextSize(textSize);
            schoolClass.setTextSize(textSize);
            name.setTextSize(textSize);
            subject.setTextSize(textSize);
        }

        date.setText(dateText);
        schoolClass.setText(schoolClassText);
        name.setText(nameText);
        subject.setText(subjectText);
        return convertView;
    }

    public View getColumns() {
        return getRow(null, "Date", "Class", "Name", "Subject", 15f);
    }
}
