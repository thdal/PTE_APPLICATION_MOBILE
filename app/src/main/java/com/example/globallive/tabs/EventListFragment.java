package com.example.globallive.tabs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventAdapter;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.HomeThread;
import com.example.globallive.threads.IHomeActivityResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment implements  View.OnClickListener, IHomeActivityResult {

    //add
    private IEventService _eventService;
    private int _userId;
    private HomeThread _thread;
    private RecyclerView _recyclerView;
    private EventAdapter _eventAdapter;
    private Handler _mainHandler = new Handler();

    public EventListFragment() {
        // Required empty public constructor
        this._eventService = new EventServiceImplementation();
        //this._userId = getActivity().getIntent().getIntExtra("CURRENT_USER_ID", 0);


        _thread = new HomeThread(this, this._userId, _eventService);
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
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
