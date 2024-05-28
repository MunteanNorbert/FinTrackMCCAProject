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

public class OpenExpensesActivity extends AppCompatActivity {

    private String userID;
    private String userFirstname;
    private String userLastname;
    private String userType;
    private String userType1;
    private String employeeID;
    private String expenseStatus;

    private Button addExpenseButton;
    private TextView activityTitle;

    DatabaseReference mDatabase;
    private ArrayList<Expense> expenseList;
    private ArrayList<String> expenseTitles;
    private ArrayList<String> expenseItemTitles;
    private RecyclerView mRecyclerView;
    private MyExpensesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_expenses);

        userID = getIntent().getStringExtra("userID");
        employeeID = getIntent().getStringExtra("employeeID");
        userType = getIntent().getStringExtra("userType");
        userType1 = getIntent().getStringExtra("userType1");
        expenseStatus = getIntent().getStringExtra("expenseStatus");
        userFirstname = getIntent().getStringExtra("userFirstname");
        userLastname = getIntent().getStringExtra("userLastname");

        mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(userID);

        expenseList = new ArrayList<Expense>();
        expenseTitles = new ArrayList<String>();
        expenseItemTitles = new ArrayList<String>();
        mRecyclerView = findViewById(R.id.recycler_view_expenses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyExpensesAdapter(expenseList, expenseTitles, expenseItemTitles, expenseStatus, employeeID ,userType, userType1 , userID, OpenExpensesActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        addExpenseButton = findViewById(R.id.add_new_expense);
        activityTitle = findViewById(R.id.textView_my_expenses);

        if(expenseStatus.equals("myexpenses")){
           activityTitle.setText("My Expenses");
        }
        if((expenseStatus.equals("requestedexpenses") || expenseStatus.equals("openexpenses")) && userType1 == null){
            activityTitle.setText("My Open Expenses");
            addExpenseButton.setVisibility(View.VISIBLE);
            addExpenseButton.setEnabled(true);
        }

        if((expenseStatus.equals("requestedexpenses") || expenseStatus.equals("openexpenses")) && userType1 != null){
            activityTitle.setText(userFirstname + " " + userLastname);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseTitles.clear();
                expenseItemTitles.clear();
                expenseList.clear();
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    expenseTitles.add(expenseSnapshot.getKey());
                    for (DataSnapshot expenseItemSnapshot : expenseSnapshot.getChildren()) {
                        expenseItemTitles.add(expenseItemSnapshot.getKey());
                        expenseList.add(new Expense(expenseItemSnapshot.child("expenseDate").getValue(String.class),
                                expenseItemSnapshot.child("expenseMoney").getValue(String.class),
                                expenseItemSnapshot.child("expenseImage").getValue(String.class)));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenExpensesActivity.this, NewExpenseActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("userType",userType);
                intent.putExtra("expenseStatus", expenseStatus);
                startActivity(intent);
            }
        });
    }
}