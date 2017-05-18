package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;


public class Login extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    EditText user, pass;
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        user = (EditText) findViewById(R.id.loginUser);
        pass = (EditText) findViewById(R.id.loginPass);
        loginButton = (TextView) findViewById(R.id.loginButton);
        TextView needHelp = (TextView) findViewById(R.id.needhelp);
        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                alert.setMessage("Please contact admin regarding your username and password" +
                        "(send a mail to ramindersingh58@gmail.com along with your details");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });

        TextView nam = (TextView) findViewById(R.id.nam);
        nam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signup.class));
                finish();
            }
        });

        changeStatusBarColor();
    }
// launch of main activity
    public void login(View view) {
        String us = user.getText().toString();
        String pa = pass.getText().toString();
        if (us.trim().equals(""))
        {
            user.setError("Please Enter UserName / Email Id");
            user.requestFocus();
        }
        else if(pa.trim().equals(""))
        {
            pass.setError("Please Enter Valid Password");
            pass.requestFocus();
        } else {
            if(isNetworkConnected())
                new Thread(new job(us,pa)).start();
            else
                Toast.makeText(this,"Internet is not accessible. Please enable it first",Toast.LENGTH_SHORT).show();
        }
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
                URL url = new URL(weburl +"/VehicleUserLogIn" + query);
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
                final String s2=sb.toString();
                if(s2.contains("success"))
                {
                    JSONObject jsonObject2 = new JSONObject(s2);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("success");
                    JSONObject obj = jsonArray2.getJSONObject(0);
                    String pn = obj.getString("pn");
                    String ei = obj.getString("ei");
                    String un = obj.getString("un");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", un);
                    editor.putString("password", pw);
                    editor.putString("email_id",ei);
                    editor.putString("person_name",pn);
                    editor.commit();
                    Intent in = new Intent(Login.this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pass.setText("");
                            pass.setError("Please Enter Valid Password");
                            pass.requestFocus();
                            //Toast.makeText(Login.this, "Incorrect Password !!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
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


