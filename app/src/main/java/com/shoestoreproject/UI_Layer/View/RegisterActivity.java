package com.shoestoreproject.UI_Layer.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.shoestoreproject.R;
import com.shoestoreproject.UI_Layer.ViewModel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;
    private EditText userNameTxt, emailTxt, passwordTxt, confirmPasswordTxt;
    private TextView textViewMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        userNameTxt = findViewById(R.id.editTextUserName);
        emailTxt = findViewById(R.id.editTextEmail);
        passwordTxt = findViewById(R.id.editTextPassword);
        confirmPasswordTxt = findViewById(R.id.editTextConfirmPassword);
        textViewMessage = findViewById(R.id.textViewMessage);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView signInRedirect = findViewById(R.id.signInRedirect);

        buttonRegister.setOnClickListener(view -> {
            String userName = userNameTxt.getText().toString().trim();
            String email = emailTxt.getText().toString().trim();
            String password = passwordTxt.getText().toString().trim();
            String confirmPassword = confirmPasswordTxt.getText().toString().trim();

            registerViewModel.register(email, password, userName).observe(this, registerResult -> {
                if(!confirmPassword.equals(password)){
                    textViewMessage.setText("Confirm password does not match");
                }else if (registerResult.equals("Registration successfully")) {
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, registerResult, Toast.LENGTH_SHORT).show();
                }
            });
        });

        signInRedirect.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }
}