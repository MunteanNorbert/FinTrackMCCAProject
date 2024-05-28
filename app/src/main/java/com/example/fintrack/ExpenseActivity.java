package com.example.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    private String userID;
    private String employeeID;
    private String expenseTitle1;
    private String expenseStatus;
    private String userType;
    private String userType1;
    private String expenseItemTitle;
    private String expenseDate;
    private String expenseMoney;
    private String expenseImage;
    private float finalSum = 0;

    private Button addExpenseItemButton;
    private Button requestExpenseButton;
    private Button acceptRequestedButton;
    private Button declineRequestedButton;
    private TextView expenseTitle;
    private TextView expenseSum;

    DatabaseReference mDatabase;
    DatabaseReference nDatabase;
    DatabaseReference oDatabase;
    DatabaseReference pDatabase;
    DatabaseReference qDatabase;

    private ArrayList<String> newExpenseList;
    private RecyclerView mRecyclerView;
    private ExpenseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        userID = getIntent().getStringExtra("userID");
        employeeID = getIntent().getStringExtra("employeeID");
        expenseStatus = getIntent().getStringExtra("expenseStatus");
        expenseTitle1 = getIntent().getStringExtra("expenseTitle");
        expenseItemTitle = getIntent().getStringExtra("expenseItemTitle");
        expenseDate = getIntent().getStringExtra("expenseDate");
        expenseMoney = getIntent().getStringExtra("expenseMoney");
        expenseImage = getIntent().getStringExtra("expenseImage");
        userType = getIntent().getStringExtra("userType");
        userType1 = getIntent().getStringExtra("userType1");

        newExpenseList = new ArrayList<String>();
        mRecyclerView = findViewById(R.id.recycler_view_expense_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExpenseAdapter(newExpenseList, expenseTitle1, expenseStatus, employeeID, userType, userType1, userID, ExpenseActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        addExpenseItemButton = findViewById(R.id.add_new_expense_item);
        requestExpenseButton = findViewById(R.id.request_expense);
        acceptRequestedButton = findViewById(R.id.acceptRequestedItem);
        declineRequestedButton = findViewById(R.id.declineRequestedItem);
        expenseTitle = findViewById(R.id.textView_new_expense);
        expenseSum = findViewById(R.id.textView_price_sum);
        expenseTitle.setText(expenseTitle1);
        if(employeeID == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(userID).child(expenseTitle1);
            nDatabase = FirebaseDatabase.getInstance().getReference("openexpenses").child(userID).child(expenseTitle1);
        }

        if(employeeID != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(employeeID).child(expenseTitle1);
            nDatabase = FirebaseDatabase.getInstance().getReference("openexpenses").child(employeeID).child(expenseTitle1);
            oDatabase = FirebaseDatabase.getInstance().getReference("openexpenses").child(employeeID).child(expenseTitle1);
            pDatabase = FirebaseDatabase.getInstance().getReference("myexpenses").child(employeeID).child(expenseTitle1);
            qDatabase = FirebaseDatabase.getInstance().getReference("requestedexpenses").child(employeeID).child(expenseTitle1);
        }

        if(expenseStatus.equals("myexpenses")){
            addExpenseItemButton.setEnabled(false);
            requestExpenseButton.setEnabled(false);
            acceptRequestedButton.setEnabled(false);
            declineRequestedButton.setEnabled(false);
        }

        if(expenseStatus.equals("openexpenses") && (userType.equals("employee") || userType == null)){
            addExpenseItemButton.setVisibility(View.VISIBLE);
            requestExpenseButton.setVisibility(View.VISIBLE);
            addExpenseItemButton.setEnabled(true);
            requestExpenseButton.setEnabled(true);
            acceptRequestedButton.setEnabled(false);
            declineRequestedButton.setEnabled(false);
        }

        if(expenseStatus.equals("openexpenses") && userType1 != null){
            acceptRequestedButton.setVisibility(View.VISIBLE);
            declineRequestedButton.setVisibility(View.VISIBLE);
            addExpenseItemButton.setEnabled(false);
            requestExpenseButton.setEnabled(false);
            acceptRequestedButton.setEnabled(true);
            declineRequestedButton.setEnabled(true);
        }

        if(expenseStatus.equals("requestedexpenses") && userType1 == null && userType != null ){
            addExpenseItemButton.setVisibility(View.VISIBLE);
            requestExpenseButton.setVisibility(View.VISIBLE);
            addExpenseItemButton.setEnabled(true);
            requestExpenseButton.setEnabled(true);
            acceptRequestedButton.setEnabled(false);
            declineRequestedButton.setEnabled(false);
        }

        if(expenseStatus.equals("requestedexpenses") && userType1 == null && userType == null ){
            addExpenseItemButton.setVisibility(View.VISIBLE);
            requestExpenseButton.setVisibility(View.VISIBLE);
            addExpenseItemButton.setEnabled(true);
            requestExpenseButton.setEnabled(true);
            acceptRequestedButton.setEnabled(false);
            declineRequestedButton.setEnabled(false);
        }

        if(expenseStatus.equals("requestedexpenses") && userType1 != null){
            addExpenseItemButton.setEnabled(false);
            requestExpenseButton.setEnabled(false);
            acceptRequestedButton.setEnabled(false);
            declineRequestedButton.setEnabled(false);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newExpenseList.clear();
                for(DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    if (expenseSnapshot.exists()) {
                        String title = expenseSnapshot.getKey();
                        if(employeeID == null && expenseStatus.equals("myexpenses")) {
                            finalSum = finalSum + Float.parseFloat(expenseSnapshot.child("expenseMoney").getValue(String.class));
                        }
                        newExpenseList.add(title);
                        nDatabase.child(title);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if(employeeID == null && expenseStatus.equals("myexpenses")) {
                    expenseSum.setVisibility(View.VISIBLE);;
                    expenseSum.setText("Price: " + Float.toString(finalSum) + "$");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        addExpenseItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseActivity.this, NewExpenseActivity2.class);
                intent.putExtra("userID",userID);
                intent.putExtra("expenseTitle",expenseTitle1);
                intent.putExtra("expenseStatus", expenseStatus);
                startActivity(intent);
            }
        });

        requestExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                            if (expenseSnapshot.exists()) {
                                nDatabase.child(expenseSnapshot.getKey()).child("expenseDate").setValue(expenseSnapshot.child("expenseDate").getValue(String.class));
                                nDatabase.child(expenseSnapshot.getKey()).child("expenseMoney").setValue(expenseSnapshot.child("expenseMoney").getValue(String.class));
                                nDatabase.child(expenseSnapshot.getKey()).child("expenseImage").setValue(expenseSnapshot.child("expenseImage").getValue(String.class));
                                nDatabase.child(expenseSnapshot.getKey()).child("expenseMonthYear").setValue(expenseSnapshot.child("expenseMonthYear").getValue(String.class));
                                nDatabase.child(expenseSnapshot.getKey()).child("expenseGroup").setValue(expenseSnapshot.child("expenseGroup").getValue(String.class));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                mDatabase.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ExpenseActivity.this, "Expense was requested", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExpenseActivity.this, "Failed to request this expense", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(ExpenseActivity.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });

        acceptRequestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                            if (expenseSnapshot.exists()) {
                                pDatabase.child(expenseSnapshot.getKey()).child("expenseDate").setValue(expenseSnapshot.child("expenseDate").getValue(String.class));
                                pDatabase.child(expenseSnapshot.getKey()).child("expenseMoney").setValue(expenseSnapshot.child("expenseMoney").getValue(String.class));
                                pDatabase.child(expenseSnapshot.getKey()).child("expenseImage").setValue(expenseSnapshot.child("expenseImage").getValue(String.class));
                                pDatabase.child(expenseSnapshot.getKey()).child("expenseMonthYear").setValue(expenseSnapshot.child("expenseMonthYear").getValue(String.class));
                                pDatabase.child(expenseSnapshot.getKey()).child("expenseGroup").setValue(expenseSnapshot.child("expenseGroup").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                oDatabase.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ExpenseActivity.this, "Expense was accepted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExpenseActivity.this, "Failed to accept this expense", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(ExpenseActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        declineRequestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                            if (expenseSnapshot.exists()) {
                                qDatabase.child(expenseSnapshot.getKey()).child("expenseDate").setValue(expenseSnapshot.child("expenseDate").getValue(String.class));
                                qDatabase.child(expenseSnapshot.getKey()).child("expenseMoney").setValue(expenseSnapshot.child("expenseMoney").getValue(String.class));
                                qDatabase.child(expenseSnapshot.getKey()).child("expenseImage").setValue(expenseSnapshot.child("expenseImage").getValue(String.class));
                                qDatabase.child(expenseSnapshot.getKey()).child("expenseMonthYear").setValue(expenseSnapshot.child("expenseMonthYear").getValue(String.class));
                                qDatabase.child(expenseSnapshot.getKey()).child("expenseGroup").setValue(expenseSnapshot.child("expenseGroup").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                oDatabase.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ExpenseActivity.this, "Expense was declined", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExpenseActivity.this, "Failed to decline this expense", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(ExpenseActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}