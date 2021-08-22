package com.example.globallive.services;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanaux;
import com.example.globallive.entities.EventTypes;
import com.example.globallive.entities.OperationSuccess;
import com.example.globallive.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class EventServiceImplementation implements IEventService {
    private String _baseUrl = "http://192.168.0.25:3000";
    private String _postEventEndpoint = "/api/events";
    private String _getEventsEndpoint = "/api/events";
    private String _getEventsWithTypeEndpoint = "/api/events/type";
    private String _getEventsWithCanalEndpoint = "/api/events/canal";
    private String _getEventsOfTheDay = "/api/events/oftheday";

    private String _getEventTypes ="/api/eventTypes";
    private String _getEventCanaux ="/api/eventCanals";

    @Override
    public void PostEvent(Event event) throws IOException, JSONException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(event);
        //On ajoute un paramétre pour que ça match avec l'api
        JSONObject subParam = new JSONObject();
        subParam.put("event", json);
        String response = ConnectionUtils.POST(this._baseUrl + this._postEventEndpoint, subParam.toString());
        if(response != null && response.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
        }else{

        }

    }


    //Récupére tous nos événéments
    @Override
    public List<Event> GetEvents() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventsEndpoint);
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<Event> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<Event>>(){});
        return return_value;
    }

    //Récupére tous nos événéments en fonction de la catégorie
    @Override
    public List<Event> GetEventsWithType(int typeID) throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventsWithTypeEndpoint+"/"+typeID+"/false");
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<Event> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<Event>>(){});
        return return_value;
    }

    //Récupére tous nos événéments en fonction du canal
    @Override
    public List<Event> GetEventsWithCanal(int canalID) throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventsWithCanalEndpoint+"/"+canalID+"/false");
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<Event> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<Event>>(){});
        return return_value;
    }

    //Récupére tous nos événéments
    @Override
    public List<Event> GetEventsOfTheDay() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventsOfTheDay);
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<Event> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<Event>>(){});
        return return_value;
    }

    //Récupére les types d'événements (Culture, Pédagogique ect.)
    @Override
    public List<EventTypes> GetEventTypes() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventTypes);
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<EventTypes> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<EventTypes>>(){});
        return return_value;
    }

    //Récupére les canaux d'événements (Facebook, twitch ect.)
    @Override
    public List<EventCanaux> GetEventCanaux() throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventCanaux);
        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<EventCanaux> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<EventCanaux>>(){});
        return return_value;
    }

    @Override
    public OperationSuccess DeleteEvent(int userId, int eventId) throws IOException {
        return null;
    }
}
