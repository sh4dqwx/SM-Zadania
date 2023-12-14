package com.example.zad8;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zad8.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    private Book editedBook;
    private BookViewModel bookViewModel;

    private final ItemTouchHelper.Callback touchHelperCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int swipeFlags = ItemTouchHelper.RIGHT;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar.make(
                    findViewById(R.id.main_layout),
                    getString(R.string.book_archived),
                    Snackbar.LENGTH_LONG
            ).show();
        }
    };
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView booksRv = findViewById(R.id.books_rv);
        itemTouchHelper.attachToRecyclerView(booksRv);
        final BookAdapter adapter = new BookAdapter();
        booksRv.setAdapter(adapter);
        booksRv.setLayoutManager(new LinearLayoutManager(this));

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.findAll().observe(this, adapter::setBooks);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
            startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Book book = new Book(
                    data.getStringExtra(EditBookActivity.EDIT_BOOK_TITLE),
                    data.getStringExtra(EditBookActivity.EDIT_BOOK_AUTHOR)
            );
            bookViewModel.insert(book);
            Snackbar.make(
                    findViewById(R.id.main_layout),
                    getString(R.string.book_added),
                    Snackbar.LENGTH_LONG
            ).show();
            return;
        }

        if(requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedBook.setTitle(data.getStringExtra(EditBookActivity.EDIT_BOOK_TITLE));
            editedBook.setAuthor(data.getStringExtra(EditBookActivity.EDIT_BOOK_AUTHOR));
            bookViewModel.update(editedBook);
            Snackbar.make(
                    findViewById(R.id.main_layout),
                    getString(R.string.book_updated),
                    Snackbar.LENGTH_LONG
            ).show();
            return;
        }

        Snackbar.make(
                findViewById(R.id.main_layout),
                getString(R.string.empty_not_saved),
                Snackbar.LENGTH_LONG
        ).show();
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Book book;
        private TextView titleTv;
        private TextView authorTv;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            titleTv = itemView.findViewById(R.id.title_tv);
            authorTv = itemView.findViewById(R.id.author_tv);
        }

        public void bind(Book book) {
            this.book = book;
            titleTv.setText(book.getTitle());
            authorTv.setText(book.getAuthor());
        }

        @Override
        public void onClick(View v) {
            MainActivity.this.editedBook = book;
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
            intent.putExtra(EditBookActivity.EDIT_BOOK_TITLE, book.getTitle());
            intent.putExtra(EditBookActivity.EDIT_BOOK_AUTHOR, book.getAuthor());
            startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            bookViewModel.delete(book);
            Snackbar.make(
                    findViewById(R.id.main_layout),
                    getString(R.string.book_deleted),
                    Snackbar.LENGTH_LONG
            ).show();
            return true;
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if(books != null)
                holder.bind(books.get(position));
            else
                Log.d("MainActivity", "No books");
        }

        @Override
        public int getItemCount() {
            if(books != null)
                return books.size();
            return 0;
        }

        public void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }
}