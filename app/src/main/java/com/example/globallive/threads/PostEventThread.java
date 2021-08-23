package com.example.globallive.threads;

import com.example.globallive.entities.Event;
import com.example.globallive.services.IEventService;

public class PostEventThread extends Thread{
    IPostEventCallback c;
    IEventService _eventService;
    Event event;
    boolean update;

    public PostEventThread(IPostEventCallback c, Event event, IEventService eventService, boolean update) {
        this.c = c;
        this._eventService = eventService;
        this.event = event;
        this.update = update;
    }

    @Override
    public void run() {
        try {
            if(this.update)
                _eventService.PutEvent(event);
            else
                _eventService.PostEvent(event);
            c.postEventCallbackSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
