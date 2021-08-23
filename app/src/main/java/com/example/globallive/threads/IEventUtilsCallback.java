package com.example.globallive.threads;

import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;

import java.util.List;

public interface IEventUtilsCallback {
    void getEventTypesCallback(List<EventType> eventTypes); // would be in any signature
    void getEventCanauxCallback(List<EventCanal> eventCanaux); // would be in any signature


}
