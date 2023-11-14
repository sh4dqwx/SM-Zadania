package com.example.zad3;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
        category = Category.HOME;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public Date getDate() { return date; }
    public boolean isDone() { return done; }
    public Category getCategory() { return category; }
    public void setName(String name) { this.name = name; }
    public void setDate(Date date) { this.date = date; }
    public void setDone(boolean done) { this.done = done; }
    public void setCategory(Category category) { this.category = category; }
}
