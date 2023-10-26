package com.example.zad3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static TaskStorage storage;
    private List<Task> taskList;
    private TaskStorage() {
        taskList = new ArrayList<>();
        for(int i=1; i<=150; i++) {
            Task task = new Task();
            task.setName("Pilne zadanie nr " + i);
            task.setDone(i%3 == 0);
            taskList.add(task);
        }
    }

    public static TaskStorage getStorage() {
        if(storage == null) storage = new TaskStorage();
        return storage;
    }

    public List<Task> getTaskList() { return taskList; }
    public Task getTaskById(UUID id) {
        return taskList
                .stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
