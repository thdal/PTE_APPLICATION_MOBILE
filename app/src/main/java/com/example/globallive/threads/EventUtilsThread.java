package com.example.globallive.threads;

import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.User;
import com.example.globallive.services.IEventService;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class EventUtilsThread extends Thread{
    IEventUtilsCallback c;
    User _user;
    IEventService _eventService;
    List<EventType> _eventTypes;

    public EventUtilsThread(IEventUtilsCallback c, IEventService eventService) {
        this.c = c;
        this._eventService = eventService;
    }

    @Override
    public void run() {
        try {
            List<EventType> _eventTypes = _eventService.GetEventTypes();
            List<EventCanal> _eventCanaux = _eventService.GetEventCanaux();
            c.getEventTypesCallback(_eventTypes);
            c.getEventCanauxCallback(_eventCanaux);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
