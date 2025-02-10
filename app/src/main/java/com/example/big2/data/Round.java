package com.example.big2.data;

import androidx.annotation.NonNull;

public class Round {
    private int roundNumber;
    private int s1;
    private int s2;
    private int s3;
    private int s4;

    public Round(int roundNumber, int s1, int s2, int s3, int s4) {
        this.roundNumber = roundNumber;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }

    @NonNull
    public String toString() {
        return "Round " + roundNumber + ": "
                + "{s1: " + s1
                + "}, {s2: " + s2
                + "}, {s3: " + s3
                + "}, {s4: " + s4
                + "}";
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

