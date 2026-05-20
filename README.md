# Foodsharing App

A modern, community-driven Android application designed to facilitate food sharing and reduce waste. Built with a focus on sustainability and ease of use, the Foodsharing App connects people who have surplus food with those who can use it.

## 🌿 Core Features

- **Nearby Baskets**: Discover available food baskets in your local area using location-based services. View detailed information about food items, quantity, and collection windows.
- **Smart Pickups**: Manage your food collections with an integrated pickup tracker. Includes automated reminders so you never miss a scheduled sharing event.
- **Community Messaging**: Coordinate pickups and discuss sharing details through a dedicated in-app messaging system.
- **Real-time Notifications**: Stay updated with background synchronization for new messages, nearby basket alerts, and pickup reminders.
- **Customizable Experience**: Configure server settings and manage your profile to tailor the app to your community's needs.

## 🎨 Design Philosophy: "Sustainable Community"

The app features a **Soft Minimalist** aesthetic rooted in nature-inspired tones:
- **Primary Palette**: Forest Green for growth, Mint for freshness, and Warm White for a comfortable reading experience.
- **Soft Geometry**: Extensive use of rounded corners (20px radius) and pill-shaped components to create an approachable, friendly environment.
- **Modern Typography**: Powered by the **Outfit** font family for high legibility and a contemporary feel.

## 🛠 Tech Stack

- **Language**: 100% [Kotlin](https://kotlinlang.org/)
- **UI Framework**: 
    - [Material Components](https://material.io/develop/android) for Android
    - ViewBinding for type-safe view interaction
    - Jetpack Navigation Component for seamless flow
- **Networking & Data**:
    - [Retrofit](https://square.github.io/retrofit/) & OkHttp for API communication
    - [Moshi](https://github.com/square/moshi) for JSON serialization
    - [Glide](https://github.com/bumptech/glide) for efficient image loading
    - [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) for persistent settings and session management
- **Architecture**:
    - MVVM (Model-View-ViewModel) pattern
    - Coroutines & Flow for asynchronous programming
    - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for robust background tasks (Syncing, Reminders)
- **Services**:
    - Google Play Services Location for proximity features

## 🚀 Getting Started

### Prerequisites
- Android Studio Jellyfish (or newer)
- Android SDK 34 (Upside Down Cake) or higher
- A compatible backend server (URL configurable in App Settings)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/FoodsharingApp.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the `app` module on an emulator or physical device (Min SDK: 34).

## 📂 Project Structure
- `ui/`: Fragment and ViewModel implementations organized by feature (baskets, pickups, conversations).
- `data/`: API definitions, repositories, and data models.
- `worker/`: Background jobs for data synchronization and notifications.
- `util/`: Helper classes for authentication, settings, and UI components.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](https://mit-license.org/) file for details.
