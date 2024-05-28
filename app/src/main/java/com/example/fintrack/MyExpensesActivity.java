package com.example.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyExpensesActivity extends AppCompatActivity {

    private String userID;
    private String userFirstName;
    private String userLastName;
    private String expenseStatus;
    private String userType;
    private String employeeID;
    private String userType1;

    private Button addExpenseButton;
    private Button acceptEmployeeRequest;
    private Button declineEmployeeRequest;
    private TextView activityTitle;

    DatabaseReference mDatabase;
    DatabaseReference nDatabase;
    DatabaseReference oDatabase;
    private ArrayList<Expense> expenseList;
    private ArrayList<String> expenseTitles;
    private ArrayList<String> expenseItemsTitles;
    private String expense;
    private RecyclerView mRecyclerView;
    private MyExpensesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expenses);

        userID = getIntent().getStringExtra("userID");
        userFirstName = getIntent().getStringExtra("userFirstname");
        userLastName = getIntent().getStringExtra("userLastname");
        expenseStatus = getIntent().getStringExtra("expenseStatus");
        userType = getIntent().getStringExtra("userType");
        employeeID = getIntent().getStringExtra("employeeID");
        userType1 = getIntent().getStringExtra("userType1");

        expenseList = new ArrayList<Expense>();
        expenseTitles = new ArrayList<String>();
        expenseItemsTitles = new ArrayList<String>();
        mRecyclerView = findViewById(R.id.recycler_view_expenses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyExpensesAdapter(expenseList, expenseTitles, expenseItemsTitles, expenseStatus, employeeID, userType, userType1, userID, MyExpensesActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        addExpenseButton = findViewById(R.id.add_new_expense);
        //acceptEmployeeRequest = findViewById(R.id.acceptEmployeeRequest);
        //declineEmployeeRequest = findViewById(R.id.declineEmployeeRequest);
        activityTitle = findViewById(R.id.textView_my_expenses);


        if(employeeID == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(userID);
            nDatabase = FirebaseDatabase.getInstance().getReference("openexpenses").child(userID);
            oDatabase = FirebaseDatabase.getInstance().getReference("myexpenses").child(userID);
        }

        if(employeeID != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(employeeID);
            nDatabase = FirebaseDatabase.getInstance().getReference("openexpenses").child(employeeID);
            oDatabase = FirebaseDatabase.getInstance().getReference("myexpenses").child(employeeID);
            activityTitle.setText(userFirstName + " " + userLastName);
        }

        if(expenseStatus.equals("requestedexpenses")){
            addExpenseButton.setVisibility(View.VISIBLE);
            addExpenseButton.setEnabled(true);
        }

        if(expenseStatus.equals("openexpenses") || expenseStatus.equals("myexpenses")){
            //acceptEmployeeRequest.setEnabled(false);
            //declineEmployeeRequest.setEnabled(false);
            addExpenseButton.setEnabled(false);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseTitles.clear();
                expenseItemsTitles.clear();
                expenseList.clear();
                for(DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    if (expenseSnapshot.exists()) {
                        expense = expenseSnapshot.getKey();
                        String title = expenseSnapshot.getKey();
                        String date = expenseSnapshot.child("expenseDate").getValue(String.class);
                        String money = expenseSnapshot.child("expenseMoney").getValue(String.class);
                        String image = expenseSnapshot.child("expenseImage").getValue(String.class);
                        expenseList.add(new Expense(date, money, image));
                        expenseTitles.add(title);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyExpensesActivity.this, ExpenseActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("userType", userType);
                intent.putExtra("expenseStatus", expenseStatus);
                startActivity(intent);
            }
        });
    }
}