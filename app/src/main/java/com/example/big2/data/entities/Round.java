package com.example.big2.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "rounds",
        foreignKeys = @ForeignKey(entity = Game.class,
                parentColumns = "gameId",
                childColumns = "gameId",
                onDelete = ForeignKey.CASCADE))
public class Round {
    @PrimaryKey(autoGenerate = true)
    private int roundId;  // Unique ID for each round

    private int gameId;  // Foreign key linking to a Game
    private int roundNumber;
    private int s1;
    private int s2;
    private int s3;
    private int s4;

    public Round(int gameId, int roundNumber, int s1, int s2, int s3, int s4) {
        this.gameId = gameId;
        this.roundNumber =
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }
}
