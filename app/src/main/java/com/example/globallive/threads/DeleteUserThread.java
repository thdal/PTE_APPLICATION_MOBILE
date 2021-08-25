package com.example.globallive.threads;

import com.example.globallive.services.IUserService;

public class DeleteUserThread extends Thread{
    IDeleteUserCallback c;
    IUserService _userService;
    int userID;
    int profileId;

    public DeleteUserThread(IDeleteUserCallback c, int userID, int profileId, IUserService userService) {
        this.c = c;
        this.userID = userID;
        this.profileId = profileId;
        this._userService = userService;
    }

    @Override
    public void run() {
        try {
            _userService.DeleteUser(userID, profileId);
            c.deleteUserCallbackSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
