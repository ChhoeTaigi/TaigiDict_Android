package com.taccotap.taigidict.portal;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.taccotap.taigidict.R;
import com.taccotap.taigidict.tailo.TailoDictFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict_drawer);
        ButterKnife.bind(this);

        initUi();
    }

    private void initUi() {
        setSupportActionBar(mToolbar);

        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        // init default content fragment
        setCurrentContentFragment(R.id.nav_dict_tailo);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        setCurrentContentFragment(id);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCurrentContentFragment(int id) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ActionBar supportActionBar = getSupportActionBar();

        if (id == R.id.nav_dict_tailo) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_dicts, TailoDictFragment.newInstance())
                    .commit();
            if (supportActionBar != null) {
                supportActionBar.setTitle(R.string.nav_dict_tailo);
            }
        } else if (id == R.id.nav_help) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_dicts, HelpFragment.newInstance())
                    .commit();
            if (supportActionBar != null) {
                supportActionBar.setTitle(R.string.nav_help);
            }
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_dicts, AboutFragment.newInstance())
                    .commit();
            if (supportActionBar != null) {
                supportActionBar.setTitle(R.string.nav_about);
            }
        } else if (id == R.id.nav_licenses) {
            LibsSupportFragment fragment = new LibsBuilder()
                    .withAutoDetect(true)
                    .withLicenseShown(true)
                    .withVersionShown(true)
                    .supportFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_dicts, fragment)
                    .commit();
            if (supportActionBar != null) {
                supportActionBar.setTitle(R.string.nav_licenses);
            }
        }
    }
}
