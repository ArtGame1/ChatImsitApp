package com.example.chatimsitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText emailEt;
    private EditText passwordEt;
    private Button loginBtn;
    private TextView goToRegisterTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "LoginActivity started");

        // Находим элементы
        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        goToRegisterTv = findViewById(R.id.go_to_register_activity_tv);

        // Проверяем, если пользователь уже вошел
        checkIfAlreadyLoggedIn();

        // Проверяем, пришел ли email из регистрации
        checkForRegisteredEmail();

        // Обработчик нажатия на кнопку "Авторизация"
        loginBtn.setOnClickListener(v -> {
            handleLogin();
        });

        // Обработчик текста "Зарегистрируйтесь"
        goToRegisterTv.setOnClickListener(v -> {
            Log.d(TAG, "Sign up text clicked");
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void checkIfAlreadyLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            String email = prefs.getString("current_user_email", "");
            Log.d(TAG, "User already logged in as: " + email + ", redirecting to MainActivity");
            goToMainActivity(email);
        }
    }

    private void checkForRegisteredEmail() {
        // Проверяем, пришел ли email из RegisterActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("REGISTERED_EMAIL")) {
            String registeredEmail = intent.getStringExtra("REGISTERED_EMAIL");
            emailEt.setText(registeredEmail);
            passwordEt.requestFocus(); // Переводим курсор на поле пароля
            Toast.makeText(this, "Теперь введите пароль для входа", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogin() {
        Log.d(TAG, "Login button clicked");

        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        // Проверка полей
        if (!validateInput(email, password)) {
            return;
        }

        // Проверяем учетные данные
        if (checkCredentials(email, password)) {

            // Сохраняем данные входа
            saveLoginData(email);

            // Переход в MainActivity
            goToMainActivity(email);

            // Успешный вход
            Log.d(TAG, "Login successful for: " + email);
            Toast.makeText(getApplicationContext(), "Авторизация успешна!", Toast.LENGTH_SHORT).show();

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Введите email");
            emailEt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEt.setError("Введите пароль");
            passwordEt.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordEt.setError("Пароль должен содержать не менее 6 символов");
            passwordEt.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Введите правильный email");
            emailEt.requestFocus();
            return false;
        }

        return true;
    }

    // Метод для проверки учетных данных
    private boolean checkCredentials(String email, String password) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // 1. Проверяем тестовые данные (для демонстрации)
        if (email.equals("user@chat.com") && password.equals("password123")) {
            // Если тестовый пользователь еще не сохранен, сохраняем его
            if (prefs.getString("user_email_" + email, null) == null) {
                prefs.edit()
                        .putString("user_email_" + email, email)
                        .putString("user_name_" + email, "Тестовый пользователь")
                        .putString("user_password_" + email, password)
                        .apply();
            }
            return true;
        }

        // 2. Проверяем данные из регистрации
        String savedPassword = prefs.getString("user_password_" + email, null);

        if (savedPassword != null && savedPassword.equals(password)) {
            return true;
        }

        return false;
    }

    // Метод для сохранения данных входа
    private void saveLoginData(String email) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("current_user_email", email);
        editor.putBoolean("is_logged_in", true);
        editor.apply();

        Log.d(TAG, "Login data saved: " + email);
    }

    // Метод для перехода в MainActivity
    private void goToMainActivity(String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER_EMAIL", email);

        // Получаем имя пользователя
        String username = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("user_name_" + email, "Пользователь");
        intent.putExtra("USER_NAME", username);

        // Очищаем стек и запускаем MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkForRegisteredEmail();
    }
}