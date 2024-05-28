package com.example.fintrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class StatisticsInfoActivity extends AppCompatActivity {

    private String userID;
    private String employeeID;
    private String expenseStatus;
    private String userType;
    private String userType1;
    private String singleExpenseMonthYear;

    private float foodSum = 0;
    private float accommodationSum = 0;
    private float transportSum = 0;
    private float entertainmentSum = 0;

    private ArrayList<String> expenseMonthYear;
    private ArrayList<String> expensePrice;
    private ArrayList<String> expenseGroup;

    private ArrayList<String> food;
    private ArrayList<String> accommodation;
    private ArrayList<String> transport;
    private ArrayList<String> entertainment;

    private TextView statisticsInfoTitle;
    private TextView statisticsInfoFood;
    private TextView statisticsInfoAccommodation;
    private TextView statisticsInfoTransport;
    private TextView statisticsInfoEntertainment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_info);

        userID = getIntent().getStringExtra("userID");
        employeeID = getIntent().getStringExtra("employeeID");
        expenseStatus = getIntent().getStringExtra("expenseStatus");
        userType = getIntent().getStringExtra("userType");
        userType1 = getIntent().getStringExtra("userType1");
        singleExpenseMonthYear = getIntent().getStringExtra("singleExpenseMonthYear");
        expenseMonthYear = getIntent().getStringArrayListExtra("expenseMonthYear");
        expensePrice = getIntent().getStringArrayListExtra("expensePrice");
        expenseGroup = getIntent().getStringArrayListExtra("expenseGroup");

        statisticsInfoTitle = findViewById(R.id.statistics_item_title);
        statisticsInfoFood = findViewById(R.id.statistics_item_food);
        statisticsInfoAccommodation = findViewById(R.id.statistics_item_accommodation);
        statisticsInfoTransport = findViewById(R.id.statistics_item_transport);
        statisticsInfoEntertainment = findViewById(R.id.statistics_item_entertainment);

        food = new ArrayList<String>();
        accommodation = new ArrayList<String>();
        transport = new ArrayList<String>();
        entertainment = new ArrayList<String>();

        statisticsInfoTitle.setText(singleExpenseMonthYear);

        for (int i = 0; i < expenseMonthYear.size(); i++) {
            if (expenseMonthYear.get(i).equals(singleExpenseMonthYear)) {
                if(expenseGroup.get(i).equals("Food")){
                    food.add(expensePrice.get(i));
                    foodSum = foodSum + Float.parseFloat(expensePrice.get(i));
                }
                if(expenseGroup.get(i).equals("Accommodation")){
                    accommodation.add(expensePrice.get(i));
                    accommodationSum = accommodationSum + Float.parseFloat(expensePrice.get(i));
                }
                if(expenseGroup.get(i).equals("Transport")){
                    transport.add(expensePrice.get(i));
                    transportSum = transportSum + Float.parseFloat(expensePrice.get(i));
                }
                if(expenseGroup.get(i).equals("Entertainment")){
                    entertainment.add(expensePrice.get(i));
                    entertainmentSum = entertainmentSum + Float.parseFloat(expensePrice.get(i));
                }
            }
        }
        statisticsInfoFood.setText("Food: " + Float.toString(foodSum) + "$");
        statisticsInfoAccommodation.setText("Accommodation: " + Float.toString(accommodationSum) + "$");
        statisticsInfoTransport.setText("Transport: " + Float.toString(transportSum) + "$");
        statisticsInfoEntertainment.setText("Entertainment: " + Float.toString(entertainmentSum) + "$");
    }
}