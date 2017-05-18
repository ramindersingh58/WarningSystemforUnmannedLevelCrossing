package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.alarmEnabled;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.disbwlt;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.disbwly;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.disdl;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.distl;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.rate;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.traindetails;

public class Settings extends Fragment {

    SeekBar seekBar;
    TextView tv1,tv2,tv3,tv4;
    private CheckBox cb1,cb2,cb3,cb4;
    private SeekBar seekBar2,seekBar3;
    private Button bt1;

    public Settings() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_settings, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        seekBar=(SeekBar)getActivity().findViewById(R.id.seekBar);
        tv1=(TextView)getActivity().findViewById(R.id.tv1);
        int prog=rate/5;
        seekBar.setProgress(prog);
        tv1.setText("After "+prog*5+" seconds");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rate=5*progress;
                tv1.setText("After "+rate+" seconds");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cb1=(CheckBox)(getActivity().findViewById(R.id.cb1));
        cb2=(CheckBox)(getActivity().findViewById(R.id.cb2));
        cb3=(CheckBox)(getActivity().findViewById(R.id.cb3));
        if(traindetails)
            cb1.setChecked(true);
        if(disdl)
            cb2.setChecked(true);
        if(distl)
            cb3.setChecked(true);
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    traindetails=true;
                }
                else
                {
                    traindetails=false;
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    disdl=true;
                }
                else
                {
                    disdl=false;
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    distl=true;
                }
                else
                {
                    distl=false;
                }
            }
        });

        seekBar2=(SeekBar)getActivity().findViewById(R.id.seekBar2);
        tv2=(TextView)getActivity().findViewById(R.id.tv2);
        int prog2= (int) disbwlt;
        seekBar2.setProgress(prog2);
        tv2.setText(prog2+" km");
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                disbwlt=progress;
                tv2.setText(disbwlt+" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar3=(SeekBar)getActivity().findViewById(R.id.seekBar3);
        tv3=(TextView)getActivity().findViewById(R.id.tv3);
        int prog3= (int) (disbwly*2);
        seekBar3.setProgress(prog3);
        tv3.setText(disbwly+" km");
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                disbwly = progress*0.5;
                tv3.setText(disbwly + " km");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bt1=(Button)getActivity().findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
            }
        });

        tv4=(TextView)getActivity().findViewById(R.id.tv4);
        cb4=(CheckBox)(getActivity().findViewById(R.id.cb4));
        if(alarmEnabled)
            cb4.setChecked(true);
        else
            cb4.setChecked(false);

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    alarmEnabled=true;
                    tv4.setText("Warning through Alarm");
                }
                else
                {
                    alarmEnabled=false;
                    tv4.setText("Warning through Notification");
                }
            }
        });


    }
    private void changeMode() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Your location accuracy is very poor. Kindly switch to High Accuracy Mode from Settings ");
        alert.setIcon(R.drawable.ic_location);
        alert.setTitle("Poor Location Accuracy");
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(it);
            }
        });
        alert.create();
        alert.show();
    }

}
