package com.example.chatimsitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.chatimsitapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "LoginActivity started");

        // Проверяем, если пользователь уже "вошел" (сохранен в SharedPreferences)
        boolean isLoggedIn = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            Log.d(TAG, "User already logged in (locally), redirecting to MainActivity");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Обработчик кнопки "Login"
        binding.loginBtn.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");

            String email = binding.emailEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();

            // Простая проверка
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка формата email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка длины пароля
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // В реальном приложении здесь была бы проверка с Firebase
            // Сейчас просто имитируем успешный вход
            Log.d(TAG, "Login successful (simulated)");
            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();

            // Сохраняем данные локально
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("user_email", email)
                    .putBoolean("is_logged_in", true)
                    .apply();

            // Переход в MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Обработчик текста "Sign up"
        binding.goToRegisterActivityTv.setOnClickListener(v -> {
            Log.d(TAG, "Sign up text clicked");
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }
}