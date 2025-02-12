//package com.example.big2;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.big2.data.AppDatabase;
//import com.example.big2.data.GameDao;
//import com.example.big2.data.entities.Game;
//import com.example.big2.data.entities.Round;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(AndroidJUnit4.class)
//public class DatabaseTest {
//    private static final String TAG = "DatabaseTest"; // Log Tag
//    private AppDatabase db;
//    private GameDao gameDao;
//
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
//                .allowMainThreadQueries() // Only for testing
//                .build();
//        gameDao = db.gameDao();
//        Log.d(TAG, "Database created for testing.");
//    }
//
//    @After
//    public void closeDb() throws IOException {
//        db.close();
//        Log.d(TAG, "Database closed.");
//    }
//
//    @Test
//    public void testInsertRounds() {
//        Game game = new Game();
//        game.rounds = Arrays.asList(
//                new Round(1, 10, 5, 0, 5),
//                new Round(2, 3, 0, 5, 6)
//        );
//
//        gameDao.insertGame(game);
//        List<Game> games = gameDao.getAllGames();
//
//        assertEquals(1, games.size());
//        assertEquals(2, games.get(0).rounds.size()); // Expect 2 rounds
//
//        Log.d(TAG, "Inserted game with " + games.get(0).rounds.size() + " rounds.");
//    }
//
//    @Test
//    public void testDeleteGame() {
//        // Step 1: Insert a game
//        Game game = new Game();
//        game.rounds = List.of(new Round(1, 10, 5, 0, 5));
//
//        long gameId = gameDao.insertGame(game); // Insert and get the auto-generated ID
//        assertNotEquals(0, gameId); // Ensure the game was inserted
//
//        // Step 2: Fetch the game from the DB to get the correct ID
//        Game insertedGame = gameDao.getGameById(gameId);
//        assertNotNull(insertedGame);
//        assertEquals(gameId, insertedGame.gameId);
//
//        // Step 3: Delete the fetched game (with correct ID)
//        gameDao.deleteGame(insertedGame);
//
//        // Step 4: Verify deletion
//        Game deletedGame = gameDao.getGameById(gameId);
//        assertNull(deletedGame); // The game should no longer exist
//
//        Log.d(TAG, "Game deleted successfully.");
//    }
//
//    @Test
//    public void testFetchById() {
//        Game game = new Game();
//        game.rounds = List.of(new Round(1, 5, 5, 1, 0));
//
//        long gameId = gameDao.insertGame(game);
//        Game fetchedGame = gameDao.getGameById(gameId);
//
//        assertNotNull(fetchedGame); // Ensure the game is found
//        assertEquals(gameId, fetchedGame.gameId);
//        assertEquals(1, fetchedGame.rounds.size()); // Expect 1 round
//
//        Log.d(TAG, "Fetched game ID: " + fetchedGame.gameId + ", with " + fetchedGame.rounds.size() + " round(s).");
//    }
//}
