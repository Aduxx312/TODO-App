package dev.aduxx;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class TodoApp {
    private final TaskManager taskManager = new TaskManager();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new TodoApp().run();
    }

    public void run() {
        System.out.println("🚀 Witaj w aplikacji TODO!");
        System.out.println("Wpisz 'help' aby zobaczyć dostępne komendy\n");

        while (true) {
            System.out.print("todo> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String cmd = input.split(" ")[0].toLowerCase();

            try {
                switch (cmd) {
                    case "quit", "exit" -> quit();
                    case "help" -> showHelp();
                    case "list" -> listTasks();
                    case "clear" -> clearCompleted();
                    case "add" -> addTask(input.substring(4));
                    case "done" -> markAsDone(input.substring(5));
                    case "remove" -> removeTask(input.substring(7));
                    case "edit" -> editTask(input.substring(5));
                    default -> System.out.println("❌ Nieznana komenda. Wpisz 'help'.");
                }
            } catch (Exception e) {
                System.out.println("❌ Błąd: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void quit() {
        taskManager.saveTasks();
        System.out.println("👋 Do zobaczenia!");
        System.exit(0);
    }

    private void showHelp() {
        System.out.println("""
                📋 Dostępne komendy:

                add <zadanie>           - Dodaj nowe zadanie
                list                    - Pokaż wszystkie zadania
                done <id>               - Oznacz zadanie jako wykonane
                remove <id>             - Usuń zadanie
                edit <id> <nowy tekst>  - Edytuj zadanie
                clear                   - Usuń wykonane zadania
                help                    - Pokaż tę pomoc
                quit/exit               - Wyjdź
                """);
    }

    private void addTask(String desc) {
        if (desc.isBlank()) {
            System.out.println("❌ Opis zadania nie może być pusty!");
            return;
        }
        taskManager.addTask(desc);
        System.out.println("✅ Dodano: \"" + desc.trim() + "\"");
    }

    private void listTasks() {
        List<Task> tasks = taskManager.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("📋 Brak zadań.");
            return;
        }

        System.out.printf("%-4s %-10s %-30s %s%n", "ID", "Status", "Zadanie", "Utworzono");
        System.out.println("-".repeat(70));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        for (Task task : tasks) {
            String status = task.isCompleted() ? "✅ Gotowe" : "⏳ Otwarte";
            String txt = task.getDescription().length() > 30
                    ? task.getDescription().substring(0, 27) + "..."
                    : task.getDescription();
            System.out.printf("%-4d %-10s %-30s %s%n",
                    task.getId(), status, txt, task.getCreatedAt().format(fmt));
        }

        long done = tasks.stream().filter(Task::isCompleted).count();
        System.out.println("\n📊 Wykonano: " + done + "/" + tasks.size());
    }

    private void markAsDone(String idStr) {
        try {
            int id = Integer.parseInt(idStr.trim());
            Optional<Task> opt = taskManager.findTaskById(id);
            if (opt.isEmpty()) {
                System.out.println("❌ Nie znaleziono zadania o ID " + id);
                return;
            }
            Task task = opt.get();
            if (task.isCompleted()) {
                System.out.println("ℹ️ Już wykonane.");
                return;
            }
            taskManager.markAsDone(id);
            System.out.println("🎉 Oznaczono jako wykonane: \"" + task.getDescription() + "\"");
        } catch (NumberFormatException e) {
            System.out.println("❌ Podaj poprawny numer ID!");
        }
    }

    private void removeTask(String idStr) {
        try {
            int id = Integer.parseInt(idStr.trim());
            if (taskManager.removeTask(id)) {
                System.out.println("🗑️ Usunięto zadanie ID " + id);
            } else {
                System.out.println("❌ Nie znaleziono zadania ID " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Podaj poprawny numer ID!");
        }
    }

    private void editTask(String input) {
        String[] parts = input.trim().split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            System.out.println("❌ Użycie: edit <id> <nowy tekst>");
            return;
        }

        try {
            int id = Integer.parseInt(parts[0]);
            if (taskManager.editTask(id, parts[1].trim())) {
                System.out.println("📝 Zmieniono zadanie ID " + id);
            } else {
                System.out.println("❌ Nie znaleziono zadania ID " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Podaj poprawny numer ID!");
        }
    }

    private void clearCompleted() {
        int removed = taskManager.clearCompleted();
        if (removed == 0) {
            System.out.println("ℹ️ Brak wykonanych zadań.");
        } else {
            System.out.println("🧹 Usunięto " + removed + " zadań");
        }
    }
}
