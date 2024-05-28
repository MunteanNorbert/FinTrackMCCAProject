package com.example.fintrack;

public class Users {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String category;

    public Users() {
    }

    public Users(String username, String firstname, String lastname, String email, String category) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.category = category;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() { return firstname; }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }
}
