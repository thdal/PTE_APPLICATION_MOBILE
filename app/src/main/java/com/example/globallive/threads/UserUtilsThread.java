package com.example.globallive.threads;

import com.example.globallive.entities.UserProfil;
import com.example.globallive.services.IUserService;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class UserUtilsThread extends Thread{
    IUserUtilsCallback c;
    IUserService _userService;
    List<UserProfil> _userProfils;

    public UserUtilsThread(IUserUtilsCallback c, IUserService userService) {
        this.c = c;
        this._userService = userService;
    }

    @Override
    public void run() {
        try {
            List<UserProfil> _userProfils = _userService.GetUserProfils();
            c.getUserProfilsCallback(_userProfils);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
