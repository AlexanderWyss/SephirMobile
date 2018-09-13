package application.sephirmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import application.sephirmobile.table.adapters.TableAdapter;

@SuppressLint("StaticFieldLeak")
public abstract class AsyncTableTask extends AsyncTask<Void, Void, TableAdapter<?>> {

    private ProgressBar progressBar;
    private LinearLayout tableView;
    private Context context;

    protected AsyncTableTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(TableAdapter<?> tableAdapter) {
        FrameLayout columns = tableView.findViewById(R.id.columns);
        ListView rows = tableView.findViewById(R.id.rows);
        rows.setAdapter(tableAdapter);
        columns.addView(tableAdapter.getColumns());
        tableView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        showProgress(false);
    }

    @Override
    protected void onPreExecute() {
        tableView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.table_layout, null, false);
        progressBar = tableView.findViewById(R.id.progressBar);
        showProgress(true);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public LinearLayout getTableView() {
        return tableView;
    }
}
