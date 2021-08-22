package com.example.globallive.threads;


import com.example.globallive.entities.Event;
import java.util.List;

public interface IHomeActivityResult {
    void callbackSuccess(List<Event> events); // would be in any signature
    void callbackFail(String msg); // would be in any signature

}
