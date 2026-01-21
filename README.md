# Human Readable Duration

[![CI](https://github.com/aporkolab/human-readable-duration/actions/workflows/ci.yml/badge.svg?branch=master&v=1)](https://github.com/aporkolab/human-readable-duration/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-25-blue)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Porkoláb Ádám**

Codewars [4 kyu kata](https://www.codewars.com/kata/52742f58faf5485cae000b9a) megoldás Java nyelven.

---

## A feladat

Másodpercekben megadott időtartamot alakít ember által olvasható formátumba.

```
0        → "now"
62       → "1 minute and 2 seconds"
3662     → "1 hour, 1 minute and 2 seconds"
15731080 → "182 days, 1 hour, 44 minutes and 40 seconds"
```

### Követelmények

- Nulla esetén `"now"` a kimenet
- Időegységek: year (365 nap), day (24 óra), hour, minute, second
- Egyes szám: `"1 second"`, többes szám: `"2 seconds"`
- Elválasztás: vessző, az utolsó két elem között `" and "`
- Nulla értékű komponensek nem jelennek meg
- Nagyobb egység mindig előbb áll

---

## Megoldás

### Architektúra

A megoldás a **Single Responsibility** elvet követi - minden komponensnek egy feladata van:

```
TimeFormatter.formatDuration(seconds)
        │
        ├── extractTimeParts()    → Másodpercek felbontása komponensekre
        │         │
        │         └── TimeUnit    → Enum az időegységekhez (érték + formázás)
        │
        └── joinWithCommasAndAnd() → Komponensek összekapcsolása szabály szerint
```

### Tervezési döntések

| Döntés | Indoklás |
|--------|----------|
| **Enum az időegységekhez** | Nincs magic number, az egység és a formázás egy helyen |
| **Konstansok a másodpercekhez** | `SECONDS_PER_DAY` olvashatóbb mint `86400` |
| **Utility class pattern** | `final class` + private konstruktor, nem példányosítható |
| **Immutable műveletek** | A `joinWithCommasAndAnd()` nem módosítja a bemeneti listát |
| **Input validáció** | Negatív szám esetén `IllegalArgumentException` |
| **Javadoc** | A publikus API dokumentált, példákkal |

---

## Használat

```java
import com.example.duration.TimeFormatter;

// Alap használat
TimeFormatter.formatDuration(0);         // "now"
TimeFormatter.formatDuration(1);         // "1 second"
TimeFormatter.formatDuration(62);        // "1 minute and 2 seconds"
TimeFormatter.formatDuration(3662);      // "1 hour, 1 minute and 2 seconds"

// Nagyobb értékek
TimeFormatter.formatDuration(132030240); // "4 years, 68 days, 3 hours and 4 minutes"

// Hibakezelés
TimeFormatter.formatDuration(-1);        // IllegalArgumentException
```

---

## Futtatás

### Követelmények

- Java 21+
- Maven 3.6+

### Tesztek futtatása

```bash
mvn clean test
```

### Build + Checkstyle

```bash
mvn clean verify
```

---

## Projekt struktúra

```
human-readable-duration/
├── .editorconfig              # Kódformázási szabályok
├── .github/
│   └── workflows/
│       └── ci.yml             # GitHub Actions CI
├── checkstyle.xml             # Checkstyle szabályok
├── pom.xml                    # Maven konfiguráció
├── README.md
└── src/
    ├── main/java/com/example/duration/
    │   └── TimeFormatter.java        # Fő implementáció
    └── test/java/com/example/duration/
        └── TimeFormatterTest.java    # 26 teszt eset
```

---

## Tesztelés

A tesztek JUnit 5 keretrendszerrel készültek, a következő területeket fedik le:

| Kategória | Leírás |
|-----------|--------|
| **Kata examples** | Az eredeti feladat összes példája |
| **Singular units** | Egyes szám minden időegységnél |
| **Plural units** | Többes szám minden időegységnél |
| **Boundary values** | Határértékek (59→60→61 mp) |
| **All units combined** | Minden időegység egyszerre |
| **Input validation** | Negatív számok kezelése |

### Teszt kimenet

```
[INFO] Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Minőségbiztosítás

| Eszköz | Leírás |
|--------|--------|
| **Checkstyle** | Google Java Style alapú kódellenőrzés, build-be integrálva |
| **GitHub Actions** | Automatikus CI minden push és PR esetén |
| **EditorConfig** | Konzisztens formázás IDE-k között |

---

## Technológiák

- **Java 21** - `List.getFirst()`, `List.getLast()`
- **Maven** - Build és dependency management
- **JUnit 5** - `@ParameterizedTest`, `@Nested`, `@DisplayName`
- **Checkstyle** - Statikus kódelemzés
- **GitHub Actions** - CI/CD

---

## Clean Code elvek

- **No magic numbers** - Konstansok és enum értékek
- **Single Responsibility** - Számítás és formázás szétválasztva
- **Immutability** - Lista nem módosul a join során
- **Defensive programming** - Input validáció
- **Self-documenting code** - Beszédes metódus és változónevek
- **DRY** - `@ParameterizedTest` a hasonló teszteknél

---

## Licensz

MIT License - szabadon felhasználható.
