package com.example.globallive.services;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Person;

import java.io.IOException;

public interface IUserService {
    AuthenticatedUser RegisterUser(Person person) throws IOException;
    AuthenticatedUser AuthenticateUser(Person person) throws IOException;
}
