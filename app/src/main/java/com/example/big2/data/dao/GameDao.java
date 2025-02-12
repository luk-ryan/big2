package com.example.big2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.big2.data.entities.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Insert
    void insert(Game game);

    @Update
    void update(Game game);

    @Delete
    void delete(Game game);

    @Query("SELECT * FROM games ORDER BY gameId DESC")
    List<Game> getAllGames();

    @Query("SELECT * FROM games WHERE gameId = :id LIMIT 1")
    Game getGameById(int id);
}
