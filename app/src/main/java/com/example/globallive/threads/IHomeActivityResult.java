package com.example.globallive.threads;


import com.example.globallive.entities.Event;
import java.util.List;

public interface IHomeActivityResult {
    void callback(List<Event> events); // would be in any signature
}
