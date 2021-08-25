package com.example.globallive.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.controllers.UserEditActivity;
import com.example.globallive.entities.User;
import com.example.globallive.entities.UserAdapter;
import com.example.globallive.services.IUserService;
import com.example.globallive.services.UserServiceImplementation;
import com.example.globallive.threads.DeleteUserThread;
import com.example.globallive.threads.IDeleteUserCallback;
import com.example.globallive.threads.IPostUserCallback;
import com.example.globallive.threads.IUserListCallback;
import com.example.globallive.threads.PostUserThread;
import com.example.globallive.threads.UserListThread;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment implements IUserListCallback, IDeleteUserCallback, IPostUserCallback, UserAdapter.OnUserListener {

    //add
    private IUserService _userService;
    private int _userId;
    private UserListThread _userListThread;
    private PostUserThread _postUserThread;
    private DeleteUserThread _deleteUserThread;
    private RecyclerView _recyclerView;
    private UserAdapter _userAdapter;
    private Handler _mainHandler = new Handler();
    private User currentUser;

    private ArrayList<User> _users = new ArrayList<>();

    public UserListFragment(User user) {
        // Required public constructor
        this.currentUser = user;
        this._userService = new UserServiceImplementation();
        _userListThread = new UserListThread(this, _userService);
        _userListThread.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_user_list, container, false);
            this._recyclerView=view.findViewById(R.id.recyclerViewUser);

        return view;
    }

    private void DisplayMyUsers(List<User> users){
        this._users = (ArrayList<User>) users;
        _userAdapter = new UserAdapter(this, (ArrayList<User>) users, this);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _recyclerView.setAdapter(_userAdapter);

        // Display myEvents
        // TODO Front : Display list of Events
    }

    @Override
    public void callbackSuccess(List<User> users) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //On clear le message d'erreur si on a des événements
                DisplayMyUsers(users);
            }
        });
    }

    @Override
    public void callbackFail(String msg) {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //On envoie un tableau vide si on a une erreur pour clear la liste
                DisplayMyUsers(new ArrayList<>());
                //On init l'affichage de l'erreur
                //TODO
            }
        });
    }

    @Override
    public void deleteUserCallbackSuccess() {
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUsers();
            }
        });
    }

    //Callback de notre édition
    @Override
    public void postUserCallbackSuccess(){
        _mainHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUsers();
            }
        });
    }

    @Override
    public void onUserClick(int position) {
        //Intent intent = new Intent(getActivity(), EventViewActivity.class);
        //intent.putExtra("SELECTED_EVENT", (Serializable) _events.get(position) );
        //startActivity(intent);
    }

    @Override
    public void onUserEditClick(int position) {
        Intent intent = new Intent(getActivity(), UserEditActivity.class);
        intent.putExtra("SELECTED_USER", (Serializable) this._users.get(position) );
        intent.putExtra("CURRENT_USER", (Serializable) this.currentUser );
        startActivity(intent);
    }

    @Override
    public void onBanUserClick(int position, boolean isBanned) {
        if(this._users.get(position).getIsBanned() != isBanned){
            this._users.get(position).setIsBanned(isBanned);
            _postUserThread = new PostUserThread(this, this._users.get(position), _userService, true);
            _postUserThread.start();
        }
    }

    public void updateUsers(){
        _userListThread = new UserListThread(this, _userService);
        _userListThread.start();
    }

    public void onUserDeleteClick(int position){
        int userID = this._users.get(position).getId();
        int profileID = this._users.get(position).getProfile_id();
        Log.d("userID", String.valueOf(userID));
        Log.d("profileID", String.valueOf(profileID));

        _deleteUserThread = new DeleteUserThread(this, userID, profileID, _userService);
        _deleteUserThread.start();
    }



}
