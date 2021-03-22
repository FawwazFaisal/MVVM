package com.omnisoft.retrofitpractice.Adapters;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.omnisoft.retrofitpractice.CustomViews.DuoOptionView;

import java.util.ArrayList;

/**
 * Created by S.M.Mubbashir.A.Z. on 3/22/2021.
 */
public class MenuAdapter extends BaseAdapter {
    ArrayList<String> mOptions = new ArrayList<>();
    ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();
    ArrayList<Drawable> mIcons = new ArrayList<Drawable>();

    /*TODO: Add array list of drawable IDs and then use position on
       getView() to assign drawable based on respective positions*/

    public MenuAdapter(ArrayList<String> options, ArrayList<Drawable> icons) {
        mOptions = options;
        mIcons = icons;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    public void setViewSelected(int position, boolean selected) {
        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(selected);
            } else {
                mOptionViews.get(i).setSelected(!selected);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String option = mOptions.get(position);
        final Drawable icon = mIcons.get(position);
        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }
        // Using the DuoOptionView's default selectors
        optionView.bind(option, icon);
        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);
        return optionView;
    }
}