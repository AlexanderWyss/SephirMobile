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

import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class SchoolTestAdapter extends ArrayAdapter<SchoolTest>{
    public SchoolTestAdapter(@NonNull Context context, @NonNull List<SchoolTest> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.schooltest_table_layout, null, false);
        }
        TextView date = convertView.findViewById(R.id.date);
        TextView subject = convertView.findViewById(R.id.subject);
        TextView text = convertView.findViewById(R.id.text);
        TextView mark = convertView.findViewById(R.id.mark);

        SchoolTest schoolTest = getItem(position);
        date.setText(schoolTest.getDate().toString("dd.MM.yy"));
        subject.setText(schoolTest.getSubject());
        text.setText(schoolTest.getName());
        mark.setText(String.valueOf(schoolTest.getMark()));
        return convertView;
    }
}
