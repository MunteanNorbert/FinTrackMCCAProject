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

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {
    final ArrayList<String> singleExpenseMonthYear;
    final ArrayList<String> expenseMonthYear;
    final ArrayList<String> expensePrice;
    final ArrayList<String> expenseGroup;
    public final Context context;
    private String expenseTitle;
    private String expenseStatus;
    private String userID;
    private String userType;
    private String userType1;
    private DatabaseReference userRef;

    public static class StatisticsViewHolder extends RecyclerView.ViewHolder {
        public Button statisticsMonthButton;

        public StatisticsViewHolder(View itemView) {
            super(itemView);
            statisticsMonthButton = itemView.findViewById(R.id.tv_new_expense_title);
        }
    }

    public StatisticsAdapter(ArrayList<String> singleExpenseMonthYear, ArrayList<String> expenseMonthYear, ArrayList<String> expensePrice, ArrayList<String> expenseGroup, /*String expenseTitle,*/ String expenseStatus, String userType, String userType1, String userID, Context context) {
        this.singleExpenseMonthYear = singleExpenseMonthYear;
        this.expenseMonthYear = expenseMonthYear;
        this.expensePrice = expensePrice;
        this.expenseGroup = expenseGroup;
        //this.expenseTitle = expenseTitle;
        this.expenseStatus = expenseStatus;
        this.userType = userType;
        this.userType1 = userType1;
        this.userID = userID;
        this.context = context;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newexpensetitle_item, parent, false);
        StatisticsViewHolder viewHolder = new StatisticsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        String currentTitle = singleExpenseMonthYear.get(position);

        holder.statisticsMonthButton.setText(currentTitle);
        holder.statisticsMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatisticsInfoActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("userType", userType);
                intent.putExtra("userType1", userType1);
                intent.putExtra("expenseTitle", expenseTitle);
                intent.putExtra("singleExpenseMonthYear", currentTitle);
                intent.putExtra("expenseStatus", expenseStatus);
                intent.putExtra("expenseMonthYear", expenseMonthYear);
                intent.putExtra("expensePrice", expensePrice);
                intent.putExtra("expenseGroup", expenseGroup);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleExpenseMonthYear.size();
    }
}
