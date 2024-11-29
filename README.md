# Adwentuj z Kotlinem 2024 - Native Template

## Struktura
Każde zadanie przewidziane jest jako osobny moduł Gradle, z którego budowany będzie docelowo uruchamiany artefakt.

Domyślnie skonfigurowano dwa targety buildu:
- `macosArm64` - do lokalnego uruchamiania na systemach MacOS operujących na architekturze ARM
- `linuxX64` - do uruchamiania na docelowej maszynie pomiarowej

Jeżeli zadania będą rozwiązywane na innej platformie niż MacOS, proszę sprecyzować dodatkowy target (np. `mingwX64("windows")`) w plikach `day[1-7]/build.gradle.kts`.

W każdym module, w katalogu `src/commonMain/resources` powinien znaleźć się input na dany dzień zadania.

W każdym module, w katalogu `src/commonMain/kotlin` znajduje się plik `Day[1-7].kt`, w którym należy zaimplementować rozwiązania - odpowiednio w funkcjach `part1` oraz `part2`. Rezultat obliczeń z każdej części powinien być zwracany na `stdout`.

## Kompilacja
Ostatecznym produktem kompilacji powinien być natywny artefakt zbudowany przy pomocy polecenia `./gradlew assemble`, który następnie można poprawnie uruchomić poprzez komendę `./app-[platform].kexe` bez przekazywania dodatkowych argumentów.

Aby sprawdzić, czy skompilowany program na pewno się wykonuje, należy przejść do modułu danego dnia i wywołać polecenie:
```shell
cd day1 &&
./build/bin/macos/releaseExecutable/app-macos.kexe
```
