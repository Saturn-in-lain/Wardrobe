package com.wardrob.wardrob.weather.helper;

import com.wardrob.wardrob.weather.common.Common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import timber.log.Timber;

public class WeatherHttpClient
{

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
                                                    //706483 - Kharkiv
    /**
     * Function: getWeatherData
     * Description:
     * @param url
     * @return
     */
    public String getWeatherData(String url)
    {
        HttpURLConnection con = null;
        InputStream is = null;

        try
        {
            con = (HttpURLConnection) (new URL(url)).openConnection();
            try
            {
                if (con.getResponseCode() == 200)
                {
                    //con.setDoInput(true);
                    con.setRequestMethod("GET");
                    //con.setDoOutput(true);
                    con.connect();

                    // Let's read the response
                    StringBuffer buffer = new StringBuffer();
                    is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        //buffer.append(line);
                        buffer.append(line + "rn");
                    }

                    is.close();
                    con.disconnect();
                    return buffer.toString();
                }
                else
                {
                    Timber.d("Response was not 200 :(");
                }
            }
            catch (ProtocolException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}

        }
        catch (Throwable t) {t.printStackTrace();}
        finally
        {
            try {is.close();}
            catch (Throwable t) {}
            try {con.disconnect();}
            catch (Throwable t) { }
        }
        return null;
    }

    /**
     * Function: getImage
     * Description:
     * @param code
     * @return
     */
    public byte[] getImage(String code)
    {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(Common.IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

}
