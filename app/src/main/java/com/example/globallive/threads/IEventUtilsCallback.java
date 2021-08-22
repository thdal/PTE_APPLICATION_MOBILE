package com.example.globallive.threads;

import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanaux;
import com.example.globallive.entities.EventTypes;

import java.util.List;

public interface IEventUtilsCallback {
    void getEventTypesCallback(List<EventTypes> eventTypes); // would be in any signature
    void getEventCanauxCallback(List<EventCanaux> eventCanaux); // would be in any signature


}
