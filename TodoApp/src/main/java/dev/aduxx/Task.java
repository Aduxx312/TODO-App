package dev.aduxx;

import java.time.LocalDateTime;

public class Task {
    private final int id;
    private String description;
    private final LocalDateTime createdAt;
    private boolean completed;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    public Task(int id, String description, LocalDateTime createdAt, boolean completed) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
