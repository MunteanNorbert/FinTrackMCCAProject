package com.example.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    private String userID;
    private String userType;

    DatabaseReference mDatabase;
    private ArrayList<Users> userList;
    private ArrayList<String> employeeIDList;
    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        userID = getIntent().getStringExtra("userID");
        userType = getIntent().getStringExtra("userType");

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        employeeIDList = new ArrayList<String>();
        userList = new ArrayList<Users>();
        mRecyclerView = findViewById(R.id.recycler_view_users);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UsersAdapter(userList, employeeIDList, userType, userID, UsersListActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                employeeIDList.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren()) {
                    if (userSnapshot.exists()) {
                        String employeeID = userSnapshot.getKey();
                        String username = userSnapshot.child("username").getValue(String.class);
                        String firstname = userSnapshot.child("firstname").getValue(String.class);
                        String lastname = userSnapshot.child("lastname").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String category = userSnapshot.child("category").getValue(String.class);
                        userList.add(new Users(username, firstname, lastname, email, category));
                        employeeIDList.add(employeeID);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}