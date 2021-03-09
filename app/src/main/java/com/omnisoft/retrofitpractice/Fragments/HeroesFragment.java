package com.omnisoft.retrofitpractice.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omnisoft.retrofitpractice.Adapters.HeroRVAdapter;
import com.omnisoft.retrofitpractice.MVVM.HeroesViewModel;
import com.omnisoft.retrofitpractice.Room.Entity;
import com.omnisoft.retrofitpractice.databinding.FragmentBlankBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HeroesFragment extends Fragment {

    FragmentBlankBinding bd;
    List<Entity> list;
    HeroRVAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentBlankBinding.inflate(inflater, container, false);
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
        setViewModel();
    }

    private void setRecyclerView() {
        list = new ArrayList<>();
        bd.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new HeroRVAdapter(requireActivity());
        bd.recyclerView.setAdapter(adapter);
    }

    private void setViewModel() {
        HeroesViewModel heroesModel = ViewModelProviders.of(this).get(HeroesViewModel.class);
        heroesModel.attachActivity(requireActivity());
        observerDB(heroesModel);
        observerAPI(heroesModel);
    }

    private void observerAPI(HeroesViewModel heroesModel) {
        heroesModel.getAllHeroesAPI().observe(this, entities -> {
            if (entities != null) {
                for (Entity entity : entities) {
                    if (!isDuplicate(entity)) {
                        heroesModel.insertHero(entity);
                    }
                }
            }
        });
    }

    private boolean isDuplicate(Entity entity) {
        boolean isDuplicate = false;
        for (Entity ent : list) {
            if (ent.getName().equals(entity.getName())) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }

    private void observerDB(HeroesViewModel heroesModel) {
        heroesModel.getAllHeroesDB().observe(this, entities -> {
            if (entities != null) {
                for (Entity entity : entities) {
                    if (!isDuplicate(entity)) {
                        list.add(entity);
                    }
                }
                adapter.updateList(list);
            }
        });
    }
}