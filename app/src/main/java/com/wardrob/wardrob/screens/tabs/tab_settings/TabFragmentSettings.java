package com.wardrob.wardrob.screens.tabs.tab_settings;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.weather.common.Common;
import com.wardrob.wardrob.weather.helper.WeatherHttpClient;
import com.wardrob.wardrob.weather.model.MainWeatherContainer;

import java.lang.reflect.Type;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class TabFragmentSettings extends Fragment implements TabFragmentSettingsView, LocationListener

{
    View view;
    LocationManager locationManager;
    String provider;

    int PERMISSION = 0;
    Context context;
    static double lat, lng;

    MainWeatherContainer mainWeatherContainer = new MainWeatherContainer();
    TextView cityText, condDescr, temp, pressLab, press, humLab, hum, windLab, windSpeed, windDeg;
    ImageView condIcon;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_fragment_settings, container, false);

        context = view.getContext();

        initUIElements();

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        Timber.d("Provide: " + provider.toString());

        checkPermissions();
        Location location = locationManager.getLastKnownLocation(provider);
        if(null == location){Timber.d("No location found");}
        onLocationChanged(location);

        return view;
    }



    /**
     *  @Override Handle onPause
     */

    public void onPause()
    {
        super.onPause();
        checkPermissions();
        locationManager.removeUpdates(this);
    }

    /**
     *  @Override Handle onResume
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onResume()
    {
        super.onResume();
        checkPermissions();
        locationManager.requestLocationUpdates(provider,400,1,this);
    }

    /**
     *  @Override Handle onStop
     */
    @Override
    public void onStop()
    {
        super.onStop();
    }

    /**
     * Function: checkPermissions
     * Note: @SuppressLint("MissingPermission") now required, but no code duplicates!
     * @param
     * @return:
     */
    private void checkPermissions()
    {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.SYSTEM_ALERT_WINDOW
                    }, PERMISSION);

        }
    }

    /**
     * Function: initUIElements
     * @return:
     */
    private void initUIElements()
    {
        cityText    = (TextView) view.findViewById(R.id.cityText );
        condDescr   = (TextView) view.findViewById(R.id.condDescr);
        temp        = (TextView) view.findViewById(R.id.temp     );
        pressLab    = (TextView) view.findViewById(R.id.pressLab );
        press       = (TextView) view.findViewById(R.id.press    );
        humLab      = (TextView) view.findViewById(R.id.humLab   );
        hum         = (TextView) view.findViewById(R.id.hum      );
        windLab     = (TextView) view.findViewById(R.id.windLab  );
        windSpeed   = (TextView) view.findViewById(R.id.windSpeed);
        windDeg     = (TextView) view.findViewById(R.id.windDeg  );
        condIcon    = (ImageView) view.findViewById(R.id.condIcon);
    }

    /**
     * Function: onLocationChanged
     * @param location
     * @return:
     */
    public void onLocationChanged(Location location)
    {
        if(null != location)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
            new GetWeather().execute(Common.apiRequest(String.valueOf(lat),String.valueOf(lng)));
        }
        else
        {
            // Kharkov for testing
            lat = 36.25;
            lng = 50;
            String city_id = "706483"; // Kharkov demo
            new GetWeather().execute(Common.apiRequestBiCityId(city_id));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    private class GetWeather extends AsyncTask<String, Void, String>
    {
        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd.setTitle("Collect weather data...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            String stream;
            String urlString = strings[0];

            WeatherHttpClient http = new WeatherHttpClient();
//            stream = http.getWeatherData(Common.apiRequest(Double.valueOf(lat).toString(),
//                                                           Double.valueOf(lng).toString()));
            stream = http.getWeatherData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            if (null != s)
            {
                if (s.contains("Error:")) {
                    pd.dismiss();
                    return;
                }
            }
            else
            {
                Timber.d("Returned message as gson is empty!!! ");
            }

            Gson gson = new Gson();
            Type mType = new TypeToken<MainWeatherContainer>(){}.getType();
            try
            {
                s = s.replaceAll("rn", ""); //TODO Why this happens? --> [getWeatherData] adds it!
                mainWeatherContainer = gson.fromJson(s,mType);
            }
            catch (IllegalStateException | JsonSyntaxException exception)
            {
                Timber.d("\t\t--[Illegal JSON exception!]--> " + exception.toString());
            }

            pd.dismiss();

            if(null != mainWeatherContainer)
            {
                try {
                    cityText.setText(String.format("%s %s", mainWeatherContainer.getName(),
                            mainWeatherContainer.getSys().getCountry()));
                    condDescr.setText(mainWeatherContainer.getWeather().get(0).getDescription());

                    String temp_in_celc =
                        Double.valueOf(mainWeatherContainer.getMain().getTemp() - 273.15).toString();
                    temp.setText(temp_in_celc);

                    press.setText(Double.valueOf(mainWeatherContainer.getMain().getPressure()).toString());
                    hum.setText(String.valueOf(mainWeatherContainer.getMain().getHumidity()));

                    windSpeed.setText(Double.valueOf(mainWeatherContainer.getWind().getSpeed()).toString());
                    windDeg.setText(String.valueOf(mainWeatherContainer.getWind().getDeg()));

                    Picasso.with(context)
                            .load(Common.getImage(mainWeatherContainer.getWeather().get(0).getIcon()))
                            .into(condIcon);
                }
                catch (NullPointerException e)
                {
                    Timber.d("GSON was not able to detect data...");
                }
            }
            else
            {
                Toast.makeText(context,"data from server was empty", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
