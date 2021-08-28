package com.example.globallive.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.tabs.EventListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> _events;
    private EventListFragment activity;
    private OnEventListener mOnEventListener;
    private int userID;

    public EventAdapter(EventListFragment activity, ArrayList<Event> events, OnEventListener onEventListener, int userID){
        this.activity = activity;
        this._events = events;
        this.mOnEventListener = onEventListener;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_event, viewGroup, false);
        return new ViewHolder(view, mOnEventListener);
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
        String imgUrl = "";
        //Si notre événément a une image perso on va la chercher sur l'API avec le bon chemin
        //Sinon dans le dossier drawable l'image par défaut
        if(event.isEventImg()){
            imgUrl = holder.itemView.getContext().getString(R.string.api_url) + "/eventId"+event.getId()+"/eventImg.jpg";
            Picasso.with(holder.getEventImage().getContext()).cancelRequest(holder.getEventImage());
            Picasso.with(holder.getEventImage().getContext()).load(imgUrl).into(holder.getEventImage());
        }else{
            Picasso.with(holder.getEventImage().getContext()).load(R.drawable.event).into(holder.getEventImage());
        }
        if(this.userID == event.getUserId()){
            holder.getEditionButton().setVisibility(View.VISIBLE);
            holder.getTrashButton().setVisibility(View.VISIBLE);
        }else{
            holder.getEditionButton().setVisibility(View.GONE);
            holder.getTrashButton().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(_events == null)
            return 0;
        return _events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView eventName;
        private final TextView eventDescription;
        private final TextView eventDate;
        private final ImageView eventImage;
        private final ImageButton editionButton;
        private final ImageButton trashButton;

        OnEventListener mOnEventListener;

        public ViewHolder(View view, OnEventListener OnEventListener) {
            super(view);
            //Le click sur un élément de la liste
            mOnEventListener = OnEventListener;
            view.setOnClickListener(this);
            // Define click listener for the ViewHolder's View
            eventName = (TextView) view.findViewById(R.id.textViewEventName);
            eventDescription = (TextView) view.findViewById(R.id.textViewEventDescription);
            eventDate = (TextView) view.findViewById(R.id.textViewEventDate);
            eventImage = (ImageView) view.findViewById(R.id.imageViewEvent);
            editionButton = (ImageButton) view.findViewById(R.id.layoutEventBtnEdit);
            trashButton = (ImageButton) view.findViewById(R.id.layoutEventBtnTrash);


            editionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnEventListener.onEventEditClick(getAdapterPosition());
                }
            });
            trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnEventListener.onEventDeleteClick(getAdapterPosition());
                }
            });

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
        public ImageButton getEditionButton() {
            return editionButton;
        }
        public ImageButton getTrashButton() {
            return trashButton;
        }


        @Override
        public void onClick(View view) {
            mOnEventListener.onEventClick(getAdapterPosition());
        }


    }

    public interface OnEventListener{
        void onEventClick(int position);
        void onEventEditClick(int position);
        void onEventDeleteClick(int position);
    }
}
