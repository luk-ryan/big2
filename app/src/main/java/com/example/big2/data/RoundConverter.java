package com.example.big2.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RoundConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromRoundList(List<Round> rounds) {
        return gson.toJson(rounds);
    }

    @TypeConverter
    public static List<Round> toRoundList(String roundJson) {
        Type listType = new TypeToken<List<Round>>() {}.getType();
        return gson.fromJson(roundJson, listType);
    }
}
