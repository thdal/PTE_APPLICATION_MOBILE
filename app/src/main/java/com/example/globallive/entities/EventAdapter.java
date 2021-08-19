package com.example.globallive.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.tabs.EventListFragment;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> _events;
    private EventListFragment activity;

    public EventAdapter(EventListFragment activity, ArrayList<Event> events){
        this.activity = activity;
        this._events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_event, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = _events.get(position);
        holder.getEventName().setText(event.getEventName());
        holder.getEventDescription().setText("- " + event.getEventDescription() + "%");
    }

    @Override
    public int getItemCount() {
        return _events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventName;
        private final TextView eventDescription;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            eventName = (TextView) view.findViewById(R.id.textViewEventName);
            eventDescription = (TextView) view.findViewById(R.id.textViewEventDescription);
        }

        public TextView getEventName() {
            return eventName;
        }
        public TextView getEventDescription() {
            return eventDescription;
        }
    }
}
