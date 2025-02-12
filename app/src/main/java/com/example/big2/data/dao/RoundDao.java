package com.example.big2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.example.big2.data.entities.Round;

@Dao
public interface RoundDao {
    @Insert
    long insertRound(Round round);

    @Query("SELECT * FROM rounds WHERE gameId = :gameId")
    List<Round> getRoundsForGame(int gameId);

    @Query("DELETE FROM rounds WHERE gameId = :gameId")
    void deleteRoundsForGame(int gameId);
}
