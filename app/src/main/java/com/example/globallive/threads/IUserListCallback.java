package com.example.globallive.threads;


import com.example.globallive.entities.User;

import java.util.List;

public interface IUserListCallback {
    void callbackSuccess(List<User> users); // would be in any signature
    void callbackFail(String msg); // would be in any signature

}
