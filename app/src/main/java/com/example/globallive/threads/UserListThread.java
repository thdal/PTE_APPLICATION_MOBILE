package com.example.globallive.threads;

import com.example.globallive.entities.Event;
import com.example.globallive.entities.User;
import com.example.globallive.services.IEventService;
import com.example.globallive.services.IUserService;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListThread extends Thread{
    IUserListCallback c;
    IUserService _userService;

    public UserListThread(IUserListCallback c, IUserService userService) {
        this.c = c;
        this._userService = userService;
    }

    @Override
    public void run() {
        try {
            System.out.println("########## Let's have some users ##########");
            List<User> myUsers = new ArrayList<>();
            myUsers = _userService.GetUsers();
            this.c.callbackSuccess(myUsers);
        } catch (IOException | JSONException e) {
            if(e instanceof FileNotFoundException){
                this.c.callbackFail("Opps! Utilisateur introuvable");
            }
            e.printStackTrace();
        }
    }
}
