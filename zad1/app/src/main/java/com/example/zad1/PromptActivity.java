package com.example.zad1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {
    public static final String PROMPT_SHOWN = "promptShown";
    private boolean correctAnswer;
    private Button showPromptButton;
    private TextView promptText;

    private void setPromptShownResult(boolean promptShown) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PROMPT_SHOWN, promptShown);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        showPromptButton = findViewById(R.id.show_prompt_button);
        promptText = findViewById(R.id.prompt_text);

        correctAnswer = getIntent().getBooleanExtra(MainActivity.CORRECT_ANSWER, true);

        showPromptButton.setOnClickListener(view -> {
            int answer = correctAnswer ? R.string.button_true : R.string.button_false;
            promptText.setText(answer);
            setPromptShownResult(true);
        });
    }
}