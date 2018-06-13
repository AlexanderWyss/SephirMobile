package application.sephirmobile;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView mainView = findViewById(R.id.mainView);
        try {
            SephirInterface sephirInterface = new SephirInterface();
            sephirInterface.login(LoginUtils.load());
            List<SchoolClass> schoolClasses = new SchoolClassGetter(sephirInterface).get();
            SchoolTestGetter schoolTestGetter = new SchoolTestGetter(sephirInterface);
            List<SchoolTest> tests = new ArrayList<>();
            for (SchoolClass schoolClass : schoolClasses) {
                tests.addAll(schoolTestGetter.get(schoolClass));
            }
            SchoolTestAdapter schoolTestAdapter = new SchoolTestAdapter(this, tests);
            mainView.setAdapter(schoolTestAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_marks) {
        } else if (id == R.id.nav_futureTests) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
