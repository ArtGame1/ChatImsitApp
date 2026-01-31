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

        // Обработчик кнопки "Login" - переходит в LoginActivity
        binding.loginClickTv.setOnClickListener(view -> {
            Log.d(TAG, "Login text clicked");
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        // Обработчик кнопки "Back"
        binding.backBtn.setOnClickListener(view -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });

        // Обработчик кнопки "Sign Up"
        binding.signUpBtn.setOnClickListener(v -> {
            Log.d(TAG, "Sign up button clicked");

            String email = binding.emailEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();
            String username = binding.usernameEt.getText().toString().trim();

            Log.d(TAG, "Email: " + email + ", Username: " + username + ", Password length: " + password.length());

            // Проверка 1: Поля не должны быть пустыми
            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка 2: Минимальная длина пароля (6 символов)
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка 3: Проверка формата email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка 4: Имя пользователя должно быть не менее 3 символов
            if (username.length() < 3) {
                Toast.makeText(getApplicationContext(), "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Все проверки пройдены - переходим в MainActivity
            Log.d(TAG, "All validations passed, navigating to MainActivity");

            // Сохраняем данные пользователя (просто для примера, без Firebase)
            saveUserDataLocally(email, username);

            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

            // Переход в MainActivity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

            // Можно передать данные пользователя в MainActivity
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_NAME", username);

            // Очищаем стек активностей и запускаем MainActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Закрываем RegisterActivity
        });
    }

    // Метод для сохранения данных пользователя локально (например, в SharedPreferences)
    private void saveUserDataLocally(String email, String username) {
        // Используем SharedPreferences для сохранения данных
        getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .edit()
                .putString("user_email", email)
                .putString("user_name", username)
                .putBoolean("is_logged_in", true)
                .apply();

        Log.d(TAG, "User data saved locally: " + email + ", " + username);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RegisterActivity destroyed");
    }
}