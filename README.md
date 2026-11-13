# Premiere 🎞️

A movie browser app built with **Kotlin Multiplatform** and **Compose Multiplatform** that runs on Android, iOS and Desktop. It loads movies from a REST API and lets you search, filter and explore movie details.

> Built as an assignment for *Mobile Application Development* (RMA) at the School of Computing (RAF), Union University. It is the predecessor of my larger [Showtime](https://github.com/nikola-vasiljevic04/showtime) project.


## Features

- **Movie list**: paginated list sorted by IMDb rating
- **Search & filters**: by title, genre, release year range and minimum rating
- **Movie details**: hero image, overview, key information, cast, image gallery and trailer links

## Architecture

The project follows **Clean Architecture**, with a clear split between layers:

```
data/          API definitions (Ktorfit), DTOs, repository implementation
domain/        Repository interface and use cases
               (GetMoviesUseCase, GetMovieDetailsUseCase, GetGenresUseCase)
presentation/  Compose screens + ViewModels with a Contract (state/events) per screen
di/            Koin modules
```

## Tech stack

- Kotlin Multiplatform, Compose Multiplatform, Material 3, Navigation
- Ktor client + Ktorfit, kotlinx.serialization
- Koin (dependency injection)
- Coil 3 (image loading)
- Kotlin Coroutines & StateFlow

## Running the app

**Android**
```bash
./gradlew :composeApp:assembleDebug
```

**Desktop (JVM)**
```bash
./gradlew :composeApp:run
```

**iOS**: open `iosApp` in Xcode and run it.
