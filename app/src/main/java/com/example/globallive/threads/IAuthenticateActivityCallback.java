package com.example.globallive.threads;

public interface IAuthenticateActivityCallback {
    void callBackSuccess(int userID);
    void callBackFail(String message);
}
