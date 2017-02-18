package com.mako.srikrishnayarns;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean isdash=true;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cdlayout);
        setFragment(new Dashboard());
    }
    public  void setdarktoolbarcolor(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
    }
    public  void setlighttoolbarcolor(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimary)));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        else if (!isdash){
//            getSupportFragmentManager().popBackStack();
//            isdash=true;
//        }
        else {

//
//        }
//
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0  ) {

                new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want Exit?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            finish();



                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_delete)
                    .show();

            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
    public void startuploadActivity(Intent i){
        startActivityForResult(i, 100);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setFragment(Fragment fragment){

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {



        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

            setFragment(new Dashboard());

        } else if (id == R.id.nav_analysis) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Welcome ", Snackbar.LENGTH_LONG);
            snackbar.show();

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_settings) {
            isdash=false;
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
