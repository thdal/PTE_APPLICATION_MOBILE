package com.example.globallive.entities;

public class AuthenticatedUser {
    private Person User;
    private OperationSuccess Validation;

    public Person getUser() {
        return User;
    }

    public void setUser(Person user) {
        User = user;
    }

    public OperationSuccess getValidation() {
        return Validation;
    }

    public void setValidation(OperationSuccess validation) {
        Validation = validation;
    }
}
