<img src="https://github.com/Veriff/android-sample-app/blob/master/veriff-logo.png" width="300">

# Veriff Android SDK and sample app.

### Table of contents
* [Overview](#overview)
* [Adding the SDK](#adding-the-sdk)
* [Starting the verification flow](#starting-the-verification-flow)
* [Get the verification status](#get-the-verification-status)
    * [In the activity](#using-onactivityresult)
    * [Using a background service](#using-background-service)
    * [Using a broadcast receiver](#using-broadcast-receiver)
* [Handle result](#handle-result)
* [Adding logging](#adding-logging)
* [Go live](#go-live)
* [Releases](#releases)
* [Upgrading from SDK 1.* to 2.+](#upgrading-sdk)

## Overview

## Adding the sdk

Add two new maven repository destination under the root build.gradle repositories tag in allprojects bracket. It should contain the following two maven repositories:
```gradle
    allprojects {
        repositories {
            maven { url "http://dl.bintray.com/argo/sdk" } //probity
            maven { url "https://cdn.veriff.me/android/" } //veriff
            google()
            jcenter()

        }
    }
```  
Add two dependency imports in the application build.gradle dependency list:
```gradle
    implementation 'com.veriff:veriff-library:2.1.1'
    implementation 'io.probity.sdk:collector:1.0.0'
```    
## Starting the verification flow

The verification process must be launched inside Vendor specific Activity class. The Activity class is aware of the current customer, who is either logged in via the Vendor system or who is identified other way with unique system pass. The Vendor must be able to determine the customer later, if application ends/ or returns. It depends entirely on the Vendor business logic.

Every Veriff SDK session is unique for a client. The session expires after 7 days automatically, but is valid until then. After the verification data is uploaded, the SDK v1.0 does not wait for the final verification result (async). The SDK v1.0 only notifies whether the verification data upload is successful or not.


In Vendor Activity class - define the result code and initialize the SDK class (required):

```java
    Veriff.Builder veriffSDK = new Veriff.Builder(baseUrl, sessionToken);
    veriffSDK.launch(MainActivity.this, REQUEST_VERIFF);
```

## Get the verification status

The verification result is sent to the Vendor server in the background. ( Reference for that is the API Spec document ). Veriff SDK sends callbacks to vendor mobile application via background service, onActivityResult and broadcasts. The most reliable callback method is the background service because it’s usually waken up by the Android system services. All the mentioned ways return the same status codes so the vendor application developer can choose the preferred method and ignore the other ones.

### Using onActivityResult

For catching the SDK returning status in the activity override onActivityResult in vendor activity that starts the SDK:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   if (requestCode == REQUEST_VERIFF && data != null) {
       int statusCode = data.getIntExtra(VeriffConstants.INTENT_EXTRA_STATUS, Integer.MIN_VALUE);
       String sessionToken = data.getStringExtra(VeriffConstants.INTENT_EXTRA_SESSION_URL);
       
       handleResult(statusCode, sessionToken); //see below to know how to handle result
   }
   super.onActivityResult(requestCode, resultCode, data);
}
```

### Using background service

```java
public class VeriffSDKStatusUpdatesService extends VeriffStatusUpdatesService {
   @Override
   protected void onStatusChanged(String sessionToken, int statusCode) {
       handleResult(statusCode, sessionToken); //see below to know how to handle result
   }
}
```

After the service is set up then it needs to be declared in the application manifest:

```xml
<!-- Implement Veriff SDK updates service -->
<service android:name=".service.VeriffSDKStatusUpdatesService">
   <intent-filter>
       <action android:name="me.veriff.STATUS_UPDATE" />
   </intent-filter>
</service>
```

### Using broadcast receiver

Create a BroadcastReceiver which will listen to Veriff SDK broadcasts:

```java
public class VeriffStatusReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
       Bundle extras = intent.getExtras();
       if (extras.containsKey(VeriffConstants.INTENT_EXTRA_STATUS)) {
           int statusCode = data.getIntExtra(VeriffConstants.INTENT_EXTRA_STATUS, Integer.MIN_VALUE);
           String sessionToken = extras.getString(VeriffConstants.INTENT_EXTRA_SESSION_URL);
           
           handleResult(statusCode, sessionToken); //see below to know how to handle result         
       }
   }
}
```

Update the Manifest.xml with the new BroadcastReceiver and add a separate permission for it:

```xml
<!-- Implement you own BroadcastReceiver to track VeriffSDK status, should be protected by "signature" permission -->
<receiver
android:name=".receiver.VeriffStatusReceiver"
android:permission="${applicationId}.VERIFF_STATUS_BROADCAST_PERMISSION">
   <intent-filter>
       <category android:name="${applicationId}" />
       <action android:name="veriff.info.status" />
   </intent-filter>
</receiver>
```

## Handle result

```java
public void handleResult(int statusCode, String sessionToken){
       if (statusCode == VeriffConstants.STATUS_USER_FINISHED) {
           //user finished whatever he/she was asked to do, there might be other callbacks coming after this one (for example if the images are still being uploaded in the background)
       } else if (statusCode == VeriffConstants.STATUS_ERROR_NO_IDENTIFICATION_METHODS_AVAILABLE) {
           //there are no identifications methods currently available
       } else if (statusCode == VeriffConstants.STATUS_ERROR_SETUP) {
           //issue with the provided vendor data
       } else if (statusCode == VeriffConstants.STATUS_ERROR_UNKNOWN) {
           //unidentified error
       } else if (statusCode == VeriffConstants.STATUS_ERROR_NETWORK) {
           //network unavailable
       } else if (statusCode == VeriffConstants.STATUS_USER_CANCELED) {
           //user closed SDK
       } else if (statusCode == VeriffConstants.STATUS_UNABLE_TO_ACCESS_CAMERA) {
           //we are unable to access phone camera (either access denied or there are no usable cameras)
       } else if (statusCode == VeriffConstants.STATUS_UNABLE_TO_RECORD_AUDIO) {
           //we are unable to access phone microphone
       } else if (statusCode == VeriffConstants.STATUS_SUBMITTED) {
           //SelfID photos were successfully uploaded
       } else if (statusCode == VeriffConstants.STATUS_OUT_OF_BUSINESS_HOURS) {
           //call was made out of business hours, there were no verification specialists to handle the request
       } else if (statusCode == VeriffConstants.STATUS_ERROR_SESSION) {
           //invalid sessionToken was passed to the SDK
       } else if (statusCode == VeriffConstants.STATUS_DONE) {
           //verification specialist declined the session
       }
}
```

## Adding logging

## Go Live

## Releases

## Upgrading SDK

