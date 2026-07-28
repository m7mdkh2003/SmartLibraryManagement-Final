# Smart Library Management

An Android/Java final course project for a two-student team. The project expands the original Library Management application into a local-and-cloud smart library and covers all course topics.

## Implemented requirements

- 16 Activity screens and 4 practical Fragments.
- RecyclerView and ListView with a custom adapter.
- Shared Preferences with Remember Me.
- Background Threads through a Singleton `AppExecutors` manager.
- SQLite CRUD and transactional borrowing/returning.
- MVC, MVP and MVVM modules.
- Firebase Authentication and Cloud Firestore when configured.
- Local SQLite fallback when Firebase is not configured.
- Open Library Web Service with HTTP GET and JSON parsing.
- GitHub-ready documentation and a YouTube explanation script.

## Architecture

- **MVC:** Local Books module.
- **MVP:** Members module.
- **MVVM:** Online Books module.
- Additional patterns: Adapter, Repository, Singleton and Observer.

## Run the project

1. Open the root folder in Android Studio.
2. Allow Gradle Sync to download dependencies.
3. Run on an Android device/emulator with Android 7.0 or newer.
4. The app works immediately in SQLite local mode.
5. For cloud mode, follow [FIREBASE_SETUP.md](FIREBASE_SETUP.md).

## Web service

Online Books uses the public Open Library Search API. No API key is required. Internet permission is already present in the manifest.

## Documentation

- [Project completion and screen distribution](PROJECT_COMPLETION_REPORT.md)
- [Firebase setup](FIREBASE_SETUP.md)
- [YouTube video script](YOUTUBE_VIDEO_SCRIPT.md)

## Important test flow

Register → Login → Add Book → Add Member → Reserve Book → View Reserved Books → Return Book → Search Online Books → Save Favorite.
