package com.example.globallive.services;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.OperationSuccess;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface IEventService {
    void PostEvent(Event event) throws IOException, JSONException;
    void PutEvent(Event event) throws IOException, JSONException;
    List<Event> GetEvents() throws IOException, JSONException;
    List<Event> GetEventsOfTheDay() throws IOException, JSONException;
    List<Event> GetEventsWithType(int typeID) throws IOException, JSONException;
    List<Event> GetEventsWithCanal(int canalID) throws IOException, JSONException;
    //Utils
    List<EventType> GetEventTypes() throws IOException, JSONException;
    List<EventCanal> GetEventCanaux() throws IOException, JSONException;
    OperationSuccess DeleteEvent(int eventId) throws IOException;
}
