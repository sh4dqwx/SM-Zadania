package com.example.zad8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { Book.class }, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static BookDatabase databaseInstance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract BookDao bookDao();

    public static BookDatabase getDatabase(final Context context) {
        if(databaseInstance == null)
            databaseInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BookDatabase.class,
                    "book_database").addCallback(roomDatabaseCallback).build();
        return databaseInstance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                BookDao dao = databaseInstance.bookDao();
                dao.insert(new Book("Clean Code", "Robert C. Martin"));
                dao.insert(new Book("Atomic Habits", "James Clear"));
                dao.insert(new Book("Outsider", "Stephen King"));
            });
        }
    };
}
