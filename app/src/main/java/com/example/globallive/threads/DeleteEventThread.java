package com.example.globallive.threads;

import com.example.globallive.entities.Event;
import com.example.globallive.services.IEventService;

public class DeleteEventThread extends Thread{
    IDeleteEventCallback c;
    IEventService _eventService;
    int eventID;

    public DeleteEventThread(IDeleteEventCallback c, int eventID, IEventService eventService) {
        this.c = c;
        this.eventID = eventID;
        this._eventService = eventService;
    }

    @Override
    public void run() {
        try {
            _eventService.DeleteEvent(eventID);
            c.deleteEventCallbackSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
