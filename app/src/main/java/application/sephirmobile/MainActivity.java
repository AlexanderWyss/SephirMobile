package application.sephirmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.login.Login;
import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.notifier.NotifierService;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout columns;
    private ListView rows;
    private SephirInterface sephirInterface;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        columns = findViewById(R.id.columns);
        rows = findViewById(R.id.rows);
        progressBar = findViewById(R.id.progressBar);
        //TODO index 0
        email = navigationView.getHeaderView(0).findViewById(R.id.email);

        Login login = LoginUtils.load();
        //TODO not static
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                sephirInterface = new SephirInterface();
                boolean success = false;
                try {
                    success = sephirInterface.login(login);
                } catch (IOException e) {
                    //TODO handle Exception
                    e.printStackTrace();
                } finally {
                    return success;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (!success) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    NotifierService.scheduleJob(getApplicationContext(), 1);

                    email.setText(login.getEmail());
                    showProgress(false);
                    select(navigationView.getMenu().findItem(R.id.nav_marks));
                }
            }

            @Override
            protected void onPreExecute() {
                showProgress(true);
            }
        }.execute();
    }

    private void select(MenuItem item) {
        item.setChecked(true);
        onNavigationItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        columns.removeAllViews();
        rows.setAdapter(null);

        int id = item.getItemId();
        if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_marks) {
            showMarks();
        } else if (id == R.id.nav_futureTests) {
            showAnnouncedTests();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMarks() {
        //TODO not static
        new AsyncTask<Void, Void, List<SchoolTest>>() {

            @Override
            protected List<SchoolTest> doInBackground(Void... voids) {
                List<SchoolTest> tests = new ArrayList<>();
                try {
                    List<SchoolClass> schoolClasses = new SchoolClassGetter(sephirInterface).get();
                    SchoolTestGetter testGetter = new SchoolTestGetter(sephirInterface);
                    for (SchoolClass schoolClass : schoolClasses) {
                        tests.addAll(testGetter.get(schoolClass));
                    }
                } catch (IOException e) {
                    //TODO handle exception
                    e.printStackTrace();
                } finally {
                    return tests;
                }
            }

            @Override
            protected void onPostExecute(List<SchoolTest> tests) {
                SchoolTestAdapter testAdapter = new SchoolTestAdapter(MainActivity.this, tests);
                columns.addView(testAdapter.getColumns());
                rows.setAdapter(testAdapter);
                showProgress(false);
            }

            @Override
            protected void onPreExecute() {
                showProgress(true);
            }
        }.execute();
    }

    private void showAnnouncedTests() {
        //TODO not static
        new AsyncTask<Void, Void, List<AnnouncedTest>>() {

            @Override
            protected List<AnnouncedTest> doInBackground(Void... voids) {
                List<AnnouncedTest> tests = new ArrayList<>();
                try {
                    AnnouncedTestGetter testGetter = new AnnouncedTestGetter(sephirInterface);
                    tests.addAll(testGetter.get());
                } catch (IOException e) {
                    //TODO handle exception
                    e.printStackTrace();
                } finally {
                    return tests;
                }
            }

            @Override
            protected void onPostExecute(List<AnnouncedTest> tests) {
                AnnouncedTestAdapter testAdapter = new AnnouncedTestAdapter(MainActivity.this, tests);
                columns.addView(testAdapter.getColumns());
                rows.setAdapter(testAdapter);
                showProgress(false);
            }

            @Override
            protected void onPreExecute() {
                showProgress(true);
            }
        }.execute();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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
