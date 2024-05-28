package com.example.fintrack;

public class Expense {
    private String expenseDate;
    private String expenseMoney;
    private String expenseImage;

    public Expense() {
    }

    public Expense(String expenseDate, String expenseMoney, String expenseImage) {
        this.expenseDate = expenseDate;
        this.expenseMoney = expenseMoney;
        this.expenseImage = expenseImage;
    }

    public String getExpenseDate() { return expenseDate; }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseMoney() { return expenseMoney; }

    public void setExpenseMoney(String expenseMoney) {
        this.expenseMoney = expenseMoney;
    }

    public String getExpenseImage() { return expenseImage; }

    public void setExpenseImage(String lastname) {
        this.expenseImage = expenseImage;
    }

}
