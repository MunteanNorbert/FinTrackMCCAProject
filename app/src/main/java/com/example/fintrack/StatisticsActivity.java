package com.example.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private String userID;
    private String employeeID;
    private String expenseStatus;
    private String userType;
    private String userType1;

    private ArrayList<String> singleExpenseMonthYear;
    private ArrayList<String> expenseMonthYear;
    private ArrayList<String> expensePrice;
    private ArrayList<String> expenseGroup;
    private RecyclerView mRecyclerView;
    private StatisticsAdapter mAdapter;

    private TextView totalSum;
    private float expenseSum = 0;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        userID = getIntent().getStringExtra("userID");
        employeeID = getIntent().getStringExtra("employeeID");
        expenseStatus = getIntent().getStringExtra("expenseStatus");
        userType = getIntent().getStringExtra("userType");
        userType1 = getIntent().getStringExtra("userType1");

        singleExpenseMonthYear = new ArrayList<String>();
        expenseMonthYear = new ArrayList<String>();
        expensePrice = new ArrayList<String>();
        expenseGroup = new ArrayList<String>();
        mRecyclerView = findViewById(R.id.recycler_view_statistics);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StatisticsAdapter(singleExpenseMonthYear, expenseMonthYear, expensePrice, expenseGroup, expenseStatus, userType, userType1, userID, StatisticsActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        totalSum = findViewById(R.id.textView_price_sum);

        if(employeeID == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(userID);
        }
        if(employeeID != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(employeeID);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                singleExpenseMonthYear.clear();
                expenseMonthYear.clear();
                expensePrice.clear();
                expenseGroup.clear();
                expenseSum = 0;

                for (DataSnapshot expenseTitleSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot expenseItemTitleSnapshot : expenseTitleSnapshot.getChildren()) {
                        String money = expenseItemTitleSnapshot.child("expenseMoney").getValue(String.class);
                        String group = expenseItemTitleSnapshot.child("expenseGroup").getValue(String.class);
                        String monthYear = expenseItemTitleSnapshot.child("expenseMonthYear").getValue(String.class);

                        if (monthYear != null && !singleExpenseMonthYear.contains(monthYear)) {
                            singleExpenseMonthYear.add(monthYear);
                        }
                        if (monthYear != null) {
                            expenseMonthYear.add(monthYear);
                        }
                        if (money != null) {
                            expensePrice.add(money);
                            expenseSum = expenseSum + Float.parseFloat(money);
                        }
                        if (group != null) {
                            expenseGroup.add(group);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                totalSum.setText("Price: " + Float.toString(expenseSum) + "$");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}