package com.omnisoft.retrofitpractice.Adapters;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.omnisoft.retrofitpractice.CustomViews.DuoOptionView;

import java.util.ArrayList;

/**
 * Created by PSD on 13-04-17.
 */

public class MenuAdapter extends BaseAdapter {
    private final ArrayList<String> mOptionsTitles;
    private final ArrayList<Drawable> mOptionsIcons;
    private final ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();

    public MenuAdapter(ArrayList<String> titles, ArrayList<Drawable> icons) {
        mOptionsTitles = titles;
        mOptionsIcons = icons;
    }


    @Override
    public int getCount() {
        return mOptionsTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptionsTitles.get(position);
    }

    void setViewSelected(int position, boolean selected) {

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
        final String title = mOptionsTitles.get(position);
        final Drawable icon = mOptionsIcons.get(position);
        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }

        // Using the DuoOptionView's default selectors
        optionView.bind(title, icon);
        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);
        setViewSelected(position, true);
        return optionView;
    }
}