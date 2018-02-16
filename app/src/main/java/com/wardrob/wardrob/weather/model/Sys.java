package com.wardrob.wardrob.weather.model;

/**
 * Created by stas.savinov on 16.02.2018.
 */
public class Sys
{
    private double message;

    public double getMessage() { return this.message; }

    public void setMessage(double message) { this.message = message; }

    private String country;

    public String getCountry() { return this.country; }

    public void setCountry(String country) { this.country = country; }

    private int sunrise;

    public int getSunrise() { return this.sunrise; }

    public void setSunrise(int sunrise) { this.sunrise = sunrise; }

    private int sunset;

    public int getSunset() { return this.sunset; }

    public void setSunset(int sunset) { this.sunset = sunset; }
}
