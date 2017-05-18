package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class MyProfile extends Fragment {

    TextView tv1,tv2,tv3,tv4;
    private SharedPreferences sharedPreferences;
    private String pn;
    private String em;
    private String un;



    public MyProfile()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my_profile, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        tv1=(TextView)getActivity().findViewById(R.id.tv1);
        tv2=(TextView)getActivity().findViewById(R.id.tv2);
        tv3=(TextView)getActivity().findViewById(R.id.tv3);
        //tv4=(TextView)findViewById(R.id.tv4);
        sharedPreferences= getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
        un=sharedPreferences.getString("username","");
        em= sharedPreferences.getString("email_id","");
        pn=sharedPreferences.getString("person_name","");
        tv1.setText("Username   :  "+un);
        tv2.setText("Email Id       :  "+em);
        tv3.setText("Name          :  "+pn);
    }
}
