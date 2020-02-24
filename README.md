
[Design Document](https://docs.google.com/document/d/1Yp_233WN9cNtULJj9XZQR-F6fzW6qVobK3mI7Yx2ToQ/edit?usp=sharing)

**Getting Started**

Prerequisites:

    Android Studio - [https://developer.android.com/studio](https://developer.android.com/studio)

    Android SDK -  tools -> SDK Manager -> SDK Platform for Android 10

This project follows Google’s Material Design guidelines ([https://material.io](https://material.io/)) and is written in Java (chosen over Kotlin for its familiar syntax, ample documentation, and wide community support)

	
After cloning this repo, the project can be opened in Android Studio (file -> open project). Make sure to make the project (ctl f9) and then run on an available device (set up a virtual device with AVD manager if a physical device is not connected).

Currently this project consists of 2 main activities - login and main

The login activity :

	A simple field for API Key and login button

	The login button is hardcoded to ignore the api key value and launch the main activity onClick

The main activity:

	A tabbed activity with 2 tabs, one for blueprints and one for deployments

	Each tab holds a fragment view, consisting of a list of blueprints and deployments respectively.

![](https://github.com/gordon-cs/nimbus/workflows/build/badge.svg)
