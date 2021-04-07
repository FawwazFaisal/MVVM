package com.omnisoft.retrofitpractice.MVVM;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.FCM.FCMResponseBody;
import com.omnisoft.retrofitpractice.FCM.Notifier;
import com.omnisoft.retrofitpractice.Retrofit.CustomAPI;
import com.omnisoft.retrofitpractice.Retrofit.CustomRetrofitClient;
import com.omnisoft.retrofitpractice.Retrofit.RetrofitClient;
import com.omnisoft.retrofitpractice.Room.DAO;
import com.omnisoft.retrofitpractice.Room.Entity;
import com.omnisoft.retrofitpractice.Room.SingletonDB;
import com.omnisoft.retrofitpractice.Utility.Constants;

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
        Call<List<Entity>> call = RetrofitClient.getInstance(Constants.DATA_BASE_URL).getApi().getHeroes();
        CustomRetrofitClient.enqueue(call, "heroes", this);
        Notifier.sendNotification(App.user.fcmToken, this);
    }

    @Override
    public void onResponse(Call call, Response response, String tag) {
        if (response.isSuccessful()) {
            if (tag.equals("heroes")) {
                ArrayList<Entity> list = (ArrayList<Entity>) response.body();
                allHeroesMutable.setValue(list);
            } else if (tag.equals("fcm") && response.code() == 200) {
                if (((FCMResponseBody) response.body()).success == 1) {
                    Toast.makeText(App.getContext(), "Did you get the memo?", Toast.LENGTH_SHORT).show();
                }
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
