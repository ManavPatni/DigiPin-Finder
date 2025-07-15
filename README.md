<p align="center">
  <img src="app/src/main/res/drawable/ic_logo.png" alt="DigiPin Finder Logo" width="200"/>
</p>

<h1 align="center">DigiPin Finder</h1>

<p align="center">
  <a href="https://github.com/ManavPatni/DigiPin-Finder/blob/main/LICENSE"><img src="https://img.shields.io/github/license/ManavPatni/DigiPin-Finder" alt="License"></a>
  <a href="https://android-arsenal.com/details/3/8665"><img src="https://img.shields.io/badge/Android%20Arsenal-DigiPin%20Finder-brightgreen.svg?style=flat" alt="Android Arsenal"></a>
  <img src="https://img.shields.io/badge/API-24%2B-blue.svg?style=flat" alt="API">
  <img src="https://img.shields.io/badge/Kotlin-1.9.0-blueviolet.svg?style=flat" alt="Kotlin">
  <img src="https://img.shields.io/badge/Compose-1.5.4-yellow.svg?style=flat" alt="Compose">
</p>

A Jetpack Compose app for finding DigiPins, a digital addressing system by the Department of Post, India. The app uses Google Maps and Places API for effortless searching of places in India.

## Features

*   **Find DigiPin:** Get the DigiPin for any location in India.
*   **Search Places:** Easily search for locations using Google Places API.
*   **Favorites:** Save your favorite DigiPins for quick access.
*   **QR Code Scanner:** Scan DigiPin QR codes to get location details.
*   **Share and Navigate:** Share DigiPins and get directions to the location.

## Screenshots

| Home Screen                                     | Find DigiPin                                      |
| ----------------------------------------------- | ------------------------------------------------- |
| <img src="screenshots/home.png" width="250"/>   | <img src="screenshots/find.png" width="250"/>     |
| **Favorites**                                   | **QR Scanner**                                    |
| <img src="screenshots/favorites.png" width="250"/> | <img src="screenshots/qr.png" width="250"/>       |

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/ManavPatni/DigiPin-Finder.git
    ```
2.  **Open in Android Studio:**
    Open the cloned repository in Android Studio.
3.  **Add Google Maps API Key:**
    Get a Google Maps API key from the [Google Cloud Console](https://console.cloud.google.com/google/maps-apis/overview).
    Add your API key to the `local.properties` file:
    ```
    MAPS_API_KEY=YOUR_API_KEY
    ```
4.  **Build and Run:**
    Build and run the app on an Android emulator or a physical device.

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## Contact

Manav Patni - [@manavpatni](https://www.linkedin.com/in/manavpatni/)
