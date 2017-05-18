package com.example.raminder.warningsystemforunmannedlevelcrossing;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    DrawerLayout drawer;
    LocationManager lm;
    boolean isNetworkProviderEnabled;
    boolean isGPSProviderEnabled;
    private Intent in4;
    private String personName;
    private TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //myToolbar.setTitle("Warning System for ULC");

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.lll,new MapsActivity());
        fragmentTransaction.commit();
        /////////////////////////////////////////////////////////////////

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, //myToolbar,
                 R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences= getSharedPreferences("mypref", MODE_PRIVATE);
        personName=sharedPreferences.getString("person_name","");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        user_name = (TextView)header.findViewById(R.id.user_name);
        //email = (TextView)header.findViewById(R.id.email);
        user_name.setText(personName);
        //email.setText(personEmail);


        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        isNetworkProviderEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isGPSProviderEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isNetworkProviderEnabled && !isGPSProviderEnabled) {
            showLocationSettings();
            in4 = new Intent(this, LocationBroadcastService.class);
            startService(in4);
        }
        else {
            in4 = new Intent(this, LocationBroadcastService.class);
            startService(in4);
        }
    }
    void showLocationSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Location Access");
        builder.setMessage("This application requires location access. Do you agree?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(it);
            }
        });
        builder.setNegativeButton("NO", null);
        builder.create().show();
    }




    private static long back_pressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            MainActivity.this.finish();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_track) {
            // Handle the various action in mvp2
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lll,new MapsActivity());
            fragmentTransaction.commit();
        }
//        else if (id == R.id.nav_alerts) {
//            Toast.makeText(MainActivity.this, "Alerts Selected", Toast.LENGTH_SHORT).show();
//        }
//         else if (id == R.id.nav_route) {
//            Toast.makeText(MainActivity.this, "Routes Selected", Toast.LENGTH_SHORT).show();
//        }
//        else if (id == R.id.nav_stop_alert) {
//            Toast.makeText(MainActivity.this, "Stop Alert Selected", Toast.LENGTH_SHORT).show();
//        }
        else if (id == R.id.nav_password) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lll,new ChangePassword());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent in = new Intent(MainActivity.this, Login.class);
            startActivity(in);
        } else if (id == R.id.nav_myprofile) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lll,new MyProfile());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_settings) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lll,new Settings());
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            if(drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);
            }
            else
            {
                drawer.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}