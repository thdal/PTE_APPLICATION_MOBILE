package com.example.globallive.services;

import com.example.globallive.entities.Event;
import com.example.globallive.entities.OperationSuccess;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class EventServiceImplementation implements IEventService {
    private String _baseUrl = "http://192.168.0.25:3000";
    private String _addEventEndpoint = "addTicket";
    private String _getEventsEndpoint = "/api/events";


    @Override
    public void AddEvent(Event event) throws IOException, JSONException {
        //String json = ConnectionUtils.GET(this._baseUrl + event.getUserID() + "/" + event.getLink());

        ObjectMapper mapper = new ObjectMapper();
        //AddedTicket return_value = mapper.readValue(json, AddedTicket.class);
    }



    @Override
    public List<Event> GetEvents(int userId) throws IOException, JSONException {
        String json = ConnectionUtils.GET(this._baseUrl + _getEventsEndpoint);


        JSONArray array = new JSONArray(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        List<Event> return_value = mapper.readValue(String.valueOf(array), new TypeReference<List<Event>>(){});

        return return_value;
    }

    @Override
    public OperationSuccess DeleteEvent(int userId, int eventId) throws IOException {
        return null;
    }
}
