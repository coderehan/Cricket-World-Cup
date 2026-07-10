# Cricket World Cup - Flipkart Machine Coding Round Assessment

An Android app that simulates a 2-over cricket match between two user-selected teams, built as a machine coding assignment. The app follows MVVM + Clean Architecture principles with a strong focus on separation of concerns, testability, and extensibility.

---

## Problem Statement

ICC Cricket World Cup 2019 has already begun, and fans all over the world are eagerly waiting to find out who will be crowned World Champion. The task is to build an app that simulates a 2-over match between two teams to determine a winner.

### Screen 1 — Team Selection
- Display a list of teams along with their flags.
- Team data (name + flag URL) is bundled locally as a JSON file — no network call is required to fetch the list.
- The user must select exactly **2 teams** to proceed to the match screen.

### Screen 2 — Match Center
- Displays the live score of both teams.
- Both teams bat one after the other (first innings, then second innings).
- A **"Play Next Ball"** button simulates one delivery at a time. Each tap generates a random outcome and updates the batting team's score.
- Standard cricket rules apply to determine the winner:
  - The team with more runs at the end of 2 overs (12 balls) wins.
  - If the chasing team overtakes the target before the overs are up, the match ends immediately.
  - If a team loses 3 wickets, that team's innings ends early.

**Possible outcomes per ball:** `0`, `1`, `2`, `3`, `4`, `6`, `Out`

