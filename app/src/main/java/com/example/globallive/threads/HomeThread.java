package com.example.globallive.threads;

import com.example.globallive.entities.Event;
import com.example.globallive.services.IEventService;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class HomeThread extends Thread{
    IHomeActivityResult c;
    int _userID;
    IEventService _eventService;

    public HomeThread(IHomeActivityResult c, int userID, IEventService eventService) {
        this.c = c;
        this._userID = userID;
        this._eventService = eventService;
    }

    @Override
    public void run() {
        try {
            System.out.println("########## Let's have some events ##########");
            List<Event> myEvents = _eventService.GetEvents(_userID);
            this.c.callback(myEvents);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
