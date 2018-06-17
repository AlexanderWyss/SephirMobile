package application.sephirmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class SchoolTestAdapter extends TableAdapter<SchoolTest> {

    public static final DecimalFormat MARK_FORMATTER = new DecimalFormat("0.00");
    private SephirInterface sephirInterface;

    public SchoolTestAdapter(@NonNull Context context, @NonNull List<SchoolTest> objects, SephirInterface sephirInterface) {
        super(context, 0, objects);
        this.sephirInterface = sephirInterface;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SchoolTest schoolTest = getItem(position);
        String dateText = schoolTest.getDate().toString("dd.MM.yy");
        String subjectText = schoolTest.getSubject();
        String nameText = schoolTest.getName();
        String MarkText = MARK_FORMATTER.format(schoolTest.getMark());
        convertView = getRow(convertView, dateText, subjectText, nameText, MarkText, schoolTest, null);
        return convertView;
    }

    @NonNull
    private View getRow(@Nullable View convertView, String dateText, String subjectText, String nameText, String markText, SchoolTest test, Float textSize) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.schooltest_table_layout, null, false);
        }
        TextView date = convertView.findViewById(R.id.date);
        TextView subject = convertView.findViewById(R.id.subject);
        TextView text = convertView.findViewById(R.id.text);
        TextView mark = convertView.findViewById(R.id.mark);
        ImageView averageMarkImage = convertView.findViewById(R.id.averageSymol);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        TextView averageMarkTextView = convertView.findViewById(R.id.averageMark);

        if (textSize != null) {
            date.setTextSize(textSize);
            subject.setTextSize(textSize);
            text.setTextSize(textSize);
            mark.setTextSize(textSize);
        }
        if (test != null) {
            averageMarkImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, String>() {

                        @Override
                        protected String doInBackground(Void... voids) {
                            try {
                                return MARK_FORMATTER.format(test.getAverageMark(sephirInterface));
                            } catch (IOException e) {
                                return "-";
                            }
                        }

                        @Override
                        protected void onPreExecute() {
                            averageMarkImage.setVisibility(View.GONE);
                            showProgress(true, progressBar);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            averageMarkTextView.setText(s);
                            showProgress(false, progressBar);
                            averageMarkTextView.setVisibility(View.VISIBLE);
                        }
                    }.execute();
                }
            });
        }

        date.setText(dateText);
        subject.setText(subjectText);
        text.setText(nameText);
        mark.setText(markText);
        return convertView;
    }

    @Override
    public View getColumns() {
        return getRow(null, getContext().getString(R.string.date), getContext().getString(R.string.subject), getContext().getString(R.string.testname), getContext().getString(R.string.mark), null, 15f);
    }

    private void showProgress(final boolean show, ProgressBar progressBar) {
        int shortAnimTime = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
