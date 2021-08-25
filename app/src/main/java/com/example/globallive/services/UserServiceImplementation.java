package com.example.globallive.services;

import android.util.Log;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.OperationSuccess;
import com.example.globallive.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



public class UserServiceImplementation implements IUserService {
    //mettre son ip local, localhost pose pb
    private String _baseUrl = "http://192.168.0.25:3000";
    private String _registerEndpoint = "/api/users";
    private String _authenticateEndpoint = "/api/login";
    private String _deleteUser = "/api/users";
    private String _deleteUserProfile = "/api/deleteUserProfile";
    private String _getUserList = "/api/usersWithProfiles";
    private String _putUserEndpoint = "/api/usersMobile";

    //Post
    public AuthenticatedUser RegisterUser(User user) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);

        String response = ConnectionUtils.POST(this._baseUrl + this._registerEndpoint, json);

        if(response != null && response.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
            User userFound = mapper.readValue(response.toString(), User.class);
            OperationSuccess operationSuccess = new OperationSuccess(true, null);
            AuthenticatedUser return_value = new AuthenticatedUser(userFound, operationSuccess);
            return return_value;
        }else{
            OperationSuccess operationSuccess = new OperationSuccess(false, "Inscription impossible");
            AuthenticatedUser return_value = new AuthenticatedUser(null, operationSuccess);
            return return_value;
        }
    }
    //Put
    @Override
    public void PutUser(User user) throws IOException, JSONException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);
        //On ajoute un paramétre pour que ça match avec l'api
        //JSONObject subParam = new JSONObject();
        //subParam.put("user", json);
        String response = ConnectionUtils.POST(this._baseUrl + this._putUserEndpoint + '/' + user.getId(), json);
        if(response != null && response.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
        }else{

        }
    }

    public AuthenticatedUser AuthenticateUser(User user) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);
        String response = ConnectionUtils.POST(this._baseUrl + this._authenticateEndpoint, json);
        if(response != null && response.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
            User userFound = mapper.readValue(response.toString(), User.class);
            OperationSuccess operationSuccess = new OperationSuccess(true, null);
            AuthenticatedUser return_value = new AuthenticatedUser(userFound, operationSuccess);
            return return_value;
        }else{
            OperationSuccess operationSuccess = new OperationSuccess(false, "E-mail ou password invalide");
            AuthenticatedUser return_value = new AuthenticatedUser(null, operationSuccess);
            return return_value;
        }
    }

    //Récupére la liste des utilisateurs
    public List<User> GetUsers() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getUserList);
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<User> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<User>>(){});
        return return_value;
    }

    @Override
    public void DeleteUser(int userId, int profileId) throws IOException {
        String strUrlDeleteUser = this._baseUrl + _deleteUser + '/' + userId;
        String strUrlDeleteUserProfile = this._baseUrl + _deleteUserProfile + '/' + userId + '/' + profileId;
        Log.d("hey?", this._baseUrl + _deleteUser + '/' + userId);

        Log.d("hey?", this._baseUrl + _deleteUserProfile + '/' + userId + '/' + profileId);


        URL urlDeleteUserProfile = new URL(strUrlDeleteUserProfile);
        HttpURLConnection connection = (HttpURLConnection) urlDeleteUserProfile.openConnection();
        connection.setRequestMethod("DELETE");
        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            URL urlDeleteUser = new URL(strUrlDeleteUser);
            HttpURLConnection secondConnection = (HttpURLConnection) urlDeleteUser.openConnection();
            connection.setRequestMethod("DELETE");
            int secondResponseCode = secondConnection.getResponseCode();
        }
    }


}
