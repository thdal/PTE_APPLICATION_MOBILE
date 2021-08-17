package com.example.globallive.threads;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.services.IUserService;
import com.example.globallive.entities.User;

import java.io.IOException;

public class RegisterThread extends Thread{
    IRegisterActivityCallback c;
    User _user;
    IUserService _userService;

    public RegisterThread(IRegisterActivityCallback c, User user, IUserService userService) {
        this.c = c;
        this._user = user;
        this._userService = userService;
    }

    @Override
    public void run() {
        try {
            AuthenticatedUser authenticatedUser = _userService.RegisterUser(_user);
            if(authenticatedUser.getValidation().isSuccess()){
                c.callBackSuccess(authenticatedUser.getUser().getId());
            }{
                c.callBackFail(authenticatedUser.getValidation().getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
