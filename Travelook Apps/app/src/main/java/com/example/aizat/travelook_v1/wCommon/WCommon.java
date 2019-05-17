package com.example.aizat.travelook_v1.wCommon;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WCommon {

    public static final  String APP_ID = "1a75bb0270659eb1ae94a97e232987e5";
    public static Location current_location = null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy, HH:mm");
        String formatted = sdf.format(date);
        return formatted;

    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }
}
