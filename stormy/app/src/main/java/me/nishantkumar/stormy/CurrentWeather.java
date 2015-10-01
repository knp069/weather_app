package me.nishantkumar.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nishant on 9/17/2015.
 */
public class CurrentWeather {
    private String micon;
    private long mTime;
    private double mTemprature;
    private double mHumidity;
    private double mPrecipitation;
    private String mSummary;
    private String mTimezone;

    public String getmTimezone() {
        return mTimezone;
    }

    public void setmTimezone(String mTimezone) {
        this.mTimezone = mTimezone;
    }

    public String getmicon() {
        return micon;
    }

    public void setmicon(String micon) {
        this.micon = micon;
    }

    public int getIconId(){
        int iconId = R.mipmap.clear_day;

        if(micon.equals("clear-day")){
            iconId = R.mipmap.clear_day;
        }
        else if(micon.equals("clear-night")){
            iconId = R.mipmap.clear_night;
        }
        else if (micon.equals("rain")) {
            iconId = R.mipmap.rain;
        }
        else if (micon.equals("snow")) {
            iconId = R.mipmap.snow;
        }
        else if (micon.equals("sleet")) {
            iconId = R.mipmap.sleet;
        }
        else if (micon.equals("wind")) {
            iconId = R.mipmap.wind;
        }
        else if (micon.equals("fog")) {
            iconId = R.mipmap.fog;
        }
        else if (micon.equals("cloudy")) {
            iconId = R.mipmap.cloudy;
        }
        else if (micon.equals("partly-cloudy-day")) {
            iconId = R.mipmap.partly_cloudy;
        }
        else if (micon.equals("partly-cloudy-night")) {
            iconId = R.mipmap.cloudy_night;
        }
        return iconId;
    }

    public long getmTime() {
        return mTime;
    }
    public String getFormattedTime(){
        SimpleDateFormat formatter  = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getmTimezone()));
        Date datetime = new Date(getmTime()*1000);
        String timeString = formatter.format(datetime);
        return timeString;
    }
    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getmTemprature() {
        return (int)Math.round(mTemprature);
    }

    public void setmTemprature(double mTemprature) {
        this.mTemprature = mTemprature;
    }

    public double getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(double mHumidity) {
        this.mHumidity = mHumidity;
    }

    public int getmPrecipitation() {
        double percip = mPrecipitation*100;
        return (int)Math.round(percip);
    }

    public void setmPrecipitation(double mPrecipitation) {
        this.mPrecipitation = mPrecipitation;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }
}
