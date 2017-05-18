package com.example.raminder.warningsystemforunmannedlevelcrossing;

/**
 * Created by Raminder on 4/26/2017.
 */

public class GlobalApp {
    static String ip1="192.168.1.6";
    static String ip2="192.168.43.97";
    static String socket="8084";
    static String localwifi="http://"+ GlobalApp.ip1 +":"+GlobalApp.socket +"/unmannedlevelcrossing";
    static String localhotspot="http://"+ GlobalApp.ip2 +":"+GlobalApp.socket +"/unmannedlevelcrossing";
    static String cloud="http://ulcws.cloud.cms500.com";
    static String weburl=cloud;
    static int rate=10;
    static boolean distl=true;
    static boolean disdl=true;
    static boolean traindetails=true;
    static double disbwlt=2.0;
    static double disbwly=1.0;
    static boolean running = true;
    static boolean alarmEnabled=true;
}
