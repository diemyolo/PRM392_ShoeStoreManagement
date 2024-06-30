package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        TextView signInRedirect = findViewById(R.id.signInRedirect);
        Button buttonRegister = findViewById(R.id.buttonGetStarted);

        signInRedirect.setOnClickListener(view -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        });

        buttonRegister.setOnClickListener(view -> {
            startActivity(new Intent(IntroActivity.this, SignUp.class));
        });
    }
}