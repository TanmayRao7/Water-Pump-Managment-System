package com.tanmay.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    Button login;
    TextView email;
    TextView password;

    TextView signupText;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //private String BASE_URL = "http://10.0.2.2:3000";
    private static final String BASE_URL = "http://192.168.1.20:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login =  findViewById(R.id.singupButton);
        email =  findViewById(R.id.emailSignText);
        password =  findViewById(R.id.passwordSignText);
        signupText =  findViewById(R.id.signupTextView);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
                finish();
            }
        });

//
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", email.getText().toString().trim());
                    map.put("password", password.getText().toString().trim());

                    Call<LoginResult> call = retrofitInterface.executeLogin(map);
                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                            if (response.code() == 200) {
                                LoginResult result = response.body();
                                Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                                Intent i = new
                                        Intent(Login.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            } else if (response.code() == 400) {
                                Toast.makeText(Login.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {
                            Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        // Check if email field is empty and if it is a valid email address
        if (TextUtils.isEmpty(emailString)) {
            email.setError("Email is required.");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Invalid email address.");
            isValid = false;
        }

        // Check if password field is empty
        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Password is required.");
            isValid = false;
        }

        return isValid;
    }
}