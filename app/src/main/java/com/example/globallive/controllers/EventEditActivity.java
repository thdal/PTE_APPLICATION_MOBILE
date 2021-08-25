package com.example.globallive.controllers;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.globallive.R;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanal;
import com.example.globallive.entities.EventType;
import com.example.globallive.entities.User;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.tabs.HomeActivity;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.example.globallive.threads.IPostEventCallback;
import com.example.globallive.threads.PostEventThread;
import com.example.globallive.threads.RegisterThread;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventEditActivity extends MainActivity implements IEventUtilsCallback, IPostEventCallback {

    private Handler _mainHandler = new Handler();
    private Event selectedEvent;
    private IEventService _eventService;
    private EventUtilsThread _thread;
    Context context = this;
    User currentUser;
    private PostEventThread _postEventThread;
    //DatePicker
    private EditText editTextDate;
    private Button buttonDate;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    //Nos inputs
    private TextView eventTitle ;
    private TextView eventLink;
    private TextView eventDate;
    private TextView eventAddress;
    private TextView eventDescription;
    private Button btnSubmit;
    private Spinner mySpinnerCat;
    private Spinner mySpinnerCan;
    //Callback
    private IPostEventCallback c;

    public static void displayActivity(MainActivity activity, User user, Event event){
        Intent intent = new Intent(activity, EventEditActivity.class);
        intent.putExtra("SELECTED_EVENT", event );
        intent.putExtra("CURRENT_USER",  user );
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_form);
        showBack();
        setTitle("Modifier un événement");
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Modifier un événement");
        c = this;
        this.currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        //L'événement
        selectedEvent = (Event) getIntent().getSerializableExtra("SELECTED_EVENT");
        //On vire le logo du header pour gagner en harmonie
        RelativeLayout outer = (RelativeLayout)findViewById(R.id.headerInclude);
        ImageView iv = (ImageView)outer.findViewById(R.id.imageViewLogoGL);
        TextView txtLabel = (TextView)outer.findViewById(R.id.textViewTitle);
        txtLabel.setTypeface(null, Typeface.BOLD);
        iv.setVisibility(View.GONE);
        //On vire tout
        //outer.setVisibility(View.GONE);
        //On init notre service et on l'envoie au thread cela permettra d'afficher
        //dynamiquement les catégories et les canaux, le thread permet un appel non bloquant.
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        TextView _errorDisplay = this.findViewById(R.id.errorEventForm);
        eventTitle = this.findViewById(R.id.EventFormEventTitle);
        eventLink = this.findViewById(R.id.EventFormEventLink);
        eventDate = this.findViewById(R.id.editText_date);
        eventAddress = this.findViewById(R.id.EventFormEventAddress);
        eventDescription = this.findViewById(R.id.EventFormEventDescription);
        btnSubmit = this.findViewById(R.id.submitEvent);
        //addIcon btnSubmit
        btnSubmit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
        mySpinnerCat = (Spinner) this.findViewById(R.id.spinnerEventFormCat);
        mySpinnerCan = (Spinner) this.findViewById(R.id.spinnerEventFormCanal);
        btnSubmit.setText("Modifier");
        eventTitle.setText(selectedEvent.getEventName());
        eventLink.setText(selectedEvent.getEventLink());
        eventAddress.setText(selectedEvent.getEventAddress());
        eventDescription.setText(selectedEvent.getEventDescription());
        //Date to string
        Date date = selectedEvent.getEventDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        //string to the input
        eventDate.setText(strDate);
        //Btn date
        //Select date
        this.editTextDate = (EditText) this.findViewById(R.id.editText_date);
        EditText textDate = this.findViewById(R.id.editText_date);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO STUFF
                buttonSelectDate();
            }
        });
        //Le submit
        Button button = (Button) this.findViewById(R.id.submitEvent);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.submitEvent:
                        try {
                            EventType typeSelected = (EventType) mySpinnerCat.getSelectedItem();
                            EventCanal canalSelected = (EventCanal) mySpinnerCan.getSelectedItem();
                            Event eventToSend = new Event();
                            eventToSend.setTypeEventId(typeSelected.getId());
                            eventToSend.setCanalEventId(canalSelected.getId());
                            eventToSend.setEventName(eventTitle.getText().toString());
                            eventToSend.setEventImg(false); // pas d'upload d'image pr linstant sur lapp mobile
                            eventToSend.setEventAddress(eventAddress.getText().toString());
                            eventToSend.setEventDescription(eventDescription.getText().toString());
                            eventToSend.setEventLink(eventLink.getText().toString());
                            //On parse la string au format date
                            Date date = null;
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(eventDate.getText().toString());
                            //le modele se charge d'un deuxieme parse au format de la bdd yyyy-MM-dd voir annotation dans le fichier
                            eventToSend.setEventDate(date);
                            eventToSend.setId(selectedEvent.getId());
                            eventToSend.setUserId(selectedEvent.getUserId());
                            Toast.makeText(context,  String.valueOf(selectedEvent.getId()), Toast.LENGTH_SHORT).show();
                            _postEventThread = new PostEventThread(c, eventToSend, _eventService, true);
                            _postEventThread.start();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void getEventTypesCallback(List<EventType> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //get the spinner from the xml.
                Spinner dropdown = findViewById(R.id.spinnerEventFormCat);
                //create a list of items for the spinner.
                //Nos objets models avec méthode toString() réécrite
                ArrayList<EventType> dropdownList = new ArrayList<>();
                for(EventType eventType : eventTypes){
                    dropdownList.add(eventType);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                ArrayAdapter<EventType> adapter = new ArrayAdapter<EventType>(context, android.R.layout.simple_spinner_dropdown_item, dropdownList);
                //set the spinners adapter to the previously created one.
                dropdown.setAdapter(adapter);
                dropdown.setSelection(selectedEvent.getTypeEventId() -1);
            }
        });
    }

    @Override
    public void getEventCanauxCallback(List<EventCanal> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Spinner dropdown = findViewById(R.id.spinnerEventFormCanal);
                ArrayList<EventCanal> dropdownList = new ArrayList<>();
                for(EventCanal eventCanal : eventCanaux){
                    dropdownList.add(eventCanal);
                }
                ArrayAdapter<EventCanal> adapter = new ArrayAdapter<EventCanal>(context, android.R.layout.simple_spinner_dropdown_item, dropdownList);
                dropdown.setAdapter(adapter);
                dropdown.setSelection(selectedEvent.getCanalEventId() -1);
            }
        });
    }

    // User click on 'Select Date' button.
    private void buttonSelectDate() {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(context, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        // Show
        datePickerDialog.show();
    }

    //Callback de notre édition
    @Override
    public void postEventCallbackSuccess(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity((MainActivity) context, currentUser, "");
            }
        });
    }

    //Menu de droite tripledot (met à jour la vue)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    //Item selectionné du menu triple dot
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.rightMenuItemSignOut:
                Intent homeIntent = new Intent(Intent.ACTION_MAIN); //deconnexion
                homeIntent.addCategory( Intent.CATEGORY_HOME );//deconnexion
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//deconnexion
                startActivity(homeIntent);//deconnexion
                AuthenticationActivity.displayActivity(this); //Retour connexion
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
