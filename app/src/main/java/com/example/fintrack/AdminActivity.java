package com.example.fintrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    private Button usersList;
    private Button myExpenses;
    private Button openExpenses;
    private Button statistics;
    private TextView adminWelcome;

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String userID;
    private String category;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        usersList = findViewById(R.id.usersList);
        myExpenses = findViewById(R.id.myExpenses);
        openExpenses = findViewById(R.id.openExpenses);
        statistics = findViewById(R.id.statistics);
        adminWelcome = findViewById(R.id.adminWelcome);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    firstname = snapshot.child("firstname").getValue(String.class);
                    lastname = snapshot.child("lastname").getValue(String.class);
                    username = snapshot.child("username").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                }

                adminWelcome.setText("Welcome\n" + firstname + " !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        usersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUsersList();
            }
        });

        myExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyExpenses();
            }
        });

        openExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOpenExpenses();
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatistics();
            }
        });
    }

    public void openUsersList(){
        Intent intent = new Intent(this, UsersListActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openMyExpenses(){
        Intent intent = new Intent(this, MyExpensesActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("expenseStatus", "myexpenses");
        startActivity(intent);
    }

    public void openOpenExpenses(){
        Intent intent = new Intent(this, OpenExpensesActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("expenseStatus", "requestedexpenses");
        startActivity(intent);
    }

    public void openStatistics(){
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

}