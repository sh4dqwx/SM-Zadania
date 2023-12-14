package com.example.zad1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_INDEX = "currentIndex";
    public static final String CORRECT_ANSWER = "correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private TextView questionText;
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button hintButton;
    private boolean promptWasShown;
    private int currentIndex = 0;
    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_version, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_find_resources, false)
    };

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].getTrueAnswer();
        int resultMessageId = 0;
        if (promptWasShown) {
            resultMessageId = R.string.shown_answer;
        } else if(userAnswer == correctAnswer) {
            resultMessageId = R.string.correct_answer;
        } else {
            resultMessageId = R.string.incorrect_answer;
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion() {
        questionText.setText(questions[currentIndex].getQuestionId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LIFECYCLE", "Wywołano metodę onCreate");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX);

        questionText = findViewById(R.id.question_text);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        hintButton = findViewById(R.id.hint_button);

        trueButton.setOnClickListener(view -> { checkAnswerCorrectness(true); });
        falseButton.setOnClickListener(view -> { checkAnswerCorrectness(false); });
        nextButton.setOnClickListener(view -> {
            currentIndex = (currentIndex+1)%questions.length;
            promptWasShown = false;
            setNextQuestion();
        });
        hintButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].getTrueAnswer();
            intent.putExtra(CORRECT_ANSWER, correctAnswer);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
        });
        setNextQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("LIFECYCLE", "Wywołano metodę onSaveInstanceState");
        outState.putInt(CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if(requestCode == REQUEST_CODE_PROMPT) {
            if(data == null) return;
            promptWasShown = data.getBooleanExtra(PromptActivity.PROMPT_SHOWN, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LIFECYCLE", "Wywołano metodę onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "Wywołano metodę onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "Wywołano metodę onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LIFECYCLE", "Wywołano metodę onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "Wywołano metodę onDestroy");
    }
}