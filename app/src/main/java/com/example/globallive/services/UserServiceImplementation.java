package com.example.globallive.services;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONObject;

import java.io.IOException;

public class UserServiceImplementation implements IUserService {
    private String _baseUrl = "https://localhost:3000/";
    private String _registerEndpoint = "api/create";
    private String _authenticateEndpoint = "api/login";

    public AuthenticatedUser RegisterUser(Person person) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(person);

        JSONObject response = ConnectionUtils.POST(this._baseUrl + this._registerEndpoint, json);

        if(response != null && response.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
            AuthenticatedUser return_value = mapper.readValue(response.toString(), AuthenticatedUser.class);
            return return_value;
        }
        return null;
    }

    public AuthenticatedUser AuthenticateUser(Person person) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(person);

        JSONObject response = ConnectionUtils.POST(this._baseUrl + this._authenticateEndpoint, json);

        if(response != null && response.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
            AuthenticatedUser return_value = mapper.readValue(response.toString(), AuthenticatedUser.class);
            return return_value;
        }
        return null;
    }
}
