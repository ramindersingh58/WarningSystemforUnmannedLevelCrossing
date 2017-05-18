package com.example.raminder.warningsystemforunmannedlevelcrossing;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.Toast;
import com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;

public class Splash extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressBar pb1;
    int mProgressStatus = 0;
    Handler mHandler = new Handler();
    int PERMISSION_REQUEST_CODE = 200;
    LinearLayout mainLayout;
    private String un;
    private String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         pb1=(ProgressBar)(findViewById(R.id.pb1));
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkLocationPermission())
            {
                // Toast.makeText(this, "All Permissions Already Granted", Toast.LENGTH_SHORT).show();
                launch();
            }
            else
            {
                String permissions[] = {ACCESS_FINE_LOCATION};

                ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST_CODE);
            }
        }

        else
        {
            launch();
        }

    }
    private void launch()
    {
        // Start lengthy operation in a background thread
        Thread t1=new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 2) {

                    try{
                        Thread.sleep(1000);
                        mProgressStatus ++;
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            pb1.setProgress(mProgressStatus);



                        }
                    });
                }
                sharedPreferences= getSharedPreferences("mypref", MODE_PRIVATE);//no permission needed for sp
                un = sharedPreferences.getString("username", "");
                pw = sharedPreferences.getString("password", "");
                if(isNetworkConnected())
                {
                    if(un!=""||pw!="")
                    {
                        new Thread(new job(un, pw)).start();
                    }
                    else
                    {
                        Intent in = new Intent(Splash.this, Login.class);
                        startActivity(in);
                        finish();
                    }
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(mainLayout, "Internet is not accessible. Please enable it first and relaunch the app",
                                    Snackbar.LENGTH_INDEFINITE).show();
                        }
                    });
                }
            }
        });
        t1.start();
    }
    private boolean isNetworkConnected() {

        // get Connectivity Manager to get network status
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true; //we have a connection
        } else {
            return false; // no connection!
        }
    }
    boolean checkLocationPermission()
    {
        int ans1 = ContextCompat.checkSelfPermission(getBaseContext(),
                ACCESS_FINE_LOCATION);
        return (ans1== PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launch();
            }
            else if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //User has deny from permission dialog
                Snackbar.make(mainLayout, "Enable Location Permission for tracking location of Train",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                // User has deny permission and checked never show permission dialog so you can redirect to Application settings page
                Snackbar.make(mainLayout, "Enable location Permission from Settings",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", Splash.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }
    /////
    class job implements Runnable {
        String un, pw;

        job(String un, String pw) {
            this.un = un;
            this.pw = pw;
        }

        @Override
        public void run() {
            try {
                String query = "?un_ei=" + un + "&pw=" + pw;
                URL url = new URL(weburl+"/VehicleUserLogIn" + query);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d("Cognizant", "url"+url);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                final StringBuffer sb = new StringBuffer();
                Log.d("Cognizant", "login clicked");
                if (connection.getResponseCode() == 200) {
                    while (true) {
                        String temp = br.readLine();
                        if (temp == null)
                            break;
                        sb.append(temp);
                        Log.d("Cognizant", sb + "");
                    }
                }
                Log.d("Cognizant", sb + "sb");
                if (sb.toString().contains("success")) {
                    Intent in = new Intent(Splash.this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(Splash.this, Login.class);
                    startActivity(in);
                    finish();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static long back_pressed;


    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();

        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
/*
package com.example.raminder.warningsystemforunmannedlevelcrossing;


import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb1;
    static int SPLASH_TIME_OUT = 4000;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pb1=(ProgressBar)(findViewById(R.id.pb1));
        pb1.setMax(SPLASH_TIME_OUT);
        CountDownTimer countDownTimer=new CountDownTimer(SPLASH_TIME_OUT,SPLASH_TIME_OUT/100) {
            @Override
            public void onTick(long millisUntilFinished) {
                pb1.setProgress((int)(SPLASH_TIME_OUT-millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent in=new Intent(MainActivity.this,Login.class);
                startActivity(in);
                finish();
            }
        };
        countDownTimer.start();
    }


    private static long back_pressed;


    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();

        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
*/