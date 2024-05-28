package com.example.fintrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UsersExpensesActivity extends AppCompatActivity {
    
    private String userID;
    private String userFirstname;
    private String userLastname;
    private String userName;
    private String userType;
    private String userEmail;
    private String employeeID;
    private String userType1;

    private TextView employeeName;
    private Button employeeAcceptedExpenses;
    private Button employeeRequestedExpenses;
    private Button employeeInProgressExpenses;
    private Button employeeStatistics;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_expenses);

        userID = getIntent().getStringExtra("userID");
        userFirstname = getIntent().getStringExtra("userFirstname");
        userLastname = getIntent().getStringExtra("userLastname");
        userName = getIntent().getStringExtra("userName");
        userType = getIntent().getStringExtra("userType");
        userEmail = getIntent().getStringExtra("userEmail");
        employeeID = getIntent().getStringExtra("employeeID");
        userType1 = getIntent().getStringExtra("userType1");

        employeeName = findViewById(R.id.employeeName);
        employeeAcceptedExpenses = findViewById(R.id.acceptedExpenses);
        employeeRequestedExpenses = findViewById(R.id.openExpenses);
        employeeInProgressExpenses = findViewById(R.id.inProgressExpenses);
        employeeStatistics = findViewById(R.id.employeeStatistics);

        employeeName.setText(userFirstname + " " + userLastname);

        employeeAcceptedExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersExpensesActivity.this, MyExpensesActivity.class);
                intent.putExtra("userFirstname", userFirstname);
                intent.putExtra("userLastname", userLastname);
                intent.putExtra("userName", userName);
                intent.putExtra("userID",userID);
                intent.putExtra("userType", userType);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("expenseStatus", "myexpenses");
                intent.putExtra("employeeID", employeeID);
                intent.putExtra("userType1", userType1);
                startActivity(intent);
            }
        });

        employeeRequestedExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersExpensesActivity.this, MyExpensesActivity.class);
                intent.putExtra("userFirstname", userFirstname);
                intent.putExtra("userLastname", userLastname);
                intent.putExtra("userName", userName);
                intent.putExtra("userID",userID);
                intent.putExtra("userType", userType);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("expenseStatus", "openexpenses");
                intent.putExtra("employeeID", employeeID);
                intent.putExtra("userType1", userType1);
                startActivity(intent);

            }
        });

        employeeInProgressExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersExpensesActivity.this, OpenExpensesActivity.class);
                intent.putExtra("userFirstname", userFirstname);
                intent.putExtra("userLastname", userLastname);
                intent.putExtra("userName", userName);
                intent.putExtra("userID",userID);
                intent.putExtra("userType", userType);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("expenseStatus", "requestedexpenses");
                intent.putExtra("employeeID", employeeID);
                intent.putExtra("userType1", userType1);
                startActivity(intent);
            }
        });

        employeeStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersExpensesActivity.this, StatisticsActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("userFirstname", userFirstname);
                intent.putExtra("userLastname", userLastname);
                intent.putExtra("userName", userName);
                intent.putExtra("userID",userID);
                intent.putExtra("userType", userType);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("expenseStatus", "myexpenses");
                intent.putExtra("employeeID", employeeID);
                intent.putExtra("userType1", userType1);
                startActivity(intent);
            }
        });
    }
}