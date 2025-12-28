# Anime Trailers

An Android application that displays top-rated anime series with trailers, built using Clean Architecture principles. The app fetches data from the Jikan API and provides offline support with automatic synchronization.

## 📱 Features

### Core Features

1. **Anime List Display**
   - Displays top-rated anime series in a paginated RecyclerView
   - Shows anime title, number of episodes, rating, genres, and poster image
   - Infinite scroll pagination with loading indicators
   - Pull-to-refresh support

2. **Anime Detail Page**
   - Comprehensive anime information display
   - Video trailer playback (YouTube or direct video URLs)
   - Poster image display
   - Synopsis, genres, episodes, and rating information
   - Navigation between list and detail screens

3. **Offline Support**
   - Local database storage using Room
   - Automatic data synchronization when online
   - Offline-first approach with RemoteMediator pattern
   - Network connectivity monitoring
   - Video playback disabled when offline (with user notification)

4. **Video Playback**
   - YouTube video integration using YouTube Player API
   - ExoPlayer support for direct video URLs
   - Fallback to external browser/app for video playback
   - Error handling with graceful fallbacks

## 🏗️ Architecture

The project follows **Clean Architecture** principles with clear separation of concerns:

### Layers

1. **Presentation Layer** (`presentation/`)
   - UI components (Fragments, Adapters)
   - ViewModels for state management
   - UI state classes

2. **Domain Layer** (`domain/`)
   - Business logic and use cases
   - Domain models
   - Repository interfaces

3. **Data Layer** (`data/`)
   - Data sources (Remote and Local)
   - Repository implementations
   - DTOs (Data Transfer Objects)
   - Mappers (DTO ↔ Domain ↔ Entity)
   - Room database entities and DAOs

### Key Components

- **Dependency Injection**: Hilt for managing dependencies
- **Networking**: Retrofit with Moshi for JSON parsing
- **Local Storage**: Room database with Paging 3 integration
- **Image Loading**: Glide for efficient image loading
- **Navigation**: Android Navigation Component
- **Pagination**: AndroidX Paging 3 with RemoteMediator

## 📦 Dependencies

### Core Libraries

- **Room**: 2.7.0 - Local database
- **Retrofit**: 2.11.0 - HTTP client
- **Moshi**: 1.15.1 - JSON parsing
- **Hilt**: 2.51.1 - Dependency injection
- **Paging**: 3.3.2 - Pagination support
- **ExoPlayer**: 1.4.1 - Video playback
- **Glide**: 4.16.0 - Image loading
- **Navigation**: 2.8.4 - Navigation component
- **YouTube Player**: 13.0.0 - YouTube video integration

## ⚠️ Limitations

### YouTube Video Playback Limitations

1. **Internet Connection Required**
   - YouTube videos **cannot** be played when the device is offline
   - The app detects offline status and disables video playback buttons
   - Users receive a toast notification: "Video playback requires an internet connection"

2. **YouTube API Restrictions**
   - Uses YouTube Player API (not YouTube Data API v3)
   - Videos are embedded using the YouTube Player library
   - Some videos may be restricted by YouTube's content policies
   - Region-restricted videos may not play in certain locations

3. **Video Availability**
   - Not all anime have trailers available
   - Some trailers may be removed or made private by content creators
   - The app falls back to poster image if no trailer URL is available

4. **External Video Links**
   - When YouTube player fails, the app attempts to open videos externally
   - External links also require internet connection
   - Opening external links is disabled when offline

5. **Direct Video URLs**
   - ExoPlayer supports direct video URLs (HTTP/HTTPS)
   - These also require internet connection
   - Not all video formats may be supported

### General Limitations

1. **API Rate Limiting**
   - Jikan API has rate limits (4 requests/second)
   - The app implements pagination to minimize API calls
   - Excessive requests may result in temporary blocking

2. **Data Synchronization**
   - Data syncs automatically when the app comes online
   - Manual refresh clears local cache and fetches fresh data
   - First-time users need internet connection to load data

3. **Image Caching**
   - Images are cached by Glide
   - Cache may be cleared by the system when storage is low
   - Poster images require internet for initial load

## 🔄 Offline Functionality

### What Works Offline

- ✅ Viewing cached anime list
- ✅ Viewing cached anime details
- ✅ Scrolling through paginated list (if previously loaded)
- ✅ Navigation between screens
- ✅ Viewing cached poster images

### What Requires Internet

- ❌ Playing video trailers
- ❌ Opening external video links
- ❌ Fetching new anime data
- ❌ Loading new images
- ❌ Syncing data with server

## 🔧 Technical Details

### Data Flow

1. **List Screen**:
   - `AnimeViewModel` → `AnimeRepository` → `AnimeRemoteMediator` + `AnimeLocalDataSource`
   - RemoteMediator fetches data from API and stores in Room
   - Room PagingSource provides data to UI
   - UI observes PagingData flow

2. **Detail Screen**:
   - `AnimeDetailViewModel` → `GetAnimeDetailsUseCase` → `AnimeRepository`
   - Checks local database first
   - Fetches from API if not found locally and online
   - Saves to local database after fetching

### Network Monitoring

- `NetworkMonitor` class monitors connectivity status
- Uses ConnectivityManager to detect network changes
- Provides Flow-based network status updates
- Used to disable video playback when offline

### Database Schema

- **AnimeEntity**: Stores anime information
- **RemoteKey**: Tracks pagination state for RemoteMediator
- Type converters for List<String> (genres)

## 🐛 Known Issues

- YouTube videos may not play if YouTube app is not installed (falls back to browser)
- Some video URLs from API may be malformed or invalid
- Large image files may cause memory issues on low-end devices


## 🙏 Acknowledgments

- [Jikan API](https://jikan.moe/) for providing free anime data
- Android Jetpack libraries for robust app architecture
- Open source community for excellent libraries

