package com.example.zad3;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {
    private static final String SUBTITLE_VISIBLE = "subtitle_visible";
    public static final String EXTRA_TASK_ID = "extra_task_id";
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private boolean subtitleVisible;

    private class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Task task;
        private CheckBox taskItemCheckbox;
        private TextView taskItemName;
        private TextView taskItemDate;
        private ImageView taskItemIcon;

        public void bind(Task task) {
            this.task = task;

            taskItemCheckbox.setChecked(task.isDone());
            taskItemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                this.task.setDone(isChecked);
                if(isChecked) taskItemName.setPaintFlags(taskItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                else taskItemName.setPaintFlags(taskItemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            });

            taskItemName.setText(task.getName());
            if(task.isDone()) taskItemName.setPaintFlags(taskItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else taskItemName.setPaintFlags(taskItemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", new Locale("pl", "PL"));
            taskItemDate.setText(dateFormat.format(task.getDate()));

            if(task.getCategory().equals(Category.STUDIES)) {
                taskItemIcon.setImageResource(R.drawable.ic_studies);
            } else {
                taskItemIcon.setImageResource(R.drawable.ic_home);
            }
        }

        public TaskViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_task_item, parent, false));
            itemView.setOnClickListener(this);

            taskItemCheckbox = itemView.findViewById(R.id.task_item_checkbox);
            taskItemName = itemView.findViewById(R.id.task_item_name);
            taskItemDate = itemView.findViewById(R.id.task_item_date);
            taskItemIcon = itemView.findViewById(R.id.task_item_icon);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
        private List<Task> taskList;

        public TaskAdapter(List<Task> taskList) {
            this.taskList = taskList;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TaskViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = taskList.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }

    private void updateView() {
        List<Task> taskList = TaskStorage.getStorage().getTaskList();

        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(taskList);
            taskRecyclerView.setAdapter(taskAdapter);
        } else {
            taskAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private void updateSubtitle() {
        List<Task> taskList = TaskStorage.getStorage().getTaskList();
        int todoTasksCount = 0;
        for(Task task : taskList) {
            if(!task.isDone()) todoTasksCount++;
        }
        String subtitle = getString(R.string.subtitle_format, todoTasksCount);
        if(!subtitleVisible) subtitle = null;
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        taskRecyclerView = view.findViewById(R.id.task_recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.new_task) {
            Task task = new Task();
            TaskStorage.getStorage().addTask(task);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(EXTRA_TASK_ID, task.getId());
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_VISIBLE, subtitleVisible);
    }
}
