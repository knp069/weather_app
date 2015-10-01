package me.nishantkumar.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;


    @Bind(R.id.timelable) TextView mTimeLabel;
    @Bind(R.id.locationlabel) TextView mlocationlabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityvalue) TextView mHumidityValue;
    @Bind(R.id.percipvalue) TextView mPrecipValue;
    @Bind(R.id.summarylabel) TextView mSummaryLabel;
    @Bind(R.id.iconimageview) ImageView mIconImageView;
    @Bind(R.id.refreshbutton) Button mRefreshButton;
    @Bind(R.id.progressBar) ProgressBar mprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mprogressbar.setVisibility(View.INVISIBLE);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });

        getForecast();
        Log.d(TAG , "Main UI code is running ");
    }

    private void getForecast() {
        String apikey = "ee9f9f1a32f1724a160da42719a74d3d";
        double latitude = 21.1500;
        double longitude = 79.0900;
        String forecasturl = "https://api.forecast.io/forecast/"+apikey+"/"+latitude+","+longitude;
        if (isNetworkAvailable()) {

            toggleRefrresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecasturl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefrresh();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefrresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught : ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught : ", e);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefrresh() {
        if (mprogressbar.getVisibility() == View.INVISIBLE){
            mprogressbar.setVisibility(View.VISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
        }
        else{
            mprogressbar.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getmTemprature()+"");
        mTimeLabel.setText("At "+mCurrentWeather.getFormattedTime()+" it will be ");
        mHumidityValue.setText(mCurrentWeather.getmHumidity()+"");
        mSummaryLabel.setText(mCurrentWeather.getmSummary());
        mlocationlabel.setText(mCurrentWeather.getmTimezone());
        mPrecipValue.setText(mCurrentWeather.getmPrecipitation() + "%");
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }



    private CurrentWeather getCurrentDetails(String jsondata) throws JSONException{
        JSONObject forecast = new JSONObject(jsondata);
        String timeZone = forecast.getString("timezone");
        Log.i(TAG , "From JSON : " + timeZone);

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentweather = new CurrentWeather();
        currentweather.setmHumidity(currently.getDouble("humidity"));
        currentweather.setmTime(currently.getLong("time"));
        currentweather.setmicon(currently.getString("icon"));
        currentweather.setmPrecipitation(currently.getDouble("precipProbability"));
        currentweather.setmSummary(currently.getString("summary"));
        currentweather.setmTemprature(currently.getDouble("temperature"));
        currentweather.setmTimezone(timeZone);

        Log.d(TAG, currentweather.getFormattedTime());

        return currentweather;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkinfo != null && networkinfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;

    }
}
