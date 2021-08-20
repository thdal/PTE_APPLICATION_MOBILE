package com.example.globallive.entities;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.tabs.EventListFragment;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        holder.getEventDescription().setText(event.getEventDescription());
        //On convertit au format date
        Date date = event.getEventDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        //On passe la date à la vue
        holder.getEventDate().setText(strDate);
        //On met une image perso
        String imgUrl = "";
        //Si notre événément a une image perso on va la chercher sur l'API avec le bon chemin
        //Sinon dans le dossier drawable l'image par défaut
        if(event.isEventImg()){
            imgUrl = holder.itemView.getContext().getString(R.string.api_url) + "/eventId"+event.getId()+"/eventImg.jpg";
        Picasso.get().load(imgUrl).into(holder.getEventImage());
        }
    }

    @Override
    public int getItemCount() {
        return _events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventName;
        private final TextView eventDescription;
        private final TextView eventDate;
        private final ImageView eventImage;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            eventName = (TextView) view.findViewById(R.id.textViewEventName);
            eventDescription = (TextView) view.findViewById(R.id.textViewEventDescription);
            eventDate = (TextView) view.findViewById(R.id.textViewEventDate);
            eventImage = (ImageView) view.findViewById(R.id.imageViewEvent);

        }

        public TextView getEventName() {
            return eventName;
        }
        public TextView getEventDescription() {
            return eventDescription;
        }
        public TextView getEventDate() {
            return eventDate;
        }
        public ImageView getEventImage() {
            return eventImage;
        }

    }
}
