package com.shoestoreproject.UI_Layer.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.shoestoreproject.R;
import com.shoestoreproject.UI_Layer.ViewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText emailTxt, passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailTxt = findViewById(R.id.editTextEmail);
        passwordTxt = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView signUpRedirect = findViewById(R.id.signUpRedirect);

        buttonLogin.setOnClickListener(view -> {
            String email = emailTxt.getText().toString().trim();
            String password = passwordTxt.getText().toString().trim();

            loginViewModel.login(email, password).observe(this, loginResult -> {
                if (loginResult.equals("Login successfully")) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, loginResult, Toast.LENGTH_SHORT).show();
                }
            });
        });
        signUpRedirect.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}