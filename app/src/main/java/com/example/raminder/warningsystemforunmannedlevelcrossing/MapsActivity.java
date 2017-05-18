package com.example.raminder.warningsystemforunmannedlevelcrossing;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.disbwlt;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.disbwly;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.disdl;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.distl;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.rate;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.running;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.traindetails;
import static com.example.raminder.warningsystemforunmannedlevelcrossing.GlobalApp.weburl;
import static java.lang.Math.*;
import android.provider.Settings;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    double pi = 3.14159265358979323846;
    private GoogleMap mMap;
    double myLat = 20.5937, myLong = 78.9629;
    View view;
    Marker pmarker, cmarker, tmarker;
    TextView tv1, tv2, tv3;
    double llatitude, llongitude;
    double tlatitude, tlongitude;
    LinearLayout ll;
    MapView mMapView;
    private float accuracy;
    boolean issendingl = false;
    boolean issendingt=false;

    public MapsActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_maps, container, false);
//        FragmentManager fragmentManager=getFragmentManager();
//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        SupportMapFragment fragment=new SupportMapFragment();
//        transaction.add(R.id.mapView,fragment);
//        transaction.commit();
//        fragment.getMapAsync(this);
        // Gets the MapView from the XML layout and creates it
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return view;
        //Dont Write Logic Here, Write in onStart
    }


    @Override
    public void onStart() {
        super.onStart();
//        if(!getActivity().isGooglePlayServicesAvailable())
//        {
//            Toast.makeText(getActivity(),"Google Play Services unavailable",Toast.LENGTH_LONG).show();
//        }
        // tv1=(TextView)getActivity().findViewById(R.id.tv1);
        tv2 = (TextView) getActivity().findViewById(R.id.tv2);
        tv3 = (TextView) getActivity().findViewById(R.id.tv3);
        ll = (LinearLayout) getActivity().findViewById(R.id.ll);
        ///////// Location Manager Logic  ////////////////////
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //---check if GPS_PROVIDER is enabled---
        boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //---check if NETWORK_PROVIDER is enabled---
        boolean networkStatus = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // check which provider is enabled
        mylocationlistener ml = new mylocationlistener();

        if (gpsStatus) {
            //Toast.makeText(getActivity(), "GPS is Enabled, using it", Toast.LENGTH_SHORT).show();
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ml);
        }

        if (networkStatus) {
            //Toast.makeText(getActivity(), "Network Location is Enabled, using it", Toast.LENGTH_LONG).show();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ml);
        }
        new Thread(new LevelLocs()).start();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near India.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in India and move the camera
        LatLng india = new LatLng(myLat, myLong);
        pmarker=mMap.addMarker(new MarkerOptions().position(india).title("India Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(india, 3.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
/////////
double distance(double lat1, double lon1, double lat2, double lon2 ) {
    double theta, dist;
    theta = lon1 - lon2;
    dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta));
    dist = acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    dist = dist * 1.609344;
    return (dist);
}
    double deg2rad(double deg) {
        return (deg * pi / 180);
    }
    double rad2deg(double rad) {
        return (rad * 180 / pi);
    }

    /////////////////  Location Listener Logic ////////////////////
    class mylocationlistener implements LocationListener
    {
        public void onLocationChanged(Location location)
        {
            try {
                if (cmarker!=null) {
                    cmarker.remove();

//                    LatLng prevloc=cmarker.getPosition();
//                    Log.d("Cognizant",""+prevloc);
//                    pmarker=mMap.addMarker(new MarkerOptions().position(prevloc).title("Previous Location").
//                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
//                    Log.d("Cognizant","not null");
                }
                else
                {
                    Log.d("Cognizant","cma");
                }
                //mMap.clear();
                myLat = location.getLatitude();
                myLong = location.getLongitude();
                if (issendingt == false) {
                    new Thread(new TrainLocs()).start();
                }
                if(issendingl==false)
                {
                    new Thread(new LevelLocs2()).start();
                }


                accuracy=location.getAccuracy();
                Log.d("Cognizant","accuracy"+accuracy);
                if(accuracy>15.0 && getActivity()!=null)
                {
                    generateNoti2();
                }
                //tv3.setText("Current: "+lat+","+lon);
                LatLng newlocation = new LatLng(myLat, myLong);
                cmarker = mMap.addMarker(new MarkerOptions().position(newlocation).snippet("Lat:" + roundoff(myLat) + " Lng:" + roundoff(myLong))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Current"));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_peterleow))

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(newlocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 13.0f));
                Log.d("Cognizant", "cur:" + newlocation);

   //             Toast.makeText(getActivity(),"Lat:" + myLat + "  Lon:" + myLong+"\n"+"The amount of accuracy is"+accuracy ,Toast.LENGTH_SHORT).show();
//                if(getActivity()!=null)
//                {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//
//                                String s ;//= tv1.getText().toString();
//                                s="Lat:" + myLat + "  Lon:" + myLong+"\n";//+s;
//                                tv1.setText(s);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }

            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void onProviderDisabled(String provider)
        {

        }

        public void onProviderEnabled(String provider)
        {

        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }


    private class LevelLocs implements Runnable {
        @Override
        public void run() {
            try {
                URL url2 = new URL(weburl + "/jsonLevelLocs.jsp");

                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                final StringBuffer sb2 = new StringBuffer();

                if (connection2.getResponseCode() == 200) {
                    while (true) {
                        String temp2 = br2.readLine();
                        if (temp2 == null)
                            break;
                        sb2.append(temp2);
                    }
                }
                //////////////JSON PARSING///////////////
                final String s2=sb2.toString();
                Log.d("Cognizant",s2);
                if(s2.contains("Levels"))
                {
                    JSONObject jsonObject2 = new JSONObject(s2);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("Levels");
                    for (int i = 0; i < jsonArray2.length(); i++)
                    {
                        JSONObject obj = jsonArray2.getJSONObject(i);
                        final int level_id = obj.getInt("level_id");
                        final String level_name = obj.getString("level_name");
                        final String llat = obj.getString("lat");
                        final String llng = obj.getString("lng");
                        final double levelLat = Double.parseDouble(llat);
                        final double levelLong = Double.parseDouble(llng);
                        final LatLng levelLocation = new LatLng(levelLat, levelLong);
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mMap.addMarker(new MarkerOptions().position(levelLocation).snippet("Level_name:" + level_name + " Lat:" + roundoff(levelLat) + " Lng:" + roundoff(levelLong))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Level " + level_id));
                            }
                        });
                    }
                }
                else
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Snackbar.make(ll, s2,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class LevelLocs2 implements Runnable {
        @Override
        public void run() {
            try {
                ///level data
                issendingl = true;
                String query2 = "?lat=" + myLat + "&lon=" + myLong ;
                URL url2 = new URL(weburl + "/jsonLevelLocs2.jsp"+query2);

                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();

                BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));

                final StringBuffer sb2 = new StringBuffer();

                if (connection2.getResponseCode() == 200) {
                    while (true) {
                        String temp2 = br2.readLine();
                        if (temp2 == null)
                            break;
                        sb2.append(temp2);
                    }
                }
                //////////////JSON PARSING///////////////
                final String s2=sb2.toString();
                Log.d("Cognizant",s2);
                if(s2.contains("Levels"))
                {
                    JSONObject jsonObject2 = new JSONObject(s2);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("Levels");
                    for (int i = 0; i < jsonArray2.length(); i++)
                    {
                        JSONObject obj = jsonArray2.getJSONObject(i);
                        final int level_id = obj.getInt("level_id");
                        final String level_name = obj.getString("level_name");
                        final String llat = obj.getString("lat");
                        final String llng = obj.getString("lng");
                        llatitude = Double.parseDouble(llat);
                        llongitude = Double.parseDouble(llng);
                        final LatLng levelLocation = new LatLng(llatitude, llongitude);
//                        getActivity().runOnUiThread(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                mMap.addMarker(new MarkerOptions().position(levelLocation).snippet("Level_name:" + level_name + " Lat:" + roundoff(llatitude) + " Lng:" + roundoff(llongitude))
//                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Level " + level_id));
//                            }
//                        });
                        if(getActivity()!=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Circle circle = mMap.addCircle(new CircleOptions()
                                            .center(levelLocation)
                                            .radius(disbwly*1000)
                                            .strokeColor(Color.BLUE));
                                    Circle circle2 = mMap.addCircle(new CircleOptions()
                                            .center(levelLocation)
                                            .radius(disbwlt*1000)
                                            .strokeColor(Color.GREEN));
                                }
                            });
                        }

                    }
                }
                else
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Snackbar.make(ll, s2,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                Thread.sleep(rate*1000*5);
                issendingl=false;
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TrainLocs implements Runnable {
        @Override
        public void run() {
            try {
                issendingt = true;
                String query = "?lat=" + myLat + "&lon=" + myLong ;

                URL url = new URL(weburl + "/jsonTrainLocs2" +query );

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                final StringBuffer sb = new StringBuffer();

                if (connection.getResponseCode() == 200) {
                    while (true) {
                        String temp = br.readLine();
                        if (temp == null)
                            break;
                        sb.append(temp);

                    }
                }
                //////////////JSON PARSING///////////////
                final String s=sb.toString();
                Log.d("Cognizant",s);
                if(s.contains("Trains")) {
                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArray = obj.getJSONArray("Trains");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final int train_id = jsonObject.getInt("train_id");
                        final String train_name = jsonObject.getString("train_name");
                        final String lat = jsonObject.getString("lat");
                        final String lng = jsonObject.getString("lng");
                        tlatitude = Double.parseDouble(lat);
                        tlongitude = Double.parseDouble(lng);
                        final LatLng trainLocation = new LatLng(tlatitude, tlongitude);
                        if(getActivity()!=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMap.addMarker(new MarkerOptions().position(trainLocation).snippet("Name:" + train_name + " Lat:" + tlatitude + " Lng:" + tlongitude)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Train" + train_id));
                                    if(traindetails)
                                        tv2.setText("Train Id:"+train_id+" Name:"+train_name);
                                }
                            });

                        }

                    }
                }
                else
                {
                    if(getActivity() != null)
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Snackbar.make(ll, s,
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            }
                        });
                    }
                    tlatitude=0;tlongitude=0;

                }

                final double d1=distance(tlatitude,tlongitude,myLat,myLong);

                final double d2=distance(llatitude,llongitude,myLat,myLong);

                final double d3=distance(llatitude,llongitude,tlatitude,tlongitude);
                if(getActivity()!=null)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String display="";
                            //display=display+"Distance b/w train & vehicle:"+d1+"km";
                            if(distl&&tlatitude!=0.0)
                                display=display+"Distance b/w level & train:"+roundoff(d3)+"km";
                            else if(distl)
                                display=display+"No Train coming at your nearest level crossing at this point of time";

                            if(disdl&&llatitude!=0.0)
                                display=display+"\nDistance b/w level & vehicle:"+roundoff(d2)+"km";
                            else if(disdl)
                                display=display+"\n No nearby level crossing found";

                            tv3.setText(display);
                        }
                    });
                }

                if(d3<disbwlt&&d2<disbwly) {
                    generateNoti();
                }

                Thread.sleep(1000*rate);
                issendingt = false;

            } catch (Exception e) {
                e.printStackTrace();
            }




        }
    }

    private void generateAlarm() {
    }

    private void generateNoti() {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity());
        builder.setContentTitle("Stop if you wanna save your life");
        builder.setContentText("Train is coming to your nearest level crossing");
        builder.setContentInfo("Warning");
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.warning);

        Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.location);
        builder.setLargeIcon(bmp);
        long vibrationPatter[] = {100, 340, 1000};
        builder.setVibrate(vibrationPatter);

        /////////////////  Attach Intent with Notification ////////
        Intent in=new Intent(getActivity(),MainActivity.class);
        PendingIntent pin= PendingIntent.getActivity(getActivity(),0,in,0);

        builder.setContentIntent(pin);
        //////////////////////////////////////////////////////////

        Notification notification1 = builder.build();


        NotificationManager notificationManager =
                (NotificationManager)(getActivity().getSystemService(NOTIFICATION_SERVICE));

        notificationManager.notify(20,notification1);
    }

    private void generateNoti2() {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity());
        builder.setContentTitle("Location Accuracy is very poor("+accuracy+")");
        builder.setContentText("Switch to another mode from App Settings");
        builder.setContentInfo("Warning");

        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_location);

        Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.ic_location);
        builder.setLargeIcon(bmp);


        /////////////////  Attach Intent with Notification ////////
        Intent in=new Intent(getActivity(), Settings.class);
        PendingIntent pin= PendingIntent.getActivity(getActivity(),0,in,0);

        builder.setContentIntent(pin);
        //////////////////////////////////////////////////////////

        Notification notification1 = builder.build();


        NotificationManager notificationManager =
                (NotificationManager)(getActivity().getSystemService(NOTIFICATION_SERVICE));

        notificationManager.notify(30,notification1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        running=true;
        Log.d("Cognizant","true from onresume");
        Log.d("Cognizant","onresume");
        //new Thread(new TrainLocs(myLat,myLong)).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        running=false;
        Log.d("Cognizant","false from onPause");
        Log.d("Cognizant","onpause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Log.d("Cognizant","ondestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        Log.d("Cognizant","onresume");
    }

    private double roundoff(double d)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(d));
    }
    //////////////////////////////////////////////////////////////
}
//SELECT * FROM(
// SELECT *,(((acos(sin((@latitude*pi()/180)) * sin((Latitude*pi()/180))+cos((@latitude*pi()/180)) * cos((Latitude*pi()/180)) * cos(((@longitude - Longitude)*pi()/180))))
// *180/pi())*60*1.1515*1.609344) as distance FROM Distances) t
  //      WHERE distance <= @distance