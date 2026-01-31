package com.example.chatimsitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Объявляем переменные по ID из вашего layout
    private EditText emailEt;
    private EditText passwordEt;
    private Button loginBtn;
    private TextView goToRegisterTv;

    // Тестовые данные для входа
    private static final String CORRECT_EMAIL = "user@chat.com";
    private static final String CORRECT_PASSWORD = "password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Используем ваш существующий layout

        Log.d(TAG, "LoginActivity started");

        // Находим элементы по ID из вашего layout
        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        goToRegisterTv = findViewById(R.id.go_to_register_activity_tv);

        // Проверяем, если пользователь уже "вошел"
        boolean isLoggedIn = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            Log.d(TAG, "User already logged in (locally), redirecting to MainActivity");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Секретная подсказка по долгому нажатию на заголовок (как в фитнес-приложении)
        TextView loginTitle = findViewById(R.id.login_title);
        if (loginTitle != null) {
            loginTitle.setOnLongClickListener(v -> {
                Toast.makeText(LoginActivity.this,
                        "Тестовые данные:\nEmail: user@chat.com\nПароль: password123",
                        Toast.LENGTH_LONG).show();
                return true;
            });
        }

        // Обработчик нажатия на кнопку "Авторизация"
        loginBtn.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");

            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            // Проверка, введен ли email
            if (TextUtils.isEmpty(email)) {
                emailEt.setError("Введите email");
                emailEt.requestFocus();
                return;
            }

            // Проверка пароля
            if (TextUtils.isEmpty(password)) {
                passwordEt.setError("Введите пароль");
                passwordEt.requestFocus();
                return;
            }

            // Дополнительные проверки пароля (как в фитнес-приложении)
            if (password.length() < 6) {
                passwordEt.setError("Пароль должен содержать не менее 6 символов");
                passwordEt.requestFocus();
                return;
            }

            if (!containsDigit(password)) {
                passwordEt.setError("Пароль должен содержать хотя бы одну цифру");
                passwordEt.requestFocus();
                return;
            }

            // Проверка на правильность введенных данных
            if (email.equals(CORRECT_EMAIL) && password.equals(CORRECT_PASSWORD)) {
                // Успешный вход
                Log.d(TAG, "Login successful");
                Toast.makeText(getApplicationContext(), "Авторизация успешна!", Toast.LENGTH_SHORT).show();

                // Сохраняем данные локально (как в фитнес-приложении логика)
                saveLoginData(email, password);

                // Переход в MainActivity
                goToMainActivity(email);

            } else {
                // Неверные данные
                Toast.makeText(LoginActivity.this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик текста "Зарегистрируйтесь" (как в фитнес-приложении "Регистрация")
        goToRegisterTv.setOnClickListener(v -> {
            Log.d(TAG, "Sign up text clicked");
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            // Не закрываем LoginActivity, чтобы можно было вернуться назад (как в фитнес-приложении)
        });
    }

    // Метод для сохранения данных входа (похоже на фитнес-приложение)
    private void saveLoginData(String email, String password) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("user_email", email);
        editor.putBoolean("is_logged_in", true);
        editor.apply();

        Log.d(TAG, "Login data saved: " + email);
    }

    // Метод для перехода в MainActivity
    private void goToMainActivity(String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER_EMAIL", email);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // Метод для проверки наличия хотя бы одной цифры в пароле (ТОЧНО как в фитнес-приложении)
    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    // Метод для проверки формата email (дополнительно, как в фитнес-приложении)
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}