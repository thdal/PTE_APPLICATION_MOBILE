package com.example.globallive.threads;

import android.util.Log;

import com.example.globallive.entities.Event;
import com.example.globallive.services.IEventService;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeThread extends Thread{
    IHomeActivityResult c;
    int _userID;
    IEventService _eventService;
    int sortBy;
    int selectedItem;

    public HomeThread(IHomeActivityResult c, int userID, IEventService eventService, int sortBy, int selectedItem) {
        this.c = c;
        this._userID = userID;
        this._eventService = eventService;
        this.sortBy = sortBy;
        this.selectedItem = selectedItem;
    }

    @Override
    public void run() {
        try {
            System.out.println("########## Let's have some events ##########");
            List<Event> myEvents = new ArrayList<>();
            //La variable sortBy fonctionne commme un code
            //1 les événements par catégories
            //2 les événéments par canaux
            //3 Tous les événéments
            //4 Seulement les événements du jour
            if(sortBy == 1){
                myEvents = _eventService.GetEventsWithType(selectedItem);
            }
            if(sortBy == 2){
                myEvents = _eventService.GetEventsWithCanal(selectedItem);
            }
            if(sortBy == 3){
                myEvents = _eventService.GetEvents();
            }
            if(sortBy == 4){
                myEvents = _eventService.GetEventsOfTheDay();
            }
            if(sortBy == 0){
                 myEvents = _eventService.GetEvents();
            }

            this.c.callbackSuccess(myEvents);
        } catch (IOException | JSONException e) {
            if(e instanceof FileNotFoundException){
                this.c.callbackFail("Opps! Événement introuvable");
            }
            e.printStackTrace();
        }
    }
}
