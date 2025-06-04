package dev.aduxx;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();
    private final Path filePath = Paths.get("tasks.txt");
    private int nextId = 1;

    public TaskManager() {
        loadTasks();
        if (!tasks.isEmpty()) {
            nextId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
        }
    }

    public void addTask(String description) {
        Task task = new Task(nextId++, description.trim());
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> findTaskById(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public void markAsDone(int id) {
        findTaskById(id).ifPresent(t -> t.setCompleted(true));
    }

    public boolean removeTask(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public boolean editTask(int id, String newDescription) {
        Optional<Task> taskOpt = findTaskById(id);
        if (taskOpt.isPresent()) {
            taskOpt.get().setDescription(newDescription.trim());
            return true;
        }
        return false;
    }

    public int clearCompleted() {
        int count = (int) tasks.stream().filter(Task::isCompleted).count();
        tasks.removeIf(Task::isCompleted);
        return count;
    }

    public void saveTasks() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Task task : tasks) {
                String line = String.format("%d|%s|%s|%b",
                        task.getId(),
                        task.getDescription().replace("|", "/"),
                        task.getCreatedAt(),
                        task.isCompleted());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("❌ Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    private void loadTasks() {
        if (!Files.exists(filePath)) return;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length != 4) continue;

                int id = Integer.parseInt(parts[0]);
                String description = parts[1];
                LocalDateTime createdAt = LocalDateTime.parse(parts[2]);
                boolean completed = Boolean.parseBoolean(parts[3]);

                Task task = new Task(id, description, createdAt, completed);
                tasks.add(task);
            }
        } catch (Exception e) {
            System.err.println("❌ Błąd odczytu pliku: " + e.getMessage());
        }
    }
}
