package com.example.globallive.services;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.OperationSuccess;
import com.example.globallive.entities.User;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    AuthenticatedUser RegisterUser(User user) throws IOException;
    AuthenticatedUser AuthenticateUser(User user) throws IOException;
    List<User> GetUsers() throws IOException, JSONException;
    void DeleteUser(int eventId, int profileId) throws IOException;
    void PutUser(User user) throws IOException, JSONException;

}
