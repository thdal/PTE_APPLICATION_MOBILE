package com.example.globallive.threads;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.User;
import com.example.globallive.services.IUserService;

import org.json.JSONException;

import java.io.IOException;

public class AuthenticateThread extends Thread{
    IAuthenticateActivityCallback c;
    User _user;
    IUserService _userService;

    public AuthenticateThread(IAuthenticateActivityCallback c, User user, IUserService userService) {
        this.c = c;
        this._user = user;
        this._userService = userService;
    }

    @Override
    public void run() {
        try {
            AuthenticatedUser authenticatedUser = _userService.AuthenticateUser(_user);
            if(authenticatedUser.getValidation().isSuccess()){
                c.callBackSuccess(authenticatedUser.getUser().getId());
            }else{
                c.callBackFail(authenticatedUser.getValidation().getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        /*try {
            _userService.TryGetApi();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }
}
