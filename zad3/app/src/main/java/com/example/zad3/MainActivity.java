package com.example.zad3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID)getIntent().getSerializableExtra(TaskListFragment.EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }
}