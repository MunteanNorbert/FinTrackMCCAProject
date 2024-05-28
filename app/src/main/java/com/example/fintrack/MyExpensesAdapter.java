package com.example.fintrack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MyExpensesAdapter extends RecyclerView.Adapter<MyExpensesAdapter.MyExpensesViewHolder> {
    final ArrayList<Expense> expenseList;
    final ArrayList<String> expenseTitles;
    final ArrayList<String> expenseItemsTitles;
    public final Context context;
    private String expense;
    private String expenseStatus;
    private String userType;
    private String userType1;
    private String userID;
    private String employeeID;
    private DatabaseReference userRef;

    public static class MyExpensesViewHolder extends RecyclerView.ViewHolder {
        public Button myExpenseButton;


        public MyExpensesViewHolder(View itemView) {
            super(itemView);
            myExpenseButton = itemView.findViewById(R.id.tv_my_expenses);

        }
    }

    public MyExpensesAdapter(ArrayList<Expense> expenseList, ArrayList<String> expenseTitles, ArrayList<String> expenseItemsTitles, String expenseStatus, String employeeID, String userType, String userType1, String userID, Context context) {
        this.expenseList = expenseList;
        this.expenseTitles = expenseTitles;
        this.expenseItemsTitles = expenseItemsTitles;
        this.expenseStatus = expenseStatus;
        this.employeeID = employeeID;
        this.userType = userType;
        this.userType1 = userType1;
        this.userID = userID;
        this.context = context;
    }

    @NonNull
    @Override
    public MyExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myexpenses_item, parent, false);
        MyExpensesViewHolder viewHolder = new MyExpensesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyExpensesViewHolder holder, int position) {
        //Expense currentItem = expenseList.get(position);
        String currentTitle = expenseTitles.get(position);
        //String currentItemTitle = expenseItemsTitles.get(position);

        //holder.expenseTitle.setText(currentTitle);
        holder.myExpenseButton.setText(currentTitle);
        holder.myExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpenseActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("expenseTitle",currentTitle);
                intent.putExtra("expenseStatus", expenseStatus);
                intent.putExtra("employeeID", employeeID);
                intent.putExtra("userType", userType);
                intent.putExtra("userType1", userType1);
                //intent.putExtra("expenseTitle", currentTitle);
                //intent.putExtra("expenseItemTitle", currentItemTitle);
                //intent.putExtra("expenseDate", currentItem.getExpenseDate());
                //intent.putExtra("expenseMoney", currentItem.getExpenseMoney());
                //intent.putExtra("expenseImage", currentItem.getExpenseImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseTitles.size();
    }
}
