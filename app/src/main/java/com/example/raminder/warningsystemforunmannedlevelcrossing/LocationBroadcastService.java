package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.R.id.ll;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class LocationBroadcastService extends Service {
    private SharedPreferences sharedPreferences;

    public LocationBroadcastService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //---check if GPS_PROVIDER is enabled---
//        final boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        //---check if NETWORK_PROVIDER is enabled---
//        boolean networkStatus = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        // check which provider is enabled
//        myLocationListener ml = new myLocationListener();
//        if (gpsStatus==false && networkStatus==false)
//        {
//            Toast.makeText(this , "Both GPS and Newtork are disabled", Toast.LENGTH_SHORT).show();
//            //---display the "Location services" settings page---
//            Intent in = new  Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(in);
//        }
//
//        if (gpsStatus == true) {
//            Toast.makeText(this, "GPS is Enabled, using it", Toast.LENGTH_SHORT).show();
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ml);
//        }
//
//        if (networkStatus == true) {
//            Toast.makeText(this, "Network Location is Enabled, using it", Toast.LENGTH_SHORT).show();
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ml);
//        }
//
//        Log.d("Cognizant", "::in location service");
//        sharedPreferences= getSharedPreferences("mypref", MODE_PRIVATE);
//
//        new Thread(new levels()).start();
//        new Thread(new trains()).start();
//        return START_NOT_STICKY;
//    }
//
//    private class levels implements Runnable {
//        @Override
//        public void run() {
//            try {
//                ///level data
//                URL url2 = new URL(weburl + "/jsonLevelLocs.jsp");
//
//                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
//
//                BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
//
//                final StringBuffer sb2 = new StringBuffer();
//
//                if (connection2.getResponseCode() == 200) {
//                    while (true) {
//                        String temp2 = br2.readLine();
//                        if (temp2 == null)
//                            break;
//                        sb2.append(temp2);
//                    }
//                }
//                //////////////JSON PARSING///////////////
//                final String s2 = sb2.toString();
//                if (s2.contains("Levels")) {
//                    JSONObject jsonObject2 = new JSONObject(s2);
//                    JSONArray jsonArray2 = jsonObject2.getJSONArray("Levels");
//                    for (int i = 0; i < jsonArray2.length(); i++) {
//                        JSONObject obj = jsonArray2.getJSONObject(i);
//                        final int level_id = obj.getInt("level_id");
//                        final String level_name = obj.getString("level_name");
//                        final String llat = obj.getString("lat");
//                        final String llng = obj.getString("lng");
//                        llatitude = Double.parseDouble(llat);
//                        llongitude = Double.parseDouble(llng);
//                        final LatLng levelLocation = new LatLng(llatitude, llongitude);
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mMap.addMarker(new MarkerOptions().position(levelLocation).snippet("Level_name:" + level_name + " Lat:" + llatitude + " Lng:" + llongitude)
//                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Level" + level_id));
//                            }
//                        });
//                    }
//                } else {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Snackbar.make(ll, s2,
//                                    Snackbar.LENGTH_INDEFINITE);
//                        }
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    private class trains implements Runnable {
//        @Override
//        public void run() {
//            while(true)
//            {
//                try {
//                    URL url = new URL(weburl + "/jsonTrainLocs.jsp" );
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    final StringBuffer sb = new StringBuffer();
//                    if (connection.getResponseCode() == 200) {
//                        while (true) {
//                            String temp = br.readLine();
//                            if (temp == null)
//                                break;
//                            sb.append(temp);
//                        }
//                    }
//                    //////////////JSON PARSING///////////////
//                    final String s=sb.toString();
//                    if(s.contains("Trains")) {
//                        JSONObject obj = new JSONObject(s);
//                        JSONArray jsonArray = obj.getJSONArray("Trains");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            final int train_id = jsonObject.getInt("train_id");
//                            final String train_name = jsonObject.getString("train_name");
//                            final String lat = jsonObject.getString("lat");
//                            final String lng = jsonObject.getString("lng");
//                            tlatitude = Double.parseDouble(lat);
//                            tlongitude = Double.parseDouble(lng);
//                            final LatLng trainLocation = new LatLng(tlatitude, tlongitude);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mMap.addMarker(new MarkerOptions().position(trainLocation).snippet("Name:" + train_name + " Lat:" + tlatitude + " Lng:" + tlongitude)
//                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Train" + train_id));
//
//                                    //                                   tv2.setText("TRAIN::id:"+train_id+" name:"+train_name+" lat:"+lat+" lng:"+lng);
//                                }
//                            });
//
//                        }
//                    }
//                    else
//                    {
//                        getActivity().runOnUiThread(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                Snackbar.make(ll, s,
//                                        Snackbar.LENGTH_INDEFINITE);
//                            }
//                        });
//                    }
//
//
//                    final double d1=distance(tlatitude,tlongitude,myLat,myLong);
//
//                    final double d2=distance(llatitude,llongitude,myLat,myLong);
//
//                    final double d3=distance(llatitude,llongitude,tlatitude,tlongitude);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tv3.setText("Distance b/w train & vehicle:"+d1+"km\n Distance b/w level & train:"+d3+"km\nDistance b/w level & vehicle:"+d2+"km");
//
//                        }
//                    });
//                    if(d3<0.1&&d2<0.1) {
//                        NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity());
//                        builder.setContentTitle("Stop if you wanna save your life");
//                        builder.setContentText("Train is coming to your nearesrt level crossing");
//                        builder.setContentInfo("Warning");
//                        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        builder.setSound(ringtoneUri);
//
//
//                        builder.setSmallIcon(R.drawable.location);
//
//                        Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.location);
//                        builder.setLargeIcon(bmp);
//
//                        /////////////////  Attach Intent with Notification ////////
//                        Intent in=new Intent(getActivity(),MainActivity.class);
//                        PendingIntent pin= PendingIntent.getActivity(getActivity(),0,in,0);
//
//                        builder.setContentIntent(pin);
//                        //////////////////////////////////////////////////////////
//
//                        Notification notification1 = builder.build();
//
//
//                        NotificationManager notificationManager =
//                                (NotificationManager)(getActivity().getSystemService(NOTIFICATION_SERVICE));
//
//                        notificationManager.notify(20,notification1);
//                    }
//
//                    Thread.sleep(2000);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        }
//    double distance(double lat1, double lon1, double lat2, double lon2 ) {
//        double theta, dist;
//        theta = lon1 - lon2;
//        dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta));
//        dist = acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//
//
//        dist = dist * 1.609344;
//
//        return (dist);
//    }
//    double deg2rad(double deg) {
//        return (deg * pi / 180);
//    }
//    double rad2deg(double rad) {
//        return (rad * 180 / pi);
//    }
//
//    /////////////////  Location Listener Logic ////////////////////
//    class mylocationlistener implements LocationListener
//    {
//
//        public void onLocationChanged(Location location)
//        {
//            try {
//
//                if (cmarker!=null) {
//                    cmarker.remove();
//
////                    LatLng prevloc=cmarker.getPosition();
////                    Log.d("Cognizant",""+prevloc);
////                    pmarker=mMap.addMarker(new MarkerOptions().position(prevloc).title("Previous Location").
////                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
////                    Log.d("Cognizant","not null");
//                }
//                else
//                {
//                    Log.d("Cognizant","cma");
//                }
//                //mMap.clear();
//                myLat = location.getLatitude();
//                myLong = location.getLongitude();
//                final float accuracy=location.getAccuracy();
//
//                //tv3.setText("Current: "+lat+","+lon);
//                LatLng newlocation = new LatLng(myLat, myLong);
//                cmarker = mMap.addMarker(new MarkerOptions().position(newlocation).snippet("Lat:" + myLat + " Lng:" + myLong)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Current"));
//                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_peterleow))
//
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(newlocation));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 13.0f));
//                Log.d("Cognizant", "cur:" + newlocation);
//                Toast.makeText(getActivity(),"Lat:" + myLat + "  Lon:" + myLong+"\n"+"The amount of accuracy is"+accuracy ,Toast.LENGTH_SHORT).show();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            String s ;//= tv1.getText().toString();
//                            s="Lat:" + myLat + "  Lon:" + myLong+"\n";//+s;
//                            tv1.setText(s);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        public void onProviderDisabled(String provider)
//        {
//
//        }
//
//        public void onProviderEnabled(String provider)
//        {
//
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras)
//        {
//
//        }
//    }

    }

