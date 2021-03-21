package com.omnisoft.retrofitpractice.MVVM;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omnisoft.retrofitpractice.Room.Entity;
import com.omnisoft.retrofitpractice.Utility.Snack.CustomSnack;
import com.omnisoft.retrofitpractice.Utility.Timer.Timer;
import com.omnisoft.retrofitpractice.Utility.Timer.TimerCallback;

import java.util.List;
import java.util.concurrent.Executors;

public class HeroesViewModel extends AndroidViewModel implements TimerCallback {
    Activity activity;
    Repo repo;
    MutableLiveData<List<Entity>> allHeroesMutable;
    LiveData<List<Entity>> allHeroes;

    public HeroesViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application);
    }

    public void insertHero(Entity hero) {
        repo.insert(hero);
    }

    void updateHero(Entity hero) {
        repo.update(hero);
    }

    void deleteHero(Entity hero) {
        repo.delete(hero);
    }

    void deleteAllHeroes() {
        repo.deleteAll();
    }

    void loadHeroesFromDB() {
        allHeroes = repo.getAllHeroesDB();
    }

    void loadHeroesFromAPI() {
        if (allHeroesMutable == null) {
            allHeroesMutable = new MutableLiveData<>();
        }
        Timer timer = new Timer(5000, 1000, this);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                timer.start();
            }
        });
    }

    public LiveData<List<Entity>> getAllHeroesDB() {
        loadHeroesFromDB();
        return allHeroes;
    }

    public LiveData<List<Entity>> getAllHeroesAPI() {
        loadHeroesFromAPI();
        return allHeroesMutable;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        CustomSnack.showSnackbar(activity, Long.toString(millisUntilFinished / 1000), "OK");
    }

    @Override
    public void onFinish() {
        repo.setAllHeroesMutable(allHeroesMutable);
    }

    public void attachActivity(Activity mainActivity) {
        activity = mainActivity;
    }
}