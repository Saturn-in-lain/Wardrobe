package com.wardrob.wardrob.weather.common;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class Common
{
    //8f721d077e8dc4d8ac8868ae344e74bc
    public static String API_KEY = "7213c043a8f0e9f13383f7acf15bbc96";
    public static String API_LINK = "api.openweathermap.org/data/2.5/weather";

    //http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=7213c043a8f0e9f13383f7acf15bbc96&units-metric
    public static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    public static String IMG_URL = "http://openweathermap.org/img/w/";

    public static String apiRequest(String lat, String lng)
    {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units-metric",lat,lng,API_KEY));
        Timber.d("This is URL: " + sb.toString());
        return sb.toString();
    }

    public static String convertTime(double initTime)
    {
        DateFormat date = new SimpleDateFormat("HH:mm");
        Date time = new Date();
        time.setTime((long) initTime*1000);
        return date.format(time);
    }


    public static String getImage(String icon)
    {
        return String.format(IMG_URL + "/%s.png",icon);
    }


    public static String getDateNow()
    {
        DateFormat dateF = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateF.format(date);
    }

}
