# Pos Terminal App

![image]()

It is a sample Android mobile application designed to be used on Android Pos Terminals in sales workplaces.

## Features
- The application was developed with MVVM design pattern.
- There are two types of users in the application: Manager and Cashier (Manager is also a cashier with all powers.)
- Users can log in to the system and carry out their transactions based on their accounts.
- Users can change their passwords.
- Sales transactions can be made and products can be offered to customers.
- New products and categories can be easily added.
- Business owners (managers) or cashiers (those with authority) can add new cashiers to the system.
- It is possible to view past orders and filter orders according to the desired criteria.
- When the credit card payment option is chosen, the payment transaction is made by card via the POS device.
- Once the payment process is completed, a physical receipt is automatically generated.
- The receipt content includes customer information, order information and the logo of the application.

## Libraries

![image]()

--------------
* [Foundation]
  * [Android KTX](https://developer.android.com/kotlin/ktx) -  It is used to make the application more readable and easier to use.
  * [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat) -  It is a library that ensures Android applications are compatible with material design.
* [Architecture]
  * [Room](https://developer.android.com/jetpack/androidx/releases/room) -  It is a library used to create and manage local databases.
  * [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) -  It is used to facilitate lifecycle management of activities and fragments.
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - It is used to manage and monitor data flow.
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - It is used for storing data and sharing data between UI components.
* [UI]
  * [Animations & Transitions](https://developer.android.com/develop/ui/views/animations) - It is used to use animations in transitions between screens.
  * [Fragment](https://developer.android.com/guide/fragments) - It is used as a reusable UI component.
  * [Layout](https://developer.android.com/develop/ui/views/layout/declaring-layout) -  Widgets are used to design the user interface.
* [Third Party]
  * [Glide](https://github.com/bumptech/glide) - It is used for image uploading operations.
  * [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - It is used for asynchronous operations.
  * [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - It is used to perform Dependency Injection.
  * [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences) - It is used to save small data.
  * [Barcode Scanner](https://github.com/yuriy-budiyev/code-scanner) - It is used to scan barcodes.

## Setup
Make sure to have the following pre-requisites installed:
1. Java 17
2. Android Studio
3. Android 7.0+ Phone or Emulation setup

Fork and clone this repository and import into Android Studio
```bash
git clone https://github.com/CampusCoders/PosTerminalApp
```

Use one of the Android Studio builds to install and run the app in your device or a simulator.

## Screenshots

![image]()

## Usage

video

## Designs
You can access the Figma design of the application [here]().

![image]()

## Database Schema

![image](https://github.com/CampusCoders/PosTerminalApp/assets/78374444/58c6dd0f-01ee-4be6-88de-de8b5ef56513)

## Sample Receipts

![image]()

## Authors

- Hasan Ali Çalışkan [Github](https://github.com/hasanalic)
- Çağrı Ömer Asan [Github](https://github.com/cagriiasan)

## License



## Important Notes
