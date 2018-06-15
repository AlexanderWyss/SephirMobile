package application.sephirmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;

public class AnnouncedTestAdapter extends TableAdapter<AnnouncedTest> {

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
        convertView = getRow(convertView, dateText, schoolClassText, nameText, subjectText, announcedTest.getText(), null);
        return convertView;
    }

    @NonNull
    private View getRow(@Nullable View convertView, String dateText, String schoolClassText, String nameText, String subjectText, String infoText, Float textSize) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.announcedtest_table_layout, null);
        }
        TextView date = convertView.findViewById(R.id.date);
        TextView schoolClass = convertView.findViewById(R.id.schoolclass);
        TextView name = convertView.findViewById(R.id.name);
        TextView subject = convertView.findViewById(R.id.subject);
        ImageView image = convertView.findViewById(R.id.infoImage);

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
        if (!infoText.isEmpty()) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View popUpView = LayoutInflater.from(getContext()).inflate(R.layout.announcedtest_info_popup_layout, null);
                    PopupWindow popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView closeButton = popUpView.findViewById(R.id.button_close);
                    closeButton.setOnClickListener(v -> popupWindow.dismiss());
                    TextView infoTextView = popUpView.findViewById(R.id.infoText);
                    infoTextView.setText(infoText);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
            });
        } else {
            image.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public View getColumns() {
        return getRow(null, "Date", "Class", "Name", "Subject", "", 15f);
    }
}
