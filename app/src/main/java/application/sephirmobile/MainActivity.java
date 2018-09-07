package application.sephirmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.login.Login;
import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.notifier.NotifierService;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMarks;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;
import application.sephirmobile.sephirinterface.getters.AverageSubjectMarkGetter;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SephirInterface sephirInterface;
    private LinearLayout mainView;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainView = findViewById(R.id.mainView);
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
                } catch (Exception e) {
                    //TODO handle Exception
                    e.printStackTrace();
                } finally {
                    return success;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (!success) {
                    loginActivity();
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

    private void loginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
        mainView.removeAllViews();
        switch (item.getItemId()){
            case  R.id.nav_logout:
                LoginUtils.save(null);
                loginActivity();
                break;
            case  R.id.nav_marks:
                showMarks();
                break;
            case  R.id.nav_futureTests:
                showAnnouncedTests();
                break;
            case  R.id.nav_averageMarks:
                showAverageMarks();
                break;
            case  R.id.nav_sephirWeb:
                showWebViewSephir();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showWebViewSephir() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sephir.ch/ICT/"));
        startActivity(i);
    }

    private void showMarks() {
        //TODO not static
        new Task() {

            @Override
            protected TableAdapter<SchoolTest> doInBackground(Void... voids) {
                List<SchoolTest> tests = new ArrayList<>();
                try {
                    List<SchoolClass> schoolClasses = new SchoolClassGetter(sephirInterface).get();
                    SchoolTestGetter testGetter = new SchoolTestGetter(sephirInterface);
                    for (SchoolClass schoolClass : schoolClasses) {
                        tests.addAll(testGetter.getPastTests(schoolClass));
                    }
                } catch (Exception e) {
                    //TODO handle exception
                    e.printStackTrace();
                    loginActivity();
                } finally {
                    return new SchoolTestAdapter(MainActivity.this, tests, sephirInterface);
                }
            }
        }.execute();
    }

    private void showAnnouncedTests() {
        //TODO not static
        new Task() {

            @Override
            protected TableAdapter<AnnouncedTest> doInBackground(Void... voids) {
                List<AnnouncedTest> tests = new ArrayList<>();
                try {
                    AnnouncedTestGetter testGetter = new AnnouncedTestGetter(sephirInterface);
                    tests.addAll(testGetter.get());
                } catch (Exception e) {
                    //TODO handle exception
                    e.printStackTrace();
                    loginActivity();
                } finally {
                    return new AnnouncedTestAdapter(MainActivity.this, tests);
                }
            }
        }.execute();
    }

    private void showAverageMarks() {
        //TODO not static
        new Task() {

            @Override
            protected TableAdapter<AverageSubjectMarks> doInBackground(Void... voids) {
                List<AverageSubjectMarks> marks = new ArrayList<>();
                try {
                    AverageSubjectMarkGetter getter = new AverageSubjectMarkGetter(sephirInterface);
                    SchoolClassGetter schoolClassGetter = new SchoolClassGetter(sephirInterface);
                    List<SchoolClass> schoolClasses = schoolClassGetter.get();
                    for (SchoolClass schoolClass : schoolClasses) {
                        marks.add(getter.get(schoolClass));
                    }
                } catch (Exception e) {
                    //TODO handle exception
                    e.printStackTrace();
                    loginActivity();
                } finally {
                    return new AverageSubjectMarksAdapter(MainActivity.this, marks);
                }
            }
        }.execute();
    }

    private abstract class Task extends AsyncTask<Void, Void, TableAdapter<?>> {
        @Override
        protected void onPostExecute(TableAdapter<?> adapter) {
            final ViewGroup nullParent = null;
            View tableView = getLayoutInflater().inflate(R.layout.table_layout, nullParent, false);
            FrameLayout columns = tableView.findViewById(R.id.columns);
            ListView rows = tableView.findViewById(R.id.rows);

            columns.addView(adapter.getColumns());
            rows.setAdapter(adapter);
            mainView.addView(tableView);
            showProgress(false);
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }
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
