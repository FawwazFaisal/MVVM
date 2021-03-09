package com.omnisoft.retrofitpractice.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    void insertHero(Entity hero);

    @Update
    void updateHero(Entity hero);

    @Delete
    void deleteHero(Entity hero);

    @Query("DELETE FROM heroes")
    void deleteAllHeroes();

    @Query("SELECT * FROM heroes")
    LiveData<List<Entity>> getAllHeroes();
}
