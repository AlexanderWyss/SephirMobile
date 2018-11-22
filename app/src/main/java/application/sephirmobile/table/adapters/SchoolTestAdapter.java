package application.sephirmobile.table.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import application.sephirmobile.R;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.TestChartGetter;

public class SchoolTestAdapter extends TableAdapter<SchoolTest> {

    private static final DecimalFormat MARK_FORMATTER = new DecimalFormat("0.00");
    private final SephirInterface sephirInterface;

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
        final ViewGroup nullParent = null;
        convertView = LayoutInflater.from(getContext())
                .inflate(R.layout.schooltest_table_layout, nullParent, false);
        TextView date = convertView.findViewById(R.id.date);
        TextView subject = convertView.findViewById(R.id.subject);
        TextView text = convertView.findViewById(R.id.text);
        TextView mark = convertView.findViewById(R.id.mark);
        ImageView averageMarkImage = convertView.findViewById(R.id.averageSymol);
        ProgressBar progressBarAverageMark = convertView.findViewById(R.id.progressBarAverageMark);
        TextView averageMarkTextView = convertView.findViewById(R.id.averageMark);
        ImageView barChartImage = convertView.findViewById(R.id.barChartSymbol);
        ProgressBar progressBarTestChart = convertView.findViewById(R.id.progressBarTestChart);


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
                            showProgress(true, progressBarAverageMark);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            averageMarkTextView.setText(s);
                            showProgress(false, progressBarAverageMark);
                            averageMarkTextView.setVisibility(View.VISIBLE);
                        }
                    }.execute();
                }
            });
            barChartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, Bitmap>() {

                        @Override
                        protected Bitmap doInBackground(Void... voids) {
                            try {
                                return new TestChartGetter(sephirInterface).get(test);
                            } catch (IOException e) {
                                return null;
                            }
                        }

                        @Override
                        protected void onPreExecute() {
                            barChartImage.setVisibility(View.GONE);
                            showProgress(true, progressBarTestChart);
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            View popUpView = View.inflate(getContext(), R.layout.test_chart_popup_layout, null);
                            PopupWindow popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            popupWindow.setOutsideTouchable(true);
                            PhotoView chart = popUpView.findViewById(R.id.imageView);
                            chart.setImageBitmap(bitmap);
                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                            showProgress(false, progressBarTestChart);
                            barChartImage.setVisibility(View.VISIBLE);
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
