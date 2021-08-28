package com.example.globallive.threads;

import android.util.Log;

import com.example.globallive.entities.Event;
import com.example.globallive.entities.User;
import com.example.globallive.services.IEventService;
import com.example.globallive.services.IUserService;

public class PostUserThread extends Thread{
    IPostUserCallback c;
    IUserService _userService;
    User user;
    boolean update;

    public PostUserThread(IPostUserCallback c, User user, IUserService userService, boolean update) {
        this.c = c;
        this._userService = userService;
        this.user = user;
        this.update = update;
    }

    @Override
    public void run() {
        try {
            if(this.update)
                _userService.PutUser(user);
            else
                _userService.RegisterUser(user);
            c.postUserCallbackSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
