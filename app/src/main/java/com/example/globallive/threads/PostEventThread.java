package com.example.globallive.threads;

import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanaux;
import com.example.globallive.entities.EventTypes;
import com.example.globallive.entities.User;
import com.example.globallive.services.IEventService;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class PostEventThread extends Thread{
    IPostEventCallback c;
    IEventService _eventService;
    Event event;

    public PostEventThread(IPostEventCallback c, Event event, IEventService eventService) {
        this.c = c;
        this._eventService = eventService;
        this.event = event;
    }

    @Override
    public void run() {
        try {
            _eventService.PostEvent(event);
            c.postEventCallbackSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
