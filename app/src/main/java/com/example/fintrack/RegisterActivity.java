package com.example.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText regUsername;
    private EditText regFirstName;
    private EditText regLastName;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private TextView textViewLogin;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = findViewById(R.id.registerUsername);
        regFirstName = findViewById(R.id.registerFirstName);
        regLastName = findViewById(R.id.registerLastName);
        regEmail = findViewById(R.id.registerEmail);
        regPassword = findViewById(R.id.registerPassword);
        regConfirmPassword = findViewById(R.id.registerConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = regUsername.getText().toString().trim();
                String firstname = regFirstName.getText().toString().trim();
                String lastname = regLastName.getText().toString().trim();
                String email = regEmail.getText().toString().trim();

                if(username.isEmpty()) {
                    regUsername.setError("An username is required");
                    regUsername.requestFocus();
                }

                if(email.isEmpty()) {
                    regEmail.setError("An email is required");
                    regEmail.requestFocus();
                }

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean userExists = false;
                        boolean emailExists = false;

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String email1 = userSnapshot.child("email").getValue(String.class);
                            String username1 = userSnapshot.child("username").getValue(String.class);

                            if (username1.equals(username)) {
                                userExists = true;
                                break;
                            }
                            if (email1.equals(email)) {
                                emailExists = true;
                                break;
                            }
                        }

                        if (userExists) {
                            regUsername.setError("Username already exists");
                            regUsername.requestFocus();
                        } else if (emailExists) {
                            regEmail.setError("Email already exists");
                            regEmail.requestFocus();
                        } else {
                            String password = regPassword.getText().toString().trim();
                            String confirmPassword = regConfirmPassword.getText().toString().trim();

                            if(password.isEmpty()){
                                regPassword.setError("Insert a password");
                                regPassword.requestFocus();
                            }

                            if(confirmPassword.isEmpty()){
                                regConfirmPassword.setError("Insert the password again");
                                regConfirmPassword.requestFocus();
                            }

                            if(password.length() < 6){
                                regPassword.setError("The password must be at least of length 6");
                                regPassword.requestFocus();
                            }

                            if(confirmPassword.length() < 6){
                                regConfirmPassword.setError("The password must be at least of length 6");
                                regConfirmPassword.requestFocus();
                            }

                            if (!password.equals(confirmPassword)) {
                                regConfirmPassword.setError("The passwords do not match");
                                regConfirmPassword.requestFocus();
                            } else {
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    String userID = mAuth.getCurrentUser().getUid();
                                                    String category = email.split("@")[1].split("\\.")[0];
                                                    Users newUsers = new Users(username, firstname, lastname, email, category);
                                                    mDatabase.child(userID).setValue(newUsers);
                                                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RegisterActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}