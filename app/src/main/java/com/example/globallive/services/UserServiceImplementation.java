package com.example.globallive.services;

import android.util.Log;

import com.example.globallive.R;
import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.OperationSuccess;
import com.example.globallive.entities.User;
import com.example.globallive.entities.UserProfil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceImplementation implements IUserService {
    //mettre son ip local, localhost pose pb
    private String _baseUrl = App.getAppResources().getString(R.string.api_url);
    private String _registerEndpoint = "/api/users";
    private String _authenticateEndpoint = "/api/login";
    private String _deleteUser = "/api/users";
    private String _deleteUserProfile = "/api/deleteUserProfile";
    private String _getUserList = "/api/usersWithProfiles";
    private String _getUserProfils = "/api/userProfiles";
    private String _putUserProfile = "/api/updateUserProfile";
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
        String response = ConnectionUtils.POST(this._baseUrl + this._putUserEndpoint + '/' + user.getId(), json);
        if(response != null && response.length() > 0){
            URL url = new URL(this._baseUrl + this._putUserProfile+'/'+user.getUser_id());
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            //You definitively don't want to send "Accept-Encoding: application/json".
            httpCon.setRequestProperty("Content-Type", "application/json");
            JSONObject reqBody = new JSONObject();
            reqBody.put("profileId", user.getProfile_id());
            OutputStreamWriter wr = new OutputStreamWriter(httpCon.getOutputStream());
            wr.write(reqBody.toString());
            wr.flush();
            httpCon.getInputStream();
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

    //Récupére les types de profiles
    @Override
    public List<UserProfil> GetUserProfils() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getUserProfils);
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<UserProfil> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<UserProfil>>(){});
        return return_value;
    }

    @Override
    public void DeleteUser(int userId, int profileId) throws IOException {
        String strUrlDeleteUser = this._baseUrl + _deleteUser + '/' + userId;
        String strUrlDeleteUserProfile = this._baseUrl + _deleteUserProfile + '/' + userId + '/' + profileId;
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
