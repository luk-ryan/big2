package com.example.big2.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "rounds",
        foreignKeys = @ForeignKey(entity = Game.class,
                parentColumns = "gameId",
                childColumns = "gameId",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "gameId")
)
public class Round {
    @PrimaryKey(autoGenerate = true)
    private int roundId;  // Unique ID for each round
    @ColumnInfo(name = "gameId")
    private int gameId;  // Foreign key linking to a Game
    @ColumnInfo(name = "roundNumber")
    private int roundNumber;
    @ColumnInfo(name = "s1")
    private int s1;
    @ColumnInfo(name = "s2")
    private int s2;
    @ColumnInfo(name = "s3")
    private int s3;
    @ColumnInfo(name = "s4")
    private int s4;

    public Round(int gameId, int roundNumber, int s1, int s2, int s3, int s4) {
        this.gameId = gameId;
        this.roundNumber = roundNumber;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getS1() {
        return s1;
    }

    public void setS1(int s1) {
        this.s1 = s1;
    }

    public int getS2() {
        return s2;
    }

    public void setS2(int s2) {
        this.s2 = s2;
    }

    public int getS3() {
        return s3;
    }

    public void setS3(int s3) {
        this.s3 = s3;
    }

    public int getS4() {
        return s4;
    }

    public void setS4(int s4) {
        this.s4 = s4;
    }
}
