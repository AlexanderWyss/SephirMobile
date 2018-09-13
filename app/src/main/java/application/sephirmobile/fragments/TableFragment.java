package application.sephirmobile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import application.sephirmobile.AsyncTableTask;
import application.sephirmobile.LoginActivity;
import application.sephirmobile.table.adapters.TableAdapter;

@SuppressLint("StaticFieldLeak")
public abstract class TableFragment extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // TODO inline asyncTableTask
            AsyncTableTask task = (AsyncTableTask) new AsyncTableTask(getContext()) {
                @Override
                protected TableAdapter<?> doInBackground(Void... voids) {
                    return createAdapter();
                }
            }.execute();
            return task.getTableView();
        }

        protected abstract TableAdapter<?> createAdapter();


    protected void loginActivity() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}
