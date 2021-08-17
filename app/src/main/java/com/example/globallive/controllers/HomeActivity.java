package com.example.globallive.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.globallive.R;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventAdapter;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.HomeThread;
import com.example.globallive.threads.IHomeActivityResult;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends MainActivity implements View.OnClickListener, IHomeActivityResult {

    private IEventService _eventService;
    private int _userId;
    private HomeThread _thread;
    private RecyclerView _recyclerView;
    private EventAdapter _eventAdapter;

    private Handler _mainHandler = new Handler();

    public static void displayActivity(MainActivity activity, int currentUser, String msg){
        Intent intent = new Intent(activity,HomeActivity.class);
        intent.putExtra("CURRENT_USER_ID", currentUser);
        intent.putExtra("MESSAGE", msg);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Acceuil");

        this._eventService = new EventServiceImplementation();
        this._recyclerView=findViewById(R.id.recyclerView);
        this._userId = getIntent().getIntExtra("CURRENT_USER_ID", 0);


        _thread = new HomeThread(this, this._userId, _eventService);
        _thread.start();


        //findViewById(R.id.buttonGroupes).setOnClickListener(this);
       // findViewById(R.id.buttonCategorie).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
           /* case R.id.buttonGroupes:
                GroupActivity.displayActivity(this);
                break;
            case R.id.buttonCategorie:
                CategorieActivity.displayActivity(this);
                break;*/
        }
    }

    private void DisplayMyEvents(List<Event> events){
        _eventAdapter = new EventAdapter(this, (ArrayList<Event>) events);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.setAdapter(_eventAdapter);
        // Display myEvents
        // TODO Front : Display list of Events
    }

    @Override
    public void callback(List<Event> events) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                DisplayMyEvents(events);
            }
        });
    }
}




