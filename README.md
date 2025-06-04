TODO App
Prosta aplikacja konsolowa do zarządzania listą zadań (TODO), napisana w Javie.

Jak działa?
Aplikacja pozwala na dodawanie, edytowanie, usuwanie i oznaczanie zadań jako wykonane. Wszystkie zadania są zapisywane do pliku tasks.txt i wczytywane przy starcie programu, dzięki czemu lista zadań jest zachowywana między uruchomieniami.

Funkcjonalności
add <zadanie> — dodaje nowe zadanie z opisem <zadanie>

list — wyświetla listę wszystkich zadań wraz ze statusem i datą utworzenia

done <id> — oznacza zadanie o podanym ID jako wykonane

remove <id> — usuwa zadanie o podanym ID

edit <id> <nowy tekst> — edytuje opis zadania o podanym ID

clear — usuwa wszystkie wykonane zadania z listy

help — wyświetla listę dostępnych komend

quit / exit — kończy działanie programu, zapisując zadania do pliku
Wymagania
Java 21
Uruchamianie:
Uruchamianie
Przejdź do folderu z .jar i uruchom komendę w terminalu:
java -jar TodoApp Aduxx-1.0.jar
