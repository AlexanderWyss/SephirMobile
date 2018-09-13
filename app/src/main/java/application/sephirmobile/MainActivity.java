package application.sephirmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import application.sephirmobile.fragments.AnnouncedTestFragment;
import application.sephirmobile.fragments.AverageMarksFragment;
import application.sephirmobile.fragments.MarksFragment;
import application.sephirmobile.login.LoginUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupDrawerContent(findViewById(R.id.nav_view));

        NavigationView navigationView = findViewById(R.id.nav_view);
        //TODO index 0
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.email)).setText(LoginUtils.load().getEmail());
        select(navigationView.getMenu().findItem(R.id.nav_marks));
    }

    private void select(MenuItem item) {
        item.setChecked(true);
        onNavigationItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.nav_logout:
                LoginUtils.save(null);
                startActivity(new Intent(this, LoginActivity.class));
                fragment = new Fragment();
                break;
            case R.id.nav_futureTests:
                fragment = new AnnouncedTestFragment();
                break;
            case R.id.nav_averageMarks:
                fragment = new AverageMarksFragment();
                break;
            case R.id.nav_sephirWeb:
                showWebViewSephir();
                fragment = new MarksFragment();
                break;
            case R.id.nav_marks:
            default:
                fragment = new MarksFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainView, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showWebViewSephir() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sephir.ch/ICT/"));
        startActivity(i);
    }
}
