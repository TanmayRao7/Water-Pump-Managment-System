package com.tanmay.example;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Register extends AppCompatActivity {

    TextView registerText;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
//    private String BASE_URL = "http://10.0.2.2:3000";
    private String BASE_URL = "http://192.168.1.20:3000";

    TextView email;
    TextView password;
    TextView name;
    Button signup;
    TextView loginTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Variable Initialization
        registerText = findViewById(R.id.registerText1);
        email =  findViewById(R.id.emailSignText);
        password =  findViewById(R.id.passwordSignText);
        name =  findViewById(R.id.nameSignText);
        signup =  findViewById(R.id.singupButton);
        loginTextView = findViewById(R.id.loginTextView);


        // Retrofit Builder

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        // Login Text View Intent

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
                finish();
            }
        });


        // Signup Button

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateForm()){

                HashMap<String,String> map = new HashMap<>();
                map.put("name",name.getText().toString());
                map.put("email",email.getText().toString());
                map.put("password",password.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if(response.code() == 200){
                            Toast.makeText(Register.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Register.this,Login.class);
                            startActivity(i);
                            finish();
                        }
                        else if(response.code() == 400){
                            Toast.makeText(Register.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                        Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
            }
        });

    }

    // Form Validation function

    private boolean validateForm() {
        boolean isValid = true;
        String nameString = name.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        // Check if name field is empty
        if (TextUtils.isEmpty(nameString)) {
            name.setError("Name is required.");
            isValid = false;
        }

        // Check if email field is empty and if it is a valid email address
        if (TextUtils.isEmpty(emailString)) {
            email.setError("Email is required.");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Invalid email address.");
            isValid = false;
        }

        // Check if password field is empty and if it is at least 6 characters long
        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Password is required.");
            isValid = false;
        } else if (passwordString.length() < 6) {
            password.setError("Password must be at least 6 characters long.");
            isValid = false;
        }

        return isValid;
    }


}
