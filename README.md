
[Design Document](https://docs.google.com/document/d/1Yp_233WN9cNtULJj9XZQR-F6fzW6qVobK3mI7Yx2ToQ/edit?usp=sharing)

[Handoff Document](https://docs.google.com/document/d/1Bu1i-rRUlzmKqpDJdNWhol64u49c6dMTqTzAZ_nW2oM/edit?usp=sharing)

[Final Presentation](https://gordonedu-my.sharepoint.com/:p:/g/personal/addison_abbot_gordon_edu/EXeUmhVc5HZLuHEivy3TiN8B5kGh2E6a1phtpX4qt501kQ?e=NBERLN)

**Overview**

Our system is a native Android application that attempts to bridge the gap between sysadmin and Day 0/2 operations for Android mobile devices (a parallel iOS version is in active development as well). It interfaces with vRealize Automation Cloud, a VMware product, and is aptly named the vRAC Companion app. The app can perform a limited number of actions to interact with the VMware Cloud Assembly environment, allowing sysadmins to perform some Day 2 operations while they are away from their computer. Day 0 operations involve deploying already defined blueprints, and Day 2 operations involve powering on/off, deleting, or changing the lease of deployments.

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
