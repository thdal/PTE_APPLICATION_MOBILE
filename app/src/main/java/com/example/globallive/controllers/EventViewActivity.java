package com.example.globallive.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.globallive.R;
import com.example.globallive.entities.EventCanaux;
import com.example.globallive.entities.EventTypes;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventViewActivity extends MainActivity implements IEventUtilsCallback {

    private Handler _mainHandler = new Handler();
    private int categorieId;
    private int canalId;
    private EventUtilsThread _thread;
    private IEventService _eventService;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        showBack();
        //On récupére l'objet événement selectionné depuis la liste
        com.example.globallive.entities.Event monEvent = (com.example.globallive.entities.Event) getIntent().getSerializableExtra("selected_event");
        //On init notre service et on l'envoie au thread cela permettra d'afficher
        //dynamiquement les catégories et les canaux, le thread permet un appel non bloquant.
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        //On récupére l'id et le canal de l'event
        //on mettra à jour la vue dans les callback de notre thread plus bas.
        categorieId = monEvent.getTypeEventId();
        canalId = monEvent.getCanalEventId();
        //On récupére tous nos modules de la vue pour les mettre à j
        TextView eventName = findViewById(R.id.textViewEventViewTitle);
        TextView eventDescription = findViewById(R.id.textViewEventDescription);
        TextView eventLink = findViewById(R.id.textViewEventLink);
        TextView eventDate = findViewById(R.id.textViewEventDate);
        TextView eventAdresse = findViewById(R.id.textViewEventAdresse);
        ImageView eventImg = findViewById(R.id.imageViewEventView);
        //On convertit au format date
        Date date = monEvent.getEventDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        //On rempli notre vue avec les données de l'événement selectionné.
        eventDate.setText(strDate);
        eventDescription.setText(monEvent.getEventDescription());
        eventLink.setText(monEvent.getEventLink());
        eventAdresse.setText(monEvent.getEventAddress());
        eventName.setText(monEvent.getEventName());
        //Si notre événément a une image perso on va la chercher sur l'API avec le bon chemin
        //Sinon dans le dossier drawable l'image par défaut
        if(monEvent.isEventImg()){
            String imgUrl = getString(R.string.api_url) + "/eventId"+monEvent.getId()+"/eventImg.jpg";
            Picasso.get().load(imgUrl).into(eventImg);
        }
        //Bouton de partage
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body is here";
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
    }

    @Override
    public void getEventTypesCallback(List<EventTypes> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for(EventTypes eventType : eventTypes){
                    if(eventType.getId() == categorieId){
                        TextView eventCategorie = findViewById(R.id.textViewEventCategorie);
                        eventCategorie.setText(eventType.getTypeEventName());
                    }
                }
            }
        });
    }

    @Override
    public void getEventCanauxCallback(List<EventCanaux> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for(EventCanaux eventCanal : eventCanaux){
                    if(eventCanal.getId() == categorieId){
                        TextView eventCanalTxtView = findViewById(R.id.textViewEventCanal);
                        eventCanalTxtView.setText(eventCanal.getCanalEventName());
                    }
                }
            }
        });
    }


}
