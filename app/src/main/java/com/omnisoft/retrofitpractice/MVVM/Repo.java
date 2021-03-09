package com.omnisoft.retrofitpractice.MVVM;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omnisoft.retrofitpractice.Retrofit.CustomAPI;
import com.omnisoft.retrofitpractice.Retrofit.CustomRetrofitClient;
import com.omnisoft.retrofitpractice.Retrofit.RetrofitClient;
import com.omnisoft.retrofitpractice.Room.DAO;
import com.omnisoft.retrofitpractice.Room.Entity;
import com.omnisoft.retrofitpractice.Room.SingletonDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Repo implements CustomAPI {
    DAO dao;
    LiveData<List<Entity>> allHeroes;
    MutableLiveData<List<Entity>> allHeroesMutable;

    public Repo(Application application) {
        SingletonDB db = SingletonDB.getInstance(application);
        dao = db.getDao();
        allHeroes = dao.getAllHeroes();
    }

    public void setAllHeroesMutable(MutableLiveData<List<Entity>> mutableLiveData) {
        allHeroesMutable = mutableLiveData;
        Call<List<Entity>> call = RetrofitClient.getInstance().getApi().getHeroes();
        new CustomRetrofitClient(call, "heroes", this);
    }

    @Override
    public void onResponse(Call call, Response response, String tag) {
        if (response.isSuccessful()) {
            if (tag.equals("heroes")) {
                ArrayList<Entity> list = (ArrayList<Entity>) response.body();
                allHeroesMutable.setValue(list);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, String tag) {

    }

    void insert(Entity hero) {
        new InsertAsyncTask(dao).execute(hero);
    }

    void update(Entity hero) {
        new UpdateAsyncTask(dao).execute(hero);
    }

    void delete(Entity hero) {
        new DeleteAsyncTask(dao).execute(hero);
    }

    void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    LiveData<List<Entity>> getAllHeroesDB() {
        return allHeroes;
    }

    LiveData<List<Entity>> getAllHeroesAPI() {
        return allHeroesMutable;
    }

    static class InsertAsyncTask extends AsyncTask<Entity, Void, Void> {
        DAO dao;

        public InsertAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Entity... entities) {
            dao.insertHero(entities[0]);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Entity, Void, Void> {
        DAO dao;

        public UpdateAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Entity... entities) {
            dao.updateHero(entities[0]);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Entity, Void, Void> {
        DAO dao;

        public DeleteAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Entity... entities) {
            dao.deleteHero(entities[0]);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        DAO dao;

        public DeleteAllAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllHeroes();
            return null;
        }
    }
}
