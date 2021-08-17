package com.example.globallive.threads;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Person;
import com.example.globallive.services.IUserService;

import org.json.JSONException;

import java.io.IOException;

public class AuthenticateThread extends Thread{
    IAuthenticateActivityCallback c;
    Person _person;
    IUserService _userService;

    public AuthenticateThread(IAuthenticateActivityCallback c, Person person, IUserService userService) {
        this.c = c;
        this._person = person;
        this._userService = userService;
    }

    @Override
    public void run() {
                    /*AuthenticatedUser authenticatedUser = _userService.AuthenticateUser(_person);
        try {
            if(authenticatedUser.getValidation().isSuccess()){
                c.callBackSuccess(authenticatedUser.getUser().getUserID());
            }else{
                c.callBackFail(authenticatedUser.getValidation().getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }*/
        try {
            _userService.TryGetApi();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
