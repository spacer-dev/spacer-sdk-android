# SpacerSDK

![code](https://img.shields.io/badge/kotlin-1.4.32-blue)
![platforms](https://img.shields.io/badge/android-6.0%2B-blue)
![bluetooth](https://img.shields.io/badge/bluetooth-4.2%2B-brightgreen)
![license](https://img.shields.io/github/license/spacer-dev/spacer-sdk-android)


Provides operations for using the SPACER locker.

For more information, see [docs](https://rogue-flight-1e9.notion.site/SPACER-API-5d3f6b8831be484e94497ac822099270)

## Features

### 1. CB Locker Service

Provides locker operation using BLE

- Scan lockers
- Deposit your luggage in the locker
- Take your luggage out of the locker

### 2. My Locker Service

Provides operation of the locker you are using

- Get a locker in use
- Reserve an available locker
- Cancel the reserved locker
- Share your locker in use

### 3. SPR Locker Service

Provides basic locker information

- Get multiple locker basic information
- Get multiple locker unit basic information

### 4. Location Service

Provides basic location information

- Get multiple unit location basic information
  
## Requirement

- Android OS 6.0+
- API Level 23+
- Bluetooth Low Energy (BLE) 4.2+

## Usage

### Add to AndroidManifest.xml

Please add INTERNET usage to access API with SDK

```
<uses-permission android:name="android.permission.INTERNET" />
```

### Request bluetooth permissions

Please request for the following two permissions　

- Manifest.permission.BLUETOOTH
- Manifest.permission.ACCESS_FINE_LOCATION　


### 1. CB Locker Service

```kotlin
val service = SPR.cbLockerService()

// Scan lockers
service.scan(
    context,
    token,
    object : IResultCallback<List<SPRLockerModel>> {
        override fun onSuccess(result: List<SPRLockerModel>) {}
        override fun onFailure(error: SPRError) {}
    })

// Deposit your luggage in the locker
service.put(
    context,
    token,
    spacerId,
    object : ICallback {
        override fun onSuccess() {}
        override fun onFailure(error: SPRError) {}
    })

// Take your luggage out of the locker  
service.take(
    context,
    token,
    spacerId,
    object : ICallback {
        override fun onSuccess() {}
        override fun onFailure(error: SPRError) {}
    })  

```

### 2. My Locker Service

```kotlin
val service = SPR.myLockerService()

// Get lockers in use
service.get(
    token,
    object : IResultCallback<List<MyLockerModel>> {
        override fun onSuccess(result: List<MyLockerModel>) {}
        override fun onFailure(error: SPRError) {}
    })

// Reserve an available locker
service.reserve(
    token,
    spacerId,
    object : IResultCallback<MyLockerModel> {
        override fun onSuccess(result: MyLockerModel) {}
        override fun onFailure(error: SPRError) {}
    })

// Cancel the reserved locker
service.reserveCancel(
    token,
    spacerId,
    object : ICallback {
        override fun onSuccess() {}
        override fun onFailure(error: SPRError) {}
    })

// Share locker with URL key
service.shareUrlKey(
    token,
    spacerId,
    object : IResultCallback<MyLockerModel> {
        override fun onSuccess(result: MyLockerModel) {}
        override fun onFailure(error: SPRError) {}
    })
```

### 3. SPR Locker Service

```kotlin
val service = SPR.sprLockerService()

// Get multiple locker basic information
service.getLockers(
    token,
    spacerIds,
    object : IResultCallback<List<SPRLockerModel>> {
        override fun onSuccess(result: List<SPRLockerModel>) {}
        override fun onFailure(error: SPRError) {}
    })        

// Get multiple locker unit basic information
service.getUnits(
    token,
    unitIds,
    object : IResultCallback<List<SPRLockerUnitModel>> {
        override fun onSuccess(result: List<SPRLockerUnitModel>) {}
        override fun onFailure(error: SPRError) {}
    })     
```

### 4. Location Service

```kotlin
val service = SPR.locationService()

// Get multiple unit location basic information
service.get(
    token,
    locationId,
    object : IResultCallback<LocationModel> {
        override fun onSuccess(result: LocationModel) {}
        override fun onFailure(error: SPRError) {}
    })    
```

## Example

You can check the operation of Spacer SDK in the app project. see [example code](https://github.com/spacer-dev/spacer-sdk-android/tree/main/app)    

### How to use

1. Open the `app` project
2. Set the values for `sdk.baseUrl` and　`sdk.token` in local.properties  

```
#local.properties
sdk.baseUrl=https://ex-app.spacer.co.jp
sdk.token=
```

3. Build and Run on the android

### About `sdk.token`

Originally, the token issued by SPACER obtained from your server is set for testing.

How to get `sdk.token`, see [docs](https://rogue-flight-1e9.notion.site/SPACER-API-5d3f6b8831be484e94497ac822099270)


## Installation

Added to build.gradle of Android application project

```
repositories {
    maven { url 'https://raw.githubusercontent.com/spacer-dev/spacer-sdk-android/main/repository'}
}

dependencies {
    implementation 'com.spacer:sdk:1.1.0'  
} 
```

### License
This software is released under the MIT License, see [LICENSE.](https://github.com/spacer-dev/spacer-sdk-android/blob/main/LICENSE)
