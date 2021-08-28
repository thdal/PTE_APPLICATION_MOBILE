package com.example.globallive.tabs;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.controllers.AuthenticationActivity;
import com.example.globallive.controllers.EventEditActivity;
import com.example.globallive.controllers.EventViewActivity;
import com.example.globallive.controllers.MainActivity;
import com.example.globallive.entities.EventAdapter;
import com.example.globallive.entities.User;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.DeleteEventThread;
import com.example.globallive.threads.EventListThread;
import com.example.globallive.threads.IDeleteEventCallback;
import com.example.globallive.threads.IEventListCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class EventListFragment extends Fragment implements IEventListCallback, IDeleteEventCallback, EventAdapter.OnEventListener {

    //add
    private IEventService _eventService;
    private int _userId;
    private EventListThread _eventListThread;
    private DeleteEventThread _deleteEventThread;
    private RecyclerView _recyclerView;
    private EventAdapter _eventAdapter;
    private Handler _mainHandler = new Handler();
    private User currentUser;

    private ArrayList<com.example.globallive.entities.Event> _events = new ArrayList<>();

    public EventListFragment(User user) {
        // Required public constructor
        this.currentUser = user;
        this._eventService = new EventServiceImplementation();
        _eventListThread = new EventListThread(this, this._userId, _eventService, 0, 0, "");
        _eventListThread.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        this._recyclerView=view.findViewById(R.id.recyclerViewEvent);
        return view;
    }

    private void DisplayMyEvents(List<com.example.globallive.entities.Event> events){
        this._events = (ArrayList<com.example.globallive.entities.Event>) events;
        _eventAdapter = new EventAdapter(this, (ArrayList<com.example.globallive.entities.Event>) events, this, this.currentUser.getId());
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _recyclerView.setAdapter(_eventAdapter);

        // Display myEvents
        // TODO Front : Display list of Events
    }

    @Override
    public void callbackSuccess(List<com.example.globallive.entities.Event> events) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //On clear le message d'erreur si on a des événements
                LinearLayout llErr = (LinearLayout) getActivity().findViewById(R.id.linearLayoutError);
                llErr.setVisibility(View.GONE);
                DisplayMyEvents(events);
            }
        });
    }

    @Override
    public void callbackFail(String msg) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //On envoie un tableau vide si on a une erreur pour clear la liste
                DisplayMyEvents(new ArrayList<>());
                //On init l'affichage de l'erreur
                LinearLayout llErr = (LinearLayout) getActivity().findViewById(R.id.linearLayoutError);
                llErr.setVisibility(View.VISIBLE);
                TextView errorMsg = getActivity().findViewById(R.id.EventListErrorMsg);
                errorMsg.setText(msg);
            }
        });
    }

    @Override
    public void deleteEventCallbackSuccess() {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                updateEvents(0,0);
            }
        });
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(getActivity(), EventViewActivity.class);
        intent.putExtra("SELECTED_EVENT", (Serializable) _events.get(position) );
        startActivity(intent);
    }

    @Override
    public void onEventEditClick(int position) {
        /*Intent intent = new Intent(getActivity(), EventEditActivity.class);
        intent.putExtra("SELECTED_EVENT", (Serializable) this._events.get(position) );
        intent.putExtra("CURRENT_USER", (Serializable) this.currentUser );
        startActivity(intent);*/
        MainActivity activity = (MainActivity) this.getActivity();
        EventEditActivity.displayActivity(activity, this.currentUser, this._events.get(position));

    }

    public void updateEvents(int sortBy, int selectedItem){
        _eventListThread = new EventListThread(this, this._userId, _eventService, sortBy, selectedItem, "");
        _eventListThread.start();
    }

    public void searchEvents(String searchWord){
        _eventListThread = new EventListThread(this, this._userId, _eventService, 50, 0, searchWord);
        _eventListThread.start();
    }

    public void onEventDeleteClick(int position){
        int eventID = this._events.get(position).getId();
        _deleteEventThread = new DeleteEventThread(this, eventID, _eventService);
        _deleteEventThread.start();
    }
}
