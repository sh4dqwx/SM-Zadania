package com.example.zad9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books) {
        RecyclerView booksRv = findViewById(R.id.books_rv);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(books);
        booksRv.setAdapter(adapter);
        booksRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance()
                .create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                if(response.body() != null) {
                    setupBookListView(response.body().getBookList());
                }
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(
                        findViewById(R.id.main_view),
                        getString(R.string.fetch_failure),
                        BaseTransientBottomBar.LENGTH_LONG
                ).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchBooksData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private class BookHolder extends RecyclerView.ViewHolder {
        private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
        private Book book;
        private ImageView coverIv;
        private TextView titleTv;
        private TextView authorTv;
        private TextView numberOfPagesTv;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            coverIv = itemView.findViewById(R.id.cover_iv);
            titleTv = itemView.findViewById(R.id.title_tv);
            authorTv = itemView.findViewById(R.id.author_tv);
            numberOfPagesTv = itemView.findViewById(R.id.number_of_pages_tv);
        }

        public void bind(Book book) {
            if(book != null && book.getTitle() != null && book.getTitle().length() > 0 && book.getAuthors() != null) {
                titleTv.setText(book.getTitle());
                authorTv.setText(TextUtils.join(", ", book.getAuthors()));
                numberOfPagesTv.setText(book.getNumberOfPages());
                if(book.getCover() != null) {
                    Picasso.get()
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.ic_book).into(coverIv);
                } else {
                    coverIv.setImageResource(R.drawable.ic_book);
                }
            }
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
            if(books != null) {
                Book book = books.get(position);
                holder.bind(book);
            } else {
                Log.d("MainActivity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        public void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }
}