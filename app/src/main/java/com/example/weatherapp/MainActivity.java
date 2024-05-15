package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.model.WeatherItemList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//8d746358c28483ba3f55131ba9f7ff06 API KEY
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;  // Declare the binding variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the binding and set the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchWeatherData("jaipur");
        SearchCity();

    }

    private void SearchCity() {
       SearchView searchview= binding.search;
       searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               fetchWeatherData(query);
               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return true;
           }
       });
    }

    private void fetchWeatherData(String cityname) {
        Call<WeatherItemList> listCall = ApiClient.getRetrofit().create(APiInterface.class).getWeatherData(cityname, "8d746358c28483ba3f55131ba9f7ff06", "metric");
        listCall.enqueue(new Callback<WeatherItemList>() {
            @Override
            public void onResponse(Call<WeatherItemList> call, Response<WeatherItemList> response) {
                if (response.isSuccessful()) {
                    String temp = String.valueOf(response.body().main.getTemp());
                    String humidity = String.valueOf(response.body().main.getHumidity());
                    String windspeed = String.valueOf(response.body().wind.getSpeed());
                    String sunRise = String.valueOf(response.body().sys.getSunrise());
                    String sunSet = String.valueOf(response.body().sys.getSunset());
                    String sealevel = String.valueOf(response.body().main.getPressure());
                    String max = String.valueOf(response.body().main.getTemp_max());
                    String min = String.valueOf(response.body().main.getTemp_min());

                    String condition;
                    if (response.body().weather == null || response.body().weather.isEmpty()) {
                     condition  = "unknown"; // Default value if the list is null or empty
                    } else {
                        WeatherItemList.Weather firstWeather = response.body().weather.get(0); // Get the first element
                        condition = firstWeather != null && firstWeather.main != null
                                ? firstWeather.main // Get the 'main' property
                                : "unknown"; // Default value if 'main' is null
                    }
                    binding.temperature.setText(temp + "°C");
                 binding.condition.setText(condition);
                 binding.Weather.setText(condition);
                    binding.maxTemp.setText(max + "°C");
                    binding.minTemp.setText(min + "°C");
                    binding.hummidity.setText(humidity + "%");
                    binding.windspeed.setText(windspeed + "m/s");
                    binding.sunrise.setText(time(Long.valueOf(sunRise)));
                    binding.sunset.setText(time(Long.valueOf(sunSet)));
                    binding.sea.setText(sealevel + "hpa");
                    binding.day.setText(dayname(System.currentTimeMillis()));
                    binding.date.setText(date());
                    binding.cityname.setText(cityname);
                    changeimagesAccordingtoCondn(condition);


//            Log.d("hello",itemList);


                }
            }

            private void changeimagesAccordingtoCondn(String conditions) {
                if (conditions.equals("Clear")|| conditions.equals("Sunny")|| conditions.equals("Clear Sky")){
                    binding.main.setBackgroundResource(R.drawable.sunny_background);
                    binding.lottieAnimationView10.setAnimation(R.raw.sun);

                }
               else if (conditions.equals("Partly Clouds")|| conditions.equals("Clouds")|| conditions.equals("Foggy")|| conditions.equals("Overcast")){
                    binding.main.setBackgroundResource(R.drawable.colud_background);
                    binding.lottieAnimationView10.setAnimation(R.raw.cloud);

                }

                else if (conditions.equals("Light Rain")|| conditions.equals("Moderate Rain")|| conditions.equals("Heavy Rain")|| conditions.equals("Drizzle")){
                    binding.main.setBackgroundResource(R.drawable.rain_background);
                    binding.lottieAnimationView10.setAnimation(R.raw.rain);

                }
                else if (conditions.equals("Lignt Snow")|| conditions.equals("Moderate Show")|| conditions.equals("Heavy Rain")|| conditions.equals("Drizzle")){
                    binding.main.setBackgroundResource(R.drawable.snow_background);
                    binding.lottieAnimationView10.setAnimation(R.raw.snow);

                }
                else {
                    binding.main.setBackgroundResource(R.drawable.sunny_background);
                    binding.lottieAnimationView10.setAnimation(R.raw.sun);

                }


                binding.lottieAnimationView10.playAnimation();

            }

            @Override
            public void onFailure(Call<WeatherItemList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("test", "faill");

            }
        });


    }

    public String dayname(Long timestamp) {

        Calendar calendar = Calendar.getInstance();

        // Get the day of the week as a full name (e.g., "Monday")
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        // Format the day of the week
        String dayOfWeek = dayFormat.format(calendar.getTime());
        return  dayOfWeek;

    }

    public String date() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());



        return formattedDate;
    }
    public String time(Long timestamp) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Format the time
        String formattedTime = timeFormat.format(timestamp*1000);




        return formattedTime;
    }

}