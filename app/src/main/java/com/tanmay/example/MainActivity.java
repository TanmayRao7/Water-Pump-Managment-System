package com.tanmay.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button stopButton;
    ListView status_list;
    TextView textView;
    private final List<PumpData> waterPumpDataList = new ArrayList<>();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //private String BASE_URL = "http://10.0.2.2:3000";
    private static final String BASE_URL = "http://192.168.1.20:3000";

    ProgressBar water_level_progress_bar;

    ProgressBar energy_level_progress_bar;
    Random random = new Random();

    TextView last_updated_time;

    ImageButton refresh ;

    ArrayAdapter<PumpData> arrayAdapter;

    SimpleDateFormat sdf;

    TextView water_state;

    ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.logOutButton);
        water_level_progress_bar = findViewById(R.id.water_level_bar);
        energy_level_progress_bar = findViewById(R.id.energy_level_bar);
        // Initializing variables
        refresh = findViewById(R.id.image_button);
        //Start Button
        startButton = findViewById(R.id.button_start);
        //Stop Button
        stopButton = findViewById(R.id.button_stop);
        // Water Status Textview
        water_state = (TextView) findViewById(R.id.waterStatus);
        // Time status TextView
        last_updated_time =  findViewById(R.id.timeTextView);
        // Listview , Array Adapter
        status_list = findViewById(R.id.status_listview);
        //ArrayAdapter<PumpData> arrayAdapter = new ArrayAdapter<>( getApplicationContext(),android.R.layout.simple_list_item_1, waterPumpDataList);
        arrayAdapter = new ArrayAdapter<>( getApplicationContext(),android.R.layout.simple_list_item_1, waterPumpDataList);
        status_list.setAdapter(arrayAdapter);
        // Logo Textview
        textView = findViewById(R.id.textView);

        // Formatted Time
        sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());

        // Retrofit Initialization
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        updateStatus();
        refresh();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        // Button to update list
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                },5000);

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                },5000);
            }
        });

    }

    public void refresh(){
        Call<List<PumpData>> call = retrofitInterface.getPumpData();
        call.enqueue(new Callback<List<PumpData>>() {
            @Override
            public void onResponse(Call<List<PumpData>> call, Response<List<PumpData>> response) {
                if(response.code() == 200){
                    waterPumpDataList.clear();
                    List<PumpData> pumpDataList = response.body(); // get the list from the response
                    arrayAdapter.addAll(pumpDataList); // add all elements to the adapter
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PumpData>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void start(){

        int min = 50;
        int max = 100;
        int random_water_level = random.nextInt(max-min+1)+min;
        int random_energy_level = random.nextInt(max-min+1)+min;
        PumpData data = new PumpData("Online", random_water_level, random_energy_level);
        Call<PumpData> call = retrofitInterface.addData(data);
        call.enqueue(new Callback<PumpData>() {
            @Override
            public void onResponse(Call<PumpData> call, Response<PumpData> response) {
                Toast.makeText(MainActivity.this, "Water Pump has been started", Toast.LENGTH_SHORT).show();
                water_level_progress_bar.setProgress(random_water_level);
                energy_level_progress_bar.setProgress(random_energy_level);
                String currentDateAndTime = sdf.format(new Date());
                last_updated_time.setText(currentDateAndTime);
                water_state.setText("Online");
                water_state.setTextColor(Color.parseColor("#7CB342"));
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<PumpData> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void stop(){
        int min = 50;
        int max = 100;
        int random_water_level = random.nextInt(max-min+1)+min;
        int random_energy_level = random.nextInt(max-min+1)+min;
        PumpData data = new PumpData("Offline", random_water_level, random_energy_level);
        Call<PumpData> call = retrofitInterface.addData(data);
        call.enqueue(new Callback<PumpData>() {
            @Override
            public void onResponse(Call<PumpData> call, Response<PumpData> response) {

                Toast.makeText(MainActivity.this, "Water Pump has been stopped", Toast.LENGTH_SHORT).show();
                water_level_progress_bar.setProgress(random_water_level);
                energy_level_progress_bar.setProgress(random_energy_level);
                String currentDateAndTime = sdf.format(new Date());
                last_updated_time.setText(currentDateAndTime);
                water_state.setText("Offline");
                water_state.setTextColor(Color.RED);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
            @Override
            public void onFailure(Call<PumpData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Sever Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatus(){

        Call<List<PumpData>> call = retrofitInterface.getPumpData();

        call.enqueue(new Callback<List<PumpData>>() {
            @Override
            public void onResponse(Call<List<PumpData>> call, Response<List<PumpData>> response) {
                if(response.code() == 200){
                    List<PumpData> list = response.body();
                    PumpData pumpData = list.get(0);
                    String datetime = pumpData.getTime();
                    String Status = pumpData.getPumpStatus();
                    int water_level = pumpData.getWaterLevel();
                    int energy_level = pumpData.getEnergyLevel();
                    water_level_progress_bar.setProgress(water_level);
                    energy_level_progress_bar.setProgress(energy_level);
                    last_updated_time.setText(datetime);
                    if(Status.equals("Offline")){
                        water_state.setText("Offline");
                        water_state.setTextColor(Color.RED);
                        stopButton.setEnabled(false);
                        startButton.setEnabled(true);
                    }else if(Status.equals("Online")){
                        water_state.setText("Online");
                        water_state.setTextColor(Color.parseColor("#7CB342"));
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                    }
                }

            }
            @Override
            public void onFailure(Call<List<PumpData>> call, Throwable t) {

            }
        });

    }



}