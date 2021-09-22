package com.quickbooks2.on_boarding_screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new OnBoardingScreen1();

        }
        else if (position == 1) {

            return new OnBoardingScreen2();

        }
        else if (position == 2){

            return new OnBoardingScreen3();
        }

        return createFragment(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
