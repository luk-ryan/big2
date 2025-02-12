package com.example.big2.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.big2.data.dao.GameDao;
import com.example.big2.data.dao.RoundDao;
import com.example.big2.data.entity.Game;
import com.example.big2.data.entity.Round;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Game.class, Round.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // DAO references
    public abstract GameDao gameDao();
    public abstract RoundDao roundDao();

    // Executor for background database operations
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    // Get the database instance
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "big2_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
