package com.example.globallive.tabs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.globallive.R;
import com.example.globallive.controllers.MainActivity;
import com.example.globallive.entities.Event;
import com.example.globallive.entities.EventCanaux;
import com.example.globallive.entities.EventTypes;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.IEventService;
import com.example.globallive.threads.EventUtilsThread;
import com.example.globallive.threads.IEventUtilsCallback;
import com.example.globallive.threads.IPostEventCallback;
import com.example.globallive.threads.PostEventThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventUtilsFormFragment extends Fragment  implements IEventUtilsCallback, IPostEventCallback {

    private EventUtilsThread _thread;
    private PostEventThread _postEventThread;

    private IEventService _eventService;
    //DatePicker
    private EditText editTextDate;
    private Button buttonDate;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private int userID;
    private IPostEventCallback c;

    private Handler _mainHandler = new Handler();
    //Constructor obligatoire dans un fragment
    public EventUtilsFormFragment(int userID) {
        //On init notre service et on l'envoie au thread cela permettra d'afficher
        //dynamiquement les catégories et les canaux, le thread permet un appel non bloquant.
        this.userID = userID;
        this._eventService = new EventServiceImplementation();
        _thread = new EventUtilsThread(this, _eventService);
        _thread.start();
        c = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_form, container, false);
        //Le click de notre bouton submit dans le fragment
        Button button = (Button) view.findViewById(R.id.submitEvent);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Spinner mySpinnerCat = (Spinner) getActivity().findViewById(R.id.spinnerEventFormCat);
                EventTypes typeSelected = (EventTypes) mySpinnerCat.getSelectedItem();
                Spinner mySpinnerCan = (Spinner) getActivity().findViewById(R.id.spinnerEventFormCat);
                EventTypes canalSelected = (EventTypes) mySpinnerCan.getSelectedItem();
                TextView _errorDisplay = getActivity().findViewById(R.id.errorEventForm);
                TextView eventTitle = getActivity().findViewById(R.id.EventFormEventTitle);
                TextView eventLink = getActivity().findViewById(R.id.EventFormEventLink);
                TextView eventDate = getActivity().findViewById(R.id.editText_date);
                TextView eventAddress = getActivity().findViewById(R.id.EventFormEventAddress);
                TextView eventDescription = getActivity().findViewById(R.id.EventFormEventDescription);
                eventTitle.setText("test");
                eventLink.setText("test");
                eventAddress.setText("test");
                eventTitle.setText("test");

                //Si un des champs n'est pas rempli
                if(eventTitle.getText().toString().length() <= 0 ||
                        eventLink.getText().toString().length() <= 0 ||
                        eventDate.getText().toString().length() <= 0 ||
                        eventAddress.getText().toString().length() <= 0 ||
                        eventDescription.getText().toString().length() <= 0)
                {
                    _errorDisplay.setText("Veuillez remplir tous les champs");
                }
                //Sinon on post
                else{
                    try {
                        _errorDisplay.setText("");
                        Event eventToSend = new Event();
                        eventToSend.setTypeEventId(typeSelected.getId());
                        eventToSend.setCanalEventId(canalSelected.getId());
                        eventToSend.setEventName(eventTitle.getText().toString());
                        eventToSend.setEventImg(false); // pas d'upload d'image pr linstant sur lapp mobile
                        eventToSend.setEventAddress(eventAddress.getText().toString());
                        eventToSend.setEventDescription(eventDescription.getText().toString());
                        eventToSend.setEventLink(eventLink.getText().toString());
                        //On parse la string au format date
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(eventDate.getText().toString());
                        //le modele se charge d'un deuxieme parse au format de la bdd yyyy-MM-dd voir annotation dans le fichier
                        eventToSend.setEventDate(date);
                        eventToSend.setUserId(userID);
                        _postEventThread = new PostEventThread(c, eventToSend, _eventService);
                        _postEventThread.start();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Select date
        this.editTextDate = (EditText) view.findViewById(R.id.editText_date);
        this.buttonDate = (Button) view.findViewById(R.id.button_date);
        this.buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectDate();
            }
        });
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        return view;
    }

    @Override
    public void getEventTypesCallback(List<EventTypes> eventTypes){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //get the spinner from the xml.
                Spinner dropdown = getActivity().findViewById(R.id.spinnerEventFormCat);
                //create a list of items for the spinner.
                //Nos objets models avec méthode toString() réécrite
                ArrayList<EventTypes> dropdownList = new ArrayList<>();
                for(EventTypes eventType : eventTypes){
                    dropdownList.add(eventType);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                ArrayAdapter<EventTypes> adapter = new ArrayAdapter<EventTypes>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropdownList);
                //set the spinners adapter to the previously created one.
                dropdown.setAdapter(adapter);
            }
        });
    }

    @Override
    public void getEventCanauxCallback(List<EventCanaux> eventCanaux){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Spinner dropdown = getActivity().findViewById(R.id.spinnerEventFormCanal);
                ArrayList<EventCanaux> dropdownList = new ArrayList<>();
                for(EventCanaux eventCanal : eventCanaux){
                    dropdownList.add(eventCanal);
                }
                ArrayAdapter<EventCanaux> adapter = new ArrayAdapter<EventCanaux>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropdownList);
                dropdown.setAdapter(adapter);
            }
        });
    }

    @Override
    public void postEventCallbackSuccess(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                HomeActivity.displayActivity((MainActivity) getActivity(), userID, "");
            }
        });
    }


    // User click on 'Select Date' button.
    private void buttonSelectDate() {

        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = null;


            datePickerDialog = new DatePickerDialog(getActivity(),
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);


        // Show
        datePickerDialog.show();
    }
}