**Extensions implemented:**
- **Wide ball** — batting team gets 1 extra run and 1 extra ball (doesn't count toward the 12-ball limit).
- **No ball** — batting team gets 1 extra run and 1 extra ball, and cannot get out on that delivery.
- **Weighted probabilities** — sixes and wickets are intentionally rarer than dot balls and singles, for a more realistic simulation.

---

## Architecture

The app follows **MVVM (Model-View-ViewModel)** combined with **Clean Architecture** principles, split into three layers:

```
presentation/   → Composables, ViewModels, UI State
domain/         → Business rules, engine logic (pure Kotlin, no Android dependencies)
data/           → Models, Repository, DataSource
di/             → Hilt modules
```

### Why this structure?

- **`domain/`** contains zero Android framework dependencies. `MatchEngine`, `CricketRules`, and `RandomOutcomeGenerator` are plain Kotlin classes, which means all match-simulation logic can be unit tested without instrumentation, mocked Context objects, or Robolectric.
- **`data/`** doesn't know *how* the UI will use the data — `TeamRepository` is an interface, and `TeamRepositoryImpl` decides whether teams come from local assets, network, or a database. Swapping the data source later (e.g. moving to a live API) requires no ViewModel changes.
- **`presentation/`** holds one `ViewModel` per screen, each exposing a single immutable `UiState` via `StateFlow` — a single source of truth that Compose observes and reacts to automatically.

### Package structure

```
com.rehan.cricketworldcup
│
├── data
│   ├── datasource
│   │   └── TeamDataSource.kt         // Reads teams.json from assets
│   ├── model
│   │   ├── Team.kt
│   │   ├── Innings.kt
│   │   └── BallOutcome.kt            // Sealed class: Runs, Wicket, Wide, NoBall
│   └── repository
│       ├── TeamRepository.kt         // Interface
│       └── TeamRepositoryImpl.kt     // Caches teams in memory after first read
│
├── domain
│   ├── engine
│   │   └── MatchEngine.kt            // Applies an outcome to an Innings
│   ├── random
│   │   └── RandomOutcomeGenerator.kt // Weighted random ball outcomes
│   └── rules
│       └── CricketRules.kt           // Innings-over / target-chased logic
│
├── di
│   └── RepositoryModule.kt           // Hilt bindings
│
├── core
│   └── image
│       └── CricketImageLoader.kt     // Custom Coil ImageLoader config
│
├── presentation
│   ├── navigation
│   │   ├── Screen.kt
│   │   └── CricketNavGraph.kt
│   │
│   ├── teamselection
│   │   ├── TeamSelectionScreen.kt
│   │   ├── TeamSelectionViewModel.kt
│   │   ├── TeamSelectionUiState.kt
│   │   └── components
│   │       └── TeamItem.kt
│   │
│   └── match
│       ├── MatchScreen.kt
│       ├── MatchViewModel.kt
│       ├── MatchUiState.kt
│       └── components
│           ├── TeamScoreCard.kt
│           ├── BallResultView.kt
│           └── PlayBallButton.kt
│
├── CricketApplication.kt
└── MainActivity.kt
```

### Data flow (Team Selection → Match)

```
teams.json (assets)
      │
      ▼
TeamDataSource ──► TeamRepositoryImpl (caches result) ──► TeamRepository (interface)
      │                                                          │
      ▼                                                          ▼
TeamSelectionViewModel                                   MatchViewModel
      │                                                          │
      ▼                                                          ▼
TeamSelectionUiState (StateFlow)                        MatchUiState (StateFlow)
      │                                                          │
      ▼                                                          ▼
TeamSelectionScreen (Compose)                            MatchScreen (Compose)
```

Selected team **names** are passed via Navigation arguments (not full objects, since `NavHost` args must be simple types). `MatchViewModel` resolves the full `Team` objects (with flag URLs) by looking them up from the same cached repository — avoiding a second disk read.

### Match simulation flow

```
User taps "Play Next Ball"
      │
      ▼
MatchViewModel.playNextBall()
      │
      ▼
MatchEngine.playNextBall(currentInnings)
      │
      ├──► RandomOutcomeGenerator.nextBall()   // decides what happened
      │
      └──► returns (updatedInnings, outcome)
      │
      ▼
MatchViewModel updates MatchUiState
      │
      ▼
MatchEngine checks CricketRules:
      ├── isInningsOver()      → 12 balls bowled OR 3 wickets down
      └── hasChasedTarget()    → chasing team's runs > target (checked after every ball)
      │
      ▼
MatchScreen recomposes automatically via StateFlow
```

---

## Jetpack & Android Components Used

| Component | Purpose |
|---|---|
| **Jetpack Compose** | Entire UI is built declaratively — no XML layouts. |
| **Material 3** | `Card`, `Scaffold`, `Button`, `CircularProgressIndicator`, and the overall theming. |
| **ViewModel** (`androidx.lifecycle.viewmodel.compose`) | Holds and survives configuration changes for each screen's UI state. |
| **StateFlow** | Single source of truth for UI state; exposed as `StateFlow`, collected via `collectAsStateWithLifecycle()` for lifecycle-aware collection. |
| **Navigation Compose** | Handles navigation between Team Selection and Match screens, including passing selected team names as route arguments. |
| **Hilt** | Dependency injection across the app — `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@Inject constructor`, and `@Module`/`@Binds` for repository binding. |
| **hilt-navigation-compose** | Provides `hiltViewModel()` so ViewModels are scoped correctly to each navigation destination. |
| **Coil (Compose)** | Asynchronous flag image loading via `AsyncImage`, with a custom `ImageLoader` (backed by OkHttp) configured through `ImageLoaderFactory`. |
| **Coroutines** | `viewModelScope.launch(Dispatchers.IO)` for reading the bundled JSON off the main thread. |
| **Gson** | Parses `teams.json` from assets into `List<Team>`. |
| **Sealed classes** | `BallOutcome` models the finite set of delivery outcomes (`Runs`, `Wicket`, `Wide`, `NoBall`) in a type-safe, exhaustive way. |

---

## Key Design Decisions

- **Sealed class for `BallOutcome`** instead of an enum, since `Runs` needs to carry an associated value (the run count), while `Wicket`/`Wide`/`NoBall` don't.
- **Rules isolated in `CricketRules`** — extending the match format (e.g. 5 overs instead of 2, or 5 wickets instead of 3) requires changing exactly one file, with no ripple effect into the ViewModel or engine.
- **Weighted probability table in `RandomOutcomeGenerator`** — outcome frequencies are defined as simple integer ranges (0–99), making them easy to read, verify, and rebalance.
- **In-memory caching in `TeamRepositoryImpl`** — `MatchViewModel` needs full `Team` objects synchronously during construction (to resolve nav args), while `TeamSelectionViewModel` reads the same data asynchronously off the main thread. Caching after the first read avoids a redundant disk I/O call and keeps both access patterns safe.
- **No dialog for match result** — the winner is displayed directly inside the same result panel used for ball-by-ball outcomes, matching the reference UI, instead of interrupting with a popup.
- **Custom Coil `ImageLoader`** — flag images are hosted externally; a custom `OkHttpClient` interceptor is used to ensure requests succeed reliably across image hosts.

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Architecture:** MVVM + Clean Architecture
- **DI:** Hilt
- **Async:** Kotlin Coroutines + Flow
- **Image Loading:** Coil
- **JSON Parsing:** Gson
- **Navigation:** Navigation Compose
- **Min SDK:** 24 · **Target/Compile SDK:** 37

---

## Screenshots

<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/11c1f266-a4ff-4d3c-9564-18d7e460ccbf" />
<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/7ecd352d-ba28-4fc9-b523-884d7209ba9f" />
<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/8ffb8d68-1bdc-449a-a1a7-9ca08556e81d" />
<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/91323ac3-2d06-44e6-9036-b205282418eb" />
<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/a7fb2bf8-fe7f-415d-8da8-1ae4fd74d724" />
