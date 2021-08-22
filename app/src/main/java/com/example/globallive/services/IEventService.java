package com.example.globallive.services;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanaux;
import com.example.globallive.entities.EventTypes;
import com.example.globallive.entities.OperationSuccess;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface IEventService {
    void PostEvent(Event event) throws IOException, JSONException;
    List<Event> GetEvents() throws IOException, JSONException;
    List<Event> GetEventsOfTheDay() throws IOException, JSONException;
    List<Event> GetEventsWithType(int typeID) throws IOException, JSONException;
    List<Event> GetEventsWithCanal(int canalID) throws IOException, JSONException;
    //Utils
    List<EventTypes> GetEventTypes() throws IOException, JSONException;
    List<EventCanaux> GetEventCanaux() throws IOException, JSONException;
    OperationSuccess DeleteEvent(int userId, int eventId) throws IOException;
}
