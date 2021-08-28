package com.example.globallive.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.globallive.R;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.Normalizer;
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
        //On vire le logo du header pour gagner en harmonie
        RelativeLayout outer = (RelativeLayout)findViewById(R.id.headerInclude);
        ImageView iv = (ImageView)outer.findViewById(R.id.imageViewLogoGL);
        TextView txtLabel = (TextView)outer.findViewById(R.id.textViewTitle);
        txtLabel.setTypeface(null, Typeface.BOLD);
        iv.setVisibility(View.GONE);
        //On récupére l'objet événement selectionné depuis la liste
        com.example.globallive.entities.Event monEvent = (com.example.globallive.entities.Event) getIntent().getSerializableExtra("SELECTED_EVENT");
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
        TextView eventHour = findViewById(R.id.textViewEventHour);
        TextView eventAdresse = findViewById(R.id.textViewEventAdresse);
        ImageView eventImg = findViewById(R.id.imageViewEventView);
        //On convertit au format date
        Date date = monEvent.getEventDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        //On format l'heure
        String eventHourstr = "";
        if(monEvent.getEventHour() == null){
            eventHourstr = "--:--";
        }else{
            eventHourstr = monEvent.getEventHour().toString();
        }
        //On rempli notre vue avec les données de l'événement selectionné.
        eventDate.setText(strDate);
        eventHour.setText(eventHourstr);
        eventDescription.setText(monEvent.getEventDescription());
        eventLink.setText(monEvent.getEventLink());
        eventAdresse.setText(monEvent.getEventAddress());
        //eventName.setText(monEvent.getEventName());
        setTitle(monEvent.getEventName());

        //Si notre événément a une image perso on va la chercher sur l'API avec le bon chemin
        //Sinon dans le dossier drawable l'image par défaut
        if(monEvent.isEventImg()){
            String imgUrl = getString(R.string.api_url) + "/eventId"+monEvent.getId()+"/eventImg.jpg";
            Picasso.with(this).load(imgUrl).into(eventImg);
        }else{
            Picasso.with(this).load(R.drawable.event).into(eventImg);
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
    public void getEventTypesCallback(List<EventType> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for(EventType eventType : eventTypes){
                    if(eventType.getId() == categorieId){
                        TextView eventCategorie = findViewById(R.id.textViewEventCategorie);
                        eventCategorie.setText(eventType.getTypeEventName());
                        ImageView imageViewCategorie = findViewById(R.id.imageViewCategorie);
                        String mDrawableName = stripAccents(eventType.getTypeEventName().toLowerCase());
                        //On récupére et on assigne nos icons dynamiquement
                        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                        imageViewCategorie.setImageResource(resID);
                    }
                }
            }
        });
    }




    @Override
    public void getEventCanauxCallback(List<EventCanal> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for(EventCanal eventCanal : eventCanaux){
                    if(eventCanal.getId() == categorieId){
                        TextView eventCanalTxtView = findViewById(R.id.textViewEventCanal);
                        eventCanalTxtView.setText(eventCanal.getCanalEventName());
                        ImageView imageViewCanal = findViewById(R.id.imageViewCanal);
                        String mDrawableName = stripAccents(eventCanal.getCanalEventName().toLowerCase());
                        //On récupére et on assigne nos icons dynamiquement
                        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                        imageViewCanal.setImageResource(resID);
                    }
                }
            }
        });
    }

    //On vire les accents de notre string pour matcher avec nos images en local
    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }




}
