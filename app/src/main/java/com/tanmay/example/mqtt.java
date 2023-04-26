package com.tanmay.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class mqtt extends AppCompatActivity {


    private static final String BROKER_URL = "tcp://a148u5sq3d9wyr-ats.iot.us-east-1.amazonaws.com:1883";
    private static final String CLIENT_ID = "client123";
    private MqttHandler mqttHandler;

    Button connect_button;

    EditText text;
    Button publish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        mqttHandler = new MqttHandler();
        connect_button = findViewById(R.id.connect);
        text = findViewById(R.id.textView2);
        publish = findViewById(R.id.button2);



        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mqttHandler.connect(BROKER_URL,CLIENT_ID);

                Toast.makeText(getApplicationContext(), "Client has been connected", Toast.LENGTH_LONG).show();

            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text.getText().toString().trim();
                String topic = "esp8266/sub";
                mqttHandler.publish(topic,message);
                Toast.makeText(mqtt.this, "Published Message:"+ message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "Client has been disconnected", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}