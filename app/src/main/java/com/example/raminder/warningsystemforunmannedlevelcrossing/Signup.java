package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;


public class Signup extends AppCompatActivity {

    EditText et1,et2,et3,et4;
    String un,pw,pn,ei;
    String temp;
    TextView tv4,tv5;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        sharedPreferences= getSharedPreferences("mypref", MODE_PRIVATE);

    }
    public void openlogin(View view) {
        Intent in = new Intent(Signup.this, Login.class);
        startActivity(in);
        finish();
    }

    public void signup(View view) {
        un=et1.getText().toString();
        pw=et2.getText().toString();
        pn=et3.getText().toString();
        ei=et4.getText().toString();
        if (un.trim().equals(""))
        {
            et1.setError("Please Enter Username");
            et1.requestFocus();
        }
        else if(pw.trim().equals("") )
        {
            et2.setError("Please Enter Password");
            et2.requestFocus();
        }
        else if(pn.trim().equals(""))
        {
            et3.setError("Please Enter Your Name");
            et3.requestFocus();
        }
        else if(ei.equals("")) {
            et4.setError("Please Enter Email Id");
            et4.requestFocus();
        }
        else
        {
            if(isNetworkConnected())
                new Thread(new job1(un, pw, pn, ei)).start();
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

    public void setup(View view) {
        Intent in = new Intent(Signup.this, SelectServer.class);
        startActivity(in);
    }


    ///////
class job1 implements Runnable
{
    String un,pw,pn,ei;
    job1(String un,String pw,String pn,String ei)
    {
        this.un=un;
        this.pw=pw;
        this.pn=pn;
        this.ei=ei;

    }

    @Override
    public void run() {
        try {

            String query = "?un=" + un + "&pw=" + pw + "&pn=" + pn + "&ei="+ ei;
            URL url = new URL(weburl +"/VehicleUserSignUp" + query);
            Log.d("Cognizant", "url"+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //dont get output stream
            //close firewall
            final StringBuffer sb = new StringBuffer();
            Log.d("Cognizant", "signup clicked");
            if (connection.getResponseCode() == 200) {
                while (true) {
                    temp = br.readLine();
                    if (temp == null)
                        break;
                    sb.append(temp);
                }
            }
            Log.d("Cognizant",sb+"sb");
            if(sb.toString().equals("success")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", un);
                editor.putString("password", pw);
                editor.putString("email_id",ei);
                editor.putString("person_name",pn);
                editor.commit();
                Intent in = new Intent(Signup.this, MainActivity.class);
                startActivity(in);
                finish();
            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv5.setText(sb);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

}
