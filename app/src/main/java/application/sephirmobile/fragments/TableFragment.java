package application.sephirmobile.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import application.sephirmobile.LoginActivity;
import application.sephirmobile.R;
import application.sephirmobile.table.adapters.TableAdapter;

@SuppressLint("StaticFieldLeak")
public abstract class TableFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AsyncTableTask asyncTableTask = new AsyncTableTask();
        asyncTableTask.execute();
        return asyncTableTask.getTableView();
    }

    protected abstract TableAdapter<?> createAdapter();

    protected void onGuiOpened(LinearLayout tableView) {
        // optional abstract
    }

    protected void loginActivity() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    private class AsyncTableTask extends AsyncTask<Void, Void, TableAdapter<?>> {
        private ProgressBar progressBar;
        private LinearLayout tableView;

        @Override
        protected TableAdapter<?> doInBackground(Void... voids) {
            return createAdapter();
        }

        @Override
        protected void onPostExecute(TableAdapter<?> tableAdapter) {
            FrameLayout columns = tableView.findViewById(R.id.columns);
            ListView rows = tableView.findViewById(R.id.rows);
            rows.setAdapter(tableAdapter);
            columns.addView(tableAdapter.getColumns());
            tableView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            showProgress(false);

            //last call
            onGuiOpened(tableView);
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        private void showProgress(final boolean show) {
            if (progressBar == null) {
                tableView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.table_layout, null, false);
                progressBar = tableView.findViewById(R.id.progressBarAverageMark);
            }
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

        public LinearLayout getTableView() {
            return tableView;
        }
    }
}
