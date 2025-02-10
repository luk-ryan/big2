package com.example.big2.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.big2.data.Round;
import com.example.big2.data.RoundConverter;

import java.util.List;

@Entity(tableName = "games")
public class Game {
    @PrimaryKey(autoGenerate = true)
    public int gameId;

    public String player1;
    public String player2;
    public String player3;
    public String player4;
    public boolean isCompleted;

    @TypeConverters(RoundConverter.class)
    public List<Round> rounds;  // List of Rounds stored as JSON
}
