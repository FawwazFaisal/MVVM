package com.omnisoft.retrofitpractice.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Entity.class}, version = 1)
public abstract class SingletonDB extends RoomDatabase {
    private static SingletonDB instance = null;
    //remember Room.databaseBuidler is used to instantiate instance object
    //remember CONTEXT IS REQUIRED by Room.databaseBuilder
    //remember .fallBacktoDestructiveMigration after constructor of Room.databaseBuilder
    static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Entity entity = new Entity("All dancing all singing crap of the world", "Himself", "Dude", "https://www.gogocosplay.com/wp-content/uploads/2018/05/tyler_durden-e1534347560481.jpg", "Tyler Durden", "Hollywood", "Jack Squat", "Anarchy");
            new PreInsertionAsyncCallback(instance).execute(entity);
        }
    };

    public static synchronized SingletonDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SingletonDB.class, "heroDB")
                    .addCallback(callback)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract DAO getDao();

    private static class PreInsertionAsyncCallback extends AsyncTask<Entity, Void, Void> {
        DAO dao;

        public PreInsertionAsyncCallback(SingletonDB instance) {
            dao = instance.getDao();
        }

        @Override
        protected Void doInBackground(Entity... entities) {
            dao.insertHero(entities[0]);
            return null;
        }
    }
}
