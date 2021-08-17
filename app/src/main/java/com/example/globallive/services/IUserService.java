package com.example.globallive.services;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.User;

import org.json.JSONException;

import java.io.IOException;

public interface IUserService {
    AuthenticatedUser RegisterUser(User user) throws IOException;
    AuthenticatedUser AuthenticateUser(User user) throws IOException;
    void TryGetApi() throws IOException, JSONException; // Avirer

}
