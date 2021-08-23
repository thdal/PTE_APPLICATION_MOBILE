package com.example.globallive.threads;

import com.example.globallive.entities.User;

public interface IAuthenticateActivityCallback {
    void callBackSuccess(User user);
    void callBackFail(String message);
}
