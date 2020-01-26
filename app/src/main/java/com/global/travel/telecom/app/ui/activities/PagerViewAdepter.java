package com.global.travel.telecom.app.ui.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerViewAdepter extends FragmentPagerAdapter {
    public PagerViewAdepter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_menu();
                break;
            case 1:
                fragment = new Fragment_phone();
                break;
            case 2:
                fragment = new Fragment_recent();
                break;
            case 3:
                fragment = new Fragment_contacts();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
