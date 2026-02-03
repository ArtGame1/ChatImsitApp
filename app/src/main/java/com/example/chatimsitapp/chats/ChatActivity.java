package com.example.chatimsitapp.chats;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.chatimsitapp.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private ListView listOfMessages;
    private EditText textField;
    private ImageButton attachBtn, emojiBtn, submitBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference messagesRef;
    private StorageReference storageRef;
    private FirebaseListAdapter<Message> adapter;

    private static final int REQUEST_PICK_IMAGE = 1001;
    private static final int REQUEST_PICK_FILE = 1002;
    private static final int REQUEST_PERMISSIONS = 1003;

    private String currentUserName = "Пользователь";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Инициализация Firebase
        mAuth = FirebaseAuth.getInstance();
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        storageRef = FirebaseStorage.getInstance().getReference("chat_attachments");

        // Получаем имя пользователя
        if (mAuth.getCurrentUser() != null) {
            String name = mAuth.getCurrentUser().getDisplayName();
            String email = mAuth.getCurrentUser().getEmail();
            currentUserName = (name != null && !name.isEmpty()) ? name : email;
        }

        // Находим элементы
        textField = findViewById(R.id.textField);
        attachBtn = findViewById(R.id.attachBtn);
        emojiBtn = findViewById(R.id.emojiBtn);
        submitBtn = findViewById(R.id.submitBtn);
        listOfMessages = findViewById(R.id.listOfMessages);

        // Проверяем, что элементы найдены
        if (textField == null) Toast.makeText(this, "textField not found", Toast.LENGTH_SHORT).show();
        if (attachBtn == null) Toast.makeText(this, "attachBtn not found", Toast.LENGTH_SHORT).show();
        if (emojiBtn == null) Toast.makeText(this, "emojiBtn not found", Toast.LENGTH_SHORT).show();
        if (submitBtn == null) Toast.makeText(this, "submitBtn not found", Toast.LENGTH_SHORT).show();

        // Простой обработчик эмодзи (показывает диалог с эмодзи)
        emojiBtn.setOnClickListener(v -> showSimpleEmojiPicker());

        // Обработчик вложений
        attachBtn.setOnClickListener(v -> {
            // Анимация
            v.animate()
                    .scaleX(0.9f).scaleY(0.9f).setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f).scaleY(1f).setDuration(100).start())
                    .start();

            showAttachmentOptions();
        });

        // Обработчик отправки
        submitBtn.setOnClickListener(v -> {
            // Анимация
            v.animate()
                    .scaleX(0.9f).scaleY(0.9f).setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f).scaleY(1f).setDuration(100).start())
                    .start();

            sendMessage();
        });

        // Загрузка сообщений
        displayAllMessages();
    }

    // Простой выбор эмодзи (диалоговое окно)
    private void showSimpleEmojiPicker() {
        String[] emojis = {"😀", "😂", "🥰", "😎", "😡", "😭", "👍", "👎",
                "❤️", "🔥", "⭐", "🎉", "🙏", "💯", "📷", "📁"};

        new AlertDialog.Builder(this)
                .setTitle("Выберите эмодзи")
                .setItems(emojis, (dialog, which) -> {
                    String currentText = textField.getText().toString();
                    textField.setText(currentText + emojis[which]);
                    textField.setSelection(textField.getText().length());
                })
                .show();
    }

    // Меню вложений
    private void showAttachmentOptions() {
        String[] options = {"📷 Фото", "📎 Файл", "❌ Отмена"};

        new AlertDialog.Builder(this)
                .setTitle("Прикрепить файл")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: pickImage(); break;
                        case 1: pickFile(); break;
                    }
                })
                .show();
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private void pickImage() {
        if (!checkPermissions()) return;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void pickFile() {
        if (!checkPermissions()) return;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                uploadFile(fileUri);
            }
        }
    }

    private void uploadFile(Uri fileUri) {
        String fileName = "file_" + System.currentTimeMillis();
        StorageReference fileRef = storageRef.child(fileName);

        Toast.makeText(this, "Загрузка файла...", Toast.LENGTH_SHORT).show();

        UploadTask uploadTask = fileRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String messageText = "📎 Прикрепленный файл";
                Message message = new Message(currentUserName, messageText);
                message.setFileUrl(uri.toString());

                messagesRef.push().setValue(message)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Файл отправлен", Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void sendMessage() {
        String messageText = textField.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Введите сообщение", Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = new Message(currentUserName, messageText);
        messagesRef.push().setValue(message)
                .addOnSuccessListener(aVoid -> {
                    textField.setText("");
                    Toast.makeText(this, "Отправлено", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayAllMessages() {
        Query query = messagesRef.orderByChild("timestamp");

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.item_message)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                android.widget.TextView messageText = v.findViewById(R.id.message_text);
                android.widget.TextView messageUser = v.findViewById(R.id.message_user);
                android.widget.TextView messageTime = v.findViewById(R.id.message_time);

                messageUser.setText(model.getUserName());
                messageText.setText(model.getTextMessage());

                // Показываем иконку если есть файл
                if (model.getFileUrl() != null) {
                    messageText.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_attach_file, 0);
                    messageText.setCompoundDrawablePadding(8);
                } else {
                    messageText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                if (model.getMessageTime() != null) {
                    messageTime.setText(android.text.format.DateFormat.format(
                            "HH:mm", model.getMessageTime()));
                }
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    // Простой класс Message внутри файла (если отдельного нет)
    public static class Message {
        private String userName;
        private String textMessage;
        private long timestamp;
        private String fileUrl;

        public Message() {}

        public Message(String userName, String textMessage) {
            this.userName = userName;
            this.textMessage = textMessage;
            this.timestamp = new Date().getTime();
        }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getTextMessage() { return textMessage; }
        public void setTextMessage(String textMessage) { this.textMessage = textMessage; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

        public Date getMessageTime() { return new Date(timestamp); }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    }
}