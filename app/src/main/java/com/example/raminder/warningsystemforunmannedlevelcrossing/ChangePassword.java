package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.R.id.tv5;

public class ChangePassword extends Fragment{

    EditText et1,et2,et3;
    String opw,npw,cpw;
    private SharedPreferences sharedPreferences;
    private String un;
    TextView tv5;
    Button bt1;
    public ChangePassword()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_change_password, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        et1=(EditText)getActivity().findViewById(R.id.et1);
        et2=(EditText)getActivity().findViewById(R.id.et2);
        et3=(EditText)getActivity().findViewById(R.id.et3);
        tv5=(TextView)getActivity().findViewById(R.id.tv5);
        bt1=(Button)getActivity().findViewById(R.id.bt1);
        sharedPreferences= getActivity().getSharedPreferences("mypref", MODE_PRIVATE);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Cognizant","inside on click listener");
                opw=et1.getText().toString();
                npw=et2.getText().toString();
                cpw=et3.getText().toString();
                if (opw.trim().equals(""))
                {
                    et1.setError("Enter Old Password");
                    et1.requestFocus();
                }
                else if(npw.trim().equals("") )
                {
                    et2.setError("Enter New Password");
                    et2.requestFocus();
                }
                else if(cpw.trim().equals(""))
                {
                    et3.setError("Confirm New Password");
                    et3.requestFocus();
                }
                else if(!npw.equals(cpw)) {
                    et3.setText("");
                    et3.setError("Password and Confirm Password does not match");
                    et3.requestFocus();
                }
                else
                {
                    if(isNetworkConnected())
                        new Thread(new job1()).start();
                    else
                        Toast.makeText(getActivity(),"Internet is not accessible. Please enable it first",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void changePassword(View view) {
//
//
//    }
    private boolean isNetworkConnected() {

        // get Connectivity Manager to get network status
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true; //we have a connection
        } else {
            return false; // no connection!
        }
    }
    class job1 implements Runnable
    {
        @Override
        public void run() {
            try {
                un=sharedPreferences.getString("username","");
                String query = "?username="+un+"&oldpw=" + opw + "&newpw=" + npw ;
                URL url = new URL(weburl +"/ChangePassword" + query);
                Log.d("Cognizant", "url"+url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuffer sb = new StringBuffer();
                Log.d("Cognizant", "change pw clicked");
                if (connection.getResponseCode() == 200) {
                    while (true) {
                        String temp = br.readLine();
                        if (temp == null)
                            break;
                        sb.append(temp);
                    }
                }
                Log.d("Cognizant",sb+"sb");
                if(sb.toString().equals("success")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", npw);
                    editor.commit();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv5.setText("Password successfully changed");
                            et1.setText("");
                            et2.setText("");
                            et3.setText("");
                        }
                    });
                }
                else
                {
                    getActivity().runOnUiThread(new Runnable() {
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
