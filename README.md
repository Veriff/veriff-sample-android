
<img src="https://github.com/Veriff/android-sample-app/blob/master/veriff-logo.png" width="300">

# Mobile SDK Documentation for Android

### Table of contents
* [Overview](#overview)
* [Adding the SDK](#adding-the-sdk)
* [Starting the verification flow](#starting-the-verification-flow)
* [Getting the verification status](#getting-the-verification-status)
    * [In the activity](#in-the-activity)
    * [Using a background service](#using-a-background-service)
    * [Using a broadcast receiver](#using-a-broadcast-receiver)
* [Handling the result](#handling-the-result)
* [Adding error logging](#adding-error-logging)
* [Proguard rules](#proguard-rules)
* [Releases](#releases)
* [Upgrading from SDK 1.* to 2.+](#upgrading-sdk)

## Overview

The Android SDK allows you to add the Veriff verification flow to your native Android application.
The documentation for Veriff backend integration can be found [here](https://developers.veriff.me/).

## Adding the SDK

Add a maven repository destination under the root ```build.gradle``` repositories tag in the allprojects section.
It should contain the following maven repositories:
``` java
    allprojects {
        repositories {
            maven { url "https://cdn.veriff.me/android/" } //veriff
            google()
            jcenter()

        }
    }
```
Add a dependency to the application ```build.gradle```:
``` java
    implementation 'com.veriff:veriff-library:2.4.4'
```

#### Permissions

The SDK will request all the permissions it needs, please make sure that the CAMERA, RECORD_AUDIO, and WRITE_EXTERNAL_STORAGE permissions are 
not explicitly removed using ```tools:node="remove"``` in your app`s manifest file. Ignore this if you are not explicitly removing any permissions.

## Starting the verification flow

The verification process must be launched inside the vendor specific Activity class.
The activity is aware of the current customer, who is either logged in via the Vendor system or who
is identified other way with unique system pass. The vendor must be able to determine the customer
later, if application ends/ or returns. It depends entirely on the Vendor business logic.

Every Veriff SDK session is unique for a client. The session expires after 7 days automatically,
but is valid until then.

In vendor activity class define the result code and initialize the SDK and launch the verification
flow as below:

| Parameters   | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| baseUrl      | 'https://staging.veriff.me' for testing and 'https://magic.veriff.me' for production |
| sessionToken | 'sessionToken' should be unique for each call. Check '/sessions' endpoint in the backend documentation [here](https://developers.veriff.me/) to learn how to generate one. |

```java
    Veriff.Builder veriffSDK = new Veriff.Builder(baseUrl, sessionToken);
    veriffSDK.launch(MainActivity.this, REQUEST_VERIFF);
```

## Getting the verification status

The verification result is sent to the vendor server in the background. (Reference for that is in the
[API Spec document](https://developers.veriff.me/) ). Veriff SDK sends callbacks to vendor`s mobile
application via onActivityResult, background service and broadcasts. The most reliable callback
method is the background service because it’s usually waken up by the Android system services. All
the mentioned ways return the same status codes so the vendor application developer can choose their
preferred method and ignore the other ones. Refer to the [handling result](#handling-the-result) section
to see how to handle the response and what each staus code means. Different ways to capture the
result are as below. After the verification data is uploaded, SDK v1.0 does not wait for the
final verification result (async). The SDK v1.0 only notifies whether the verification data upload is successful or not.

### In the activity

To capture the result from the SDK in the activity override ```onActivityResult``` in
vendor activity that starts the verification flow:

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

### Using a background service

To capture the result from the SDK using a background service.

```java
public class MyVeriffStatusUpdatesService extends VeriffStatusUpdatesService {
   @Override
   protected void onStatusChanged(String sessionToken, int statusCode) {
       handleResult(statusCode, sessionToken); //see below to know how to handle result
   }
}
```

Add the service to the application manifest:

```xml
<!-- Implement Veriff SDK updates service -->
<service android:name=".MyVeriffStatusUpdatesService"
    android:exported="false">
   <intent-filter>
       <action android:name="me.veriff.STATUS_UPDATE" />
   </intent-filter>
</service>
```

### Using a broadcast receiver

To capture the result using a broadcast receiver create a BroadcastReceiver which will listen to
Veriff SDK broadcasts:

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

Add the receiver to the application manifest:

```xml
<!-- Implement you own BroadcastReceiver to track VeriffSDK status, should be protected by "signature" permission -->
<receiver
android:name=".VeriffStatusReceiver"
android:permission="${applicationId}.VERIFF_STATUS_BROADCAST_PERMISSION">
   <intent-filter>
       <category android:name="${applicationId}" />
       <action android:name="veriff.info.status" />
   </intent-filter>
</receiver>
```

## Handling the result

All the three methods will return the same result which can be handled as below.

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

## Adding error logging

To turn on logging, you simply add your logging implementation instance (instance of LogAccess class) to
the SDK before launching the SDK as shown below:

```java
    Veriff.setLoggingImplementation(<Instance of your logging class>);
    Veriff.Builder veriffSDK = new Veriff.Builder(baseUrl, sessionToken);
    veriffSDK.launch(MainActivity.this, REQUEST_VERIFF);
```

## Proguard rules

### Veriff project proguard-rules.pro file contents
``` java
# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Application classes that will be serialized/deserialized over Gson
-keep class mobi.lab.veriff.data.** { *; }

-dontwarn mobi.lab.veriff.fragment.BaseFragment


-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class * {
    public protected *;
}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer
-dontwarn io.jsonwebtoken.impl.Base64Codec
-dontwarn io.jsonwebtoken.impl.crypto.EllipticCurveProvider

## RETROFIT 2 ##
-dontwarn javax.annotation.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
## RETROFIT 2 ##

## OkHttp
-dontwarn okio.**
## OKHttp


## GSON 2.2.4 specific rules ##

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-dontwarn com.koushikdutta.ion.conscrypt.ConscryptMiddleware

# WebRTC classes, accessed from native via JNI
-keep class org.webrtc.** { *; }
```

## Releases

Our release log can be found [here](https://github.com/Veriff/veriff-sample-android/blob/master/RELEASES.MD)

# Upgrading SDK

## Upgrading Veriff SDK from 1.* to 2.+

### Veriff SDK upgrade to 2.+

Veriff SDK 2.+ integration has changed significantly since 1.*. There are mayor changes in the SDK distribution where the SDK from now on is distributed via gradle import and many previously required services have been removed and trimmed down.

### Remove Veriff SDK required Firebase code

If your application uses Firebase notifications
    Remove Veriff message handling from your Firebase service class and instead you can handle your notification directly

```java
    if (!handleVeriffNotifications(remoteMessage)) {
        handleClientApplicationNotifications(remoteMessage);
    }
```

If your application does not use Firebase notifications
Delete both Firebase classes that extend **FirebaseInstanceIdService** and **FirebaseMessagingService** Remove Firebase services from the AndroidManifest:

``` java
      <!-- Implement Firebase notification service that handles Veriff notifications-->
      <service android:name=".service.SampleFirebaseMessagingService">
         <intent-filter>
             <action android:name="com.google.firebase.MESSAGING_EVENT" />
         </intent-filter>
      </service>
      <!-- Implement FirebaseInstanceIdService -->
      <service android:name=".service.SampleFirebaseInstanceIdService"
        android:exported="false">
         <intent-filter>
             <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
         </intent-filter>
      </service>
```
  Finally remove all Firebase dependencies from the app build.gradle file

## Remove the dependency on the .aar file and replace it with the gradle import from Veriff maven repo

  Remove the veriff SDK .aar file from the lib directory

  If you are not using any other manually added libraries remove the lib directory and the  reference to the lib directory from the application build.gradle file.
``` java
  allprojects {
      repositories {
          jcenter()
          flatDir {
              dirs 'libs'
          }
      }
  }
```
  Add a new maven repository destination under the root build.gradle repositories tag in allprojects bracket. It should contain the following maven repositories:

``` java
  allprojects {
      repositories {
          maven { url "https://cdn.veriff.me/android/" } //veriff
          google()
          jcenter()

      }
  }
```


  From the application build.gradle file remove all Veriff dependencies that you don't use as those are no longer needed and have been packaged in the gradle import

  The exception is <implementation 'io.probity.sdk:collector:1.0.0'> that still needs to be added on the parent application side.
``` java
  implementation "com.google.firebase:firebase-core:16.0.6"
  implementation "com.google.firebase:firebase-messaging:17.3.4"
  implementation "com.google.android.gms:play-services-gcm:16.0.0"
  implementation 'com.android.support.constraint:constraint-layout:1.1.3'
  implementation 'com.squareup.retrofit2:retrofit:2.3.0'
  implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
  implementation 'com.squareup.okhttp3:okhttp:3.10.0'
  implementation 'com.squareup.okhttp3:logging-interceptor:3.10.0'
  implementation 'com.jaredrummler:android-device-names:1.0.7'
  implementation 'com.koushikdutta.ion:ion:2.2.1'
  implementation 'com.twilio:video-android:1.3.4'
  implementation 'io.jsonwebtoken:jjwt:0.6.0'
  implementation 'org.greenrobot:eventbus:3.1.1'
```

  Remove irrelevant return values from VeriffSDKStatusUpdatedService and VeriffStatusReceiver

``` java
  VeriffConstants.STATUS_OUT_OF_BUSINESS_HOURS, VeriffConstants.STATUS_UNABLE_TO_RECORD_AUDIO and VeriffConstants.STATUS_VIDEO_CALL_ENDED
```
  As a final step add the import for Veriff libary in the application build.gradle dependency list. It should contain the following line:

``` java
  implementation 'com.veriff:veriff-library:2.4.4'
```



