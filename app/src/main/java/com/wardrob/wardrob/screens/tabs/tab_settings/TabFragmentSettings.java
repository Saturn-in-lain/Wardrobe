package com.wardrob.wardrob.screens.tabs.tab_settings;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.wardrob.wardrob.R;
import com.wardrob.wardrob.weather.common.Common;
import com.wardrob.wardrob.weather.helper.WeatherHttpClient;
import com.wardrob.wardrob.weather.model.MainWeatherContainer;

import java.lang.reflect.Type;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_fragment_settings, container, false);

        context = view.getContext();

        initUIElements();

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        Timber.d("Provide: " + provider.toString());

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

        Location location = locationManager.getLastKnownLocation(provider);
        if(null == location){Timber.d("No location found");}

        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
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
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
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
        locationManager.requestLocationUpdates(provider,400,1,this);

    }

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

    @Override
    public void onLocationChanged(Location location)
    {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat),String.valueOf(lng)));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

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
            stream = http.getWeatherData(Common.apiRequest(Double.valueOf(lat).toString(),
                                                           Double.valueOf(lng).toString()));



            return stream;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            if(s.contains("Error:"))
            {
                pd.dismiss();
                return;
            }

            Gson gson = new Gson();
            Type mType = new TypeToken<MainWeatherContainer>(){}.getType();
            mainWeatherContainer = gson.fromJson(s,mType);
            pd.dismiss();

            cityText.setText(String.format("%s %s", mainWeatherContainer.getName(),
                                                    mainWeatherContainer.getSys().getCountry()));
            condDescr.setText(mainWeatherContainer.getWeather().get(0).getDescription());

            temp.setText(Double.valueOf(mainWeatherContainer.getMain().getTemp()).toString());
            press.setText(Double.valueOf(mainWeatherContainer.getMain().getPressure()).toString());
            hum.setText(mainWeatherContainer.getMain().getHumidity());

            windSpeed.setText(Double.valueOf(mainWeatherContainer.getWind().getSpeed()).toString());
            windDeg.setText(mainWeatherContainer.getWind().getDeg());

            Picasso.with(context)
                        .load(Common.getImage(mainWeatherContainer.getWeather().get(0).getIcon()))
                        .into(condIcon);

        }
    }



}
