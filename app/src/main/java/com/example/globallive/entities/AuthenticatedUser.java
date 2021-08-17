package com.example.globallive.entities;

public class AuthenticatedUser {
    private User User;
    private OperationSuccess Validation;

    public AuthenticatedUser(User user, OperationSuccess Validation){
        this.User = user;
        this.Validation = Validation;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public OperationSuccess getValidation() {
        return Validation;
    }

    public void setValidation(OperationSuccess validation) {
        Validation = validation;
    }
}
