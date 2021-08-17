package com.example.globallive.services;

import android.util.Log;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Person;
import com.example.globallive.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserServiceImplementation implements IUserService {
    //mettre son ip local, localhost pose pb
    private String _baseUrl = "http://192.168.0.25:3000";
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

    public void TryGetApi() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl+"/api/users");


        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<User> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<User>>(){});
        Log.d("USERSERVICE", return_value.toString());
    }
}
