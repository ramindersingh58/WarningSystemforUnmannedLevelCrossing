package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.cloud;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.localhotspot;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.localwifi;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;

public class SelectServer extends AppCompatActivity {

    RadioButton r1,r2,r3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_server);
        r1=(RadioButton)(findViewById(R.id.r1));
        r2=(RadioButton)(findViewById(R.id.r2));
        r3=(RadioButton)(findViewById(R.id.r3));
        if(weburl==cloud)
        {
            r1.setChecked(true);
        }
        else if(weburl==localwifi)
        {
            r2.setChecked(true);
        }
        else
        {
            r3.setChecked(true);
        }


        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                    weburl=cloud;
            }
        });
        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                    weburl=localwifi;
            }
        });
        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                    weburl=localhotspot;
            }
        });
    }
}
