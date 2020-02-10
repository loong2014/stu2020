#!/usr/bin/env bash

## clean
./gradlew clean

## compile and generate
./gradlew build

## uninstall
#adb uninstall com.sunny.family
#adb shell pm clear com.sunny.family

## install
#adb install -r ./app/build/outputs/apk/debug/*.apk

#adb shell am start -n com.sunny.family/com.sunny.family.launcher.LauncherActivity



