# Pos Terminal App

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
| Login Page | Categories Page | Shopping Cart Page |
|-------------------|-------------------|-------------------|
| <img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/19c5304a-e5c8-494a-a2d3-8683cc841f96" width=250> | <img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/743a168c-343f-4f64-aa4e-7affd17f7cd2" width=250> | <img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/e376beb5-d3bb-4ddb-90cc-f07e5bf20502" width=250> |

| Adding Cashier Page | Cashiers Page | Customer Information Page |
|-------------------|-------------------|-------------------|
| <img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/bd73dc97-e307-42bd-8b24-992214f4f84b" width=250> | <img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/ba00c2dc-9f59-451a-b23d-672d3cefd7be" width=250> | <img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/3071684c-a9a6-4a3e-96f1-901fb2b8d871" width=250> |

## Demo Video

https://github.com/CampusCoders/PosTerminalApp/assets/78374444/ddb5e77a-9ba4-4a92-8a35-846cb6986e7d

## Designs

You can access the figma design [here](https://www.figma.com/file/bBP1Pn46TUZsndBbc3qRub/PosTerminalApp).

![app](https://github.com/CampusCoders/PosTerminalApp/assets/78374444/b621ed53-52e0-49b4-b07c-dcfc3f006feb)

## Database Schema

![image](https://github.com/CampusCoders/PosTerminalApp/assets/78374444/e0a8ec5c-ea47-41f1-a5c0-e119df9d1221)

## Sample Receipts

<img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/167e0d8f-fad9-4d17-85f8-b94fc4fe3aa0" width=250>
<img src="https://github.com/CampusCoders/PosTerminalApp/assets/78374444/9fca1b75-e8c4-4f2f-b870-326ddcdf5d43" width=250>

## Authors

- Hasan Ali Çalışkan [Github](https://github.com/hasanalic)
- Çağrı Ömer Asan [Github](https://github.com/cagriiasan)

## License

This project is licensed under the MIT License. For more information, please see the [LICENSE file](LICENSE).

## Important Notes
1. The application requires users to perform an initial login on the first screen. The following information is needed for this process:

- Terminal Identity Number (Terminal ID)
- Turkish ID Number or Tax ID Number (TCKN or VKN)
- Merchant Identification Number (Merchant ID)
- Password
  
Typically, these details are provided by the distributor for each merchant. In the case of this mobile application example, the necessary information is stored statically in the Constants.kt file. You can modify these details if needed. To input the information easily on the screen, you can click the "test" radio button.

2. The SDK for the POS device is not included in the project due to licensing issues.
