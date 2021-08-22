package com.example.globallive.tabs;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private int tabsNumber;
    private int userID;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs, int userID) {
        super(fm, behavior);
        this.tabsNumber = tabs;
        this.userID = userID;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EventListFragment();
            case 1:
                return new EventUtilsFormFragment(userID);
                default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
