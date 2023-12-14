package com.example.zad8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {
    public static final String EDIT_BOOK_TITLE = "edit_book_title";
    public static final String EDIT_BOOK_AUTHOR = "edit_book_author";

    private EditText titleEt;
    private EditText authorEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        titleEt = findViewById(R.id.title_et);
        authorEt = findViewById(R.id.author_et);

        if(getIntent().hasExtra(EDIT_BOOK_TITLE))
            titleEt.setText(getIntent().getStringExtra(EDIT_BOOK_TITLE));
        if(getIntent().hasExtra(EDIT_BOOK_AUTHOR))
            authorEt.setText(getIntent().getStringExtra(EDIT_BOOK_AUTHOR));

        final Button saveB = findViewById(R.id.save_b);
        saveB.setOnClickListener(view -> {
            Intent intent = new Intent();
            if(TextUtils.isEmpty(titleEt.getText()) || TextUtils.isEmpty(authorEt.getText())) {
                setResult(RESULT_CANCELED, intent);
            } else {
                String title = titleEt.getText().toString();
                intent.putExtra(EDIT_BOOK_TITLE, title);
                String author = authorEt.getText().toString();
                intent.putExtra(EDIT_BOOK_AUTHOR, author);
                setResult(RESULT_OK, intent);
            }
            finish();
        });
    }
}