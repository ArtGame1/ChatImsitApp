package com.example.chatimsitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.chatimsitapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "RegisterActivity started");

        // Обработчик кнопки "Login"
        binding.loginClickTv.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Обработчик кнопки "Back"
        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Обработчик кнопки "Sign Up"
        binding.signUpBtn.setOnClickListener(v -> {
            Log.d(TAG, "Sign up button clicked");

            String email = binding.emailEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();
            String username = binding.usernameEt.getText().toString().trim();

            // Проверка 1: Поля не должны быть пустыми
            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка 2: Минимальная длина пароля
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка 3: Проверка формата email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplicationContext(), "Пожалуйста введите email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка 4: Имя пользователя
            if (username.length() < 3) {
                Toast.makeText(getApplicationContext(), "Имя пользователя должно содержать не менее 3 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем данные пользователя
            if (saveUserData(email, username, password)) {
                Toast.makeText(getApplicationContext(), "Успешная регистрация!", Toast.LENGTH_SHORT).show();

                // Переходим на LoginActivity и передаем email
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("REGISTERED_EMAIL", email); // Передаем email для удобства
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Очищаем стек
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Метод для сохранения данных пользователя
    private boolean saveUserData(String email, String username, String password) {
        try {
            // Проверяем, не зарегистрирован ли уже этот email
            String existingUser = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .getString("user_email_" + email, null);

            if (existingUser != null) {
                return false; // Пользователь уже существует
            }

            // Сохраняем данные пользователя
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("user_email_" + email, email)
                    .putString("user_name_" + email, username)
                    .putString("user_password_" + email, password) // Сохраняем пароль
                    .apply();

            Log.d(TAG, "User registered: " + email + ", Username: " + username);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving user data", e);
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RegisterActivity destroyed");
    }
}