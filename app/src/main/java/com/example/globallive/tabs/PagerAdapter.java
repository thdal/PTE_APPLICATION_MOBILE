package com.example.globallive.tabs;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.globallive.entities.User;
import com.example.globallive.services.EventServiceImplementation;
import com.example.globallive.services.UserServiceImplementation;

public class PagerAdapter extends FragmentPagerAdapter {
    private int tabsNumber;
    private User user;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs, User user) {
        super(fm, behavior);
        this.tabsNumber = tabs;
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EventListFragment(user, new EventServiceImplementation(), new Handler());
            case 1:
                return new EventFormFragment(user);
            case 2:
                return new UserListFragment(user);
                default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
