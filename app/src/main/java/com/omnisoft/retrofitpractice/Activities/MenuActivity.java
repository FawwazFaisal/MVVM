package com.omnisoft.retrofitpractice.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.omnisoft.retrofitpractice.Adapters.MenuAdapter;
import com.omnisoft.retrofitpractice.CustomViews.DuoMenuView;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.databinding.ActivityMenuBinding;

import java.util.ArrayList;

public class MenuActivity extends BaseActivity implements DuoMenuView.OnMenuClickListener, View.OnClickListener {

    ActivityMenuBinding bd;
    ArrayList<String> menuOpts = new ArrayList<>();
    ArrayList<Drawable> menuIcons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        handleMenu();
        setOnClickListener();
    }

    private void setOnClickListener() {
        ImageButton menuBtn = findViewById(R.id.menuBtn);
        ImageButton backBtn = findViewById(R.id.backBtn);
        menuBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void handleMenu() {
        bd.menu.setOnMenuClickListener(this);
        bd.drawer.setMarginFactor(0.55f);
        setMenuOpts();
        setMenuIcons();
        MenuAdapter adapter = new MenuAdapter(menuOpts, menuIcons);
        bd.menu.setAdapter(adapter);
    }

    private void setMenuIcons() {
        menuIcons.add(ContextCompat.getDrawable(this, R.drawable.ic_baseline_format_list_bulleted_24));
        menuIcons.add(ContextCompat.getDrawable(this, R.drawable.ic_baseline_map_24));
        menuIcons.add(ContextCompat.getDrawable(this, R.drawable.ic_baseline_ad_units_24));
    }

    private void setMenuOpts() {
        menuOpts.add("List");
        menuOpts.add("Maps");
        menuOpts.add("Ads");
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        ((MenuAdapter) bd.menu.getAdapter()).setViewSelected(position, true);
        toggleMenu();
    }

    void toggleMenu() {
        if (bd.drawer.isDrawerOpen()) {
            bd.drawer.closeDrawer();
        } else {
            bd.drawer.openDrawer();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menuBtn || v.getId() == R.id.backBtn) {
            toggleMenu();
        }
    }
}