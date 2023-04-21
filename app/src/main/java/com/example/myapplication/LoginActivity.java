package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.example.loginapp.EXTRA_USERNAME";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Open the database connection
        db = openOrCreateDatabase("users.db", MODE_PRIVATE, null);

        // Create the "users" table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Check if the username and password are valid
                if (isValid(username, password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_USERNAME, username);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Check if the username and password are valid
                if (isValidUsername(username)) {
                    Toast.makeText(LoginActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the new user to the database
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("password", password);
                    db.insert("users", null, values);

                    Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValid(String username, String password) {
        // Query the database to check if the username and password are valid
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});

        // Check if the query returned any rows
        boolean isValid = cursor.getCount() > 0;

        // Close the cursor
        cursor.close();

        return isValid;
    }

    private boolean isValidUsername(String username) {
        // Query the database to check if the username already exists
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});

        // Check if the query returned any rows
        boolean isValid = cursor.getCount() > 0;

        // Close the cursor
        cursor.close();

        return isValid;
    }

    @Override
    protected void onDestroy() {
        // Close the database connection
        db.close();

        super.onDestroy();
    }
}
