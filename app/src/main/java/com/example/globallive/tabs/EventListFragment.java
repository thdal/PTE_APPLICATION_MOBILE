package com.example.globallive.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.controllers.EventViewActivity;
import com.example.globallive.entities.EventAdapter;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.HomeThread;
import com.example.globallive.threads.IHomeActivityResult;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment implements  View.OnClickListener, IHomeActivityResult, EventAdapter.OnEventListener {

    //add
    private IEventService _eventService;
    private int _userId;
    private HomeThread _thread;
    private RecyclerView _recyclerView;
    private EventAdapter _eventAdapter;
    private Handler _mainHandler = new Handler();
    private ArrayList<com.example.globallive.entities.Event> _events = new ArrayList<>();

    public EventListFragment() {
        // Required empty public constructor
        this._eventService = new EventServiceImplementation();
        _thread = new HomeThread(this, this._userId, _eventService, 0, 0);
        _thread.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        this._recyclerView=view.findViewById(R.id.recyclerView);
        return view;
    }
    @Override
    public void onClick(View v) {
    }

    private void DisplayMyEvents(List<com.example.globallive.entities.Event> events){
        this._events = (ArrayList<com.example.globallive.entities.Event>) events;
        _eventAdapter = new EventAdapter(this, (ArrayList<com.example.globallive.entities.Event>) events, this);
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
    public void onEventClick(int position) {
        Intent intent = new Intent(getActivity(), EventViewActivity.class);
        intent.putExtra("selected_event", (Serializable) _events.get(position) );
        startActivity(intent);
    }

    public void updateEvents(int sortBy, int selectedItem){
        _thread = new HomeThread(this, this._userId, _eventService, sortBy, selectedItem);
        _thread.start();
    }
}
