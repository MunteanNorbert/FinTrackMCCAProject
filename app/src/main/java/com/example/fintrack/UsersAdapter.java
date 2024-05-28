package com.example.fintrack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    final ArrayList<String> employeeIDList;
    final ArrayList<Users> userList;
    public final Context context;
    private String userID;
    private String userType1;
    private DatabaseReference userRef;

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        public Button userButton;

        public UsersViewHolder(View itemView) {
            super(itemView);
            userButton = itemView.findViewById(R.id.tv_user);
        }
    }

    public UsersAdapter(ArrayList<Users> userList, ArrayList<String> employeeIDList, String userType1, String userID, Context context) {
        this.userList = userList;
        this.employeeIDList = employeeIDList;
        this.userType1 = userType1;
        this.userID = userID;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        UsersViewHolder viewHolder = new UsersViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        Users currentItem = userList.get(position);
        String employeeIDItem = employeeIDList.get(position);

        holder.userButton.setText(currentItem.getFirstname() + " " + currentItem.getLastname());
        holder.userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, UsersExpensesActivity.class);
                    intent.putExtra("userFirstname", currentItem.getFirstname());
                    intent.putExtra("userLastname", currentItem.getLastname());
                    intent.putExtra("userName", currentItem.getUsername());
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", currentItem.getCategory());
                    intent.putExtra("userEmail", currentItem.getEmail());
                    intent.putExtra("employeeID", employeeIDItem);
                    intent.putExtra("userType1", userType1);
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
