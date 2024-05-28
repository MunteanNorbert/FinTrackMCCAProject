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

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpensesViewHolder> {
    final ArrayList<String> newExpenseList;
    public final Context context;
    private String expenseTitle;
    private String expenseStatus;
    private String employeeID;
    private String userID;
    private String userType;
    private String userType1;
    private DatabaseReference userRef;

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        public Button newExpenseButton;

        public ExpensesViewHolder(View itemView) {
            super(itemView);
            newExpenseButton = itemView.findViewById(R.id.tv_new_expense_title);
        }
    }

    public ExpenseAdapter(ArrayList<String> newExpenseList, String expenseTitle, String expenseStatus, String employeeID, String userType, String userType1, String userID, Context context) {
        this.newExpenseList = newExpenseList;
        this.expenseTitle = expenseTitle;
        this.expenseStatus = expenseStatus;
        this.employeeID = employeeID;
        this.userType = userType;
        this.userType1 = userType1;
        this.userID = userID;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newexpensetitle_item, parent, false);
        ExpensesViewHolder viewHolder = new ExpensesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        String currentTitle = newExpenseList.get(position);

        holder.newExpenseButton.setText(currentTitle);
        holder.newExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpenseItemInfoActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("employeeID",employeeID);
                intent.putExtra("userType", userType);
                intent.putExtra("userType1", userType1);
                intent.putExtra("expenseTitle", expenseTitle);
                intent.putExtra("expenseItemTitle", currentTitle);
                intent.putExtra("expenseStatus", expenseStatus);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newExpenseList.size();
    }
}
