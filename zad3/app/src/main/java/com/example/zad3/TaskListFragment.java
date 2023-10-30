package com.example.zad3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String EXTRA_TASK_ID = "extra_task_id";
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;

    private class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Task task;
        private TextView taskItemName;
        private TextView taskItemDate;

        public void bind(Task task) {
            this.task = task;
            taskItemName.setText(task.getName());
            taskItemDate.setText(task.getDate().toString());
        }

        public TaskViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_task_item, parent, false));
            itemView.setOnClickListener(this);

            taskItemName = itemView.findViewById(R.id.task_item_name);
            taskItemDate = itemView.findViewById(R.id.task_item_date);
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

        if(taskAdapter == null) {
            taskAdapter = new TaskAdapter(taskList);
            taskRecyclerView.setAdapter(taskAdapter);
        } else {
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        taskRecyclerView = view.findViewById(R.id.task_recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}
