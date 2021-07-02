# SSCustomBottomNavigation
[![Kotlin Version](https://img.shields.io/badge/Kotlin-v1.5.20-blue.svg)](https://kotlinlang.org)  [![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=flat)](https://www.android.com/) [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19) [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-SSCustomBottomNavigation-green.svg?style=flat )](https://android-arsenal.com/details/1/8163)

Getting Started
------------------------
**SSCustomBottomNavigation** is a customizable bottom bar library with curved animations.

The actual features are:

 * Bottom Bar which have customizable text, color, background, icon.
 * Animated wave with customizable height

### Demo
------------------------

![demo_data](https://github.com/simformsolutions/SSCustomBottomNavigation/blob/master/images/custom_bottom_navigation.gif)


### Gradle Dependency
* Add it in your root build.gradle at the end of repositories:

	```
	allprojects {
	    repositories {
		...
		maven { url 'https://jitpack.io' }
	    }
	}
	```

* Add the dependency in your app's build.gradle file

	```
	dependencies {
		implementation 'com.github.simformsolutions:SSCustomBottomNavigation:2.1'
	}
	```

### All Attributes
------------------------

| Attribute | Description | Default |
| --- | --- | --- |
| `app:ss_defaultIconColor` | Set Default Icon Color | `#757575` |
| `app:ss_selectedIconColor` | Set Selected Icon Color | `#00C957` |
| `app:ss_iconTextColor` | Set Bottom Bar Text Color | `#003F87` |
| `app:ss_iconTextTypeface` | Set Bottom Bar Fonts | `#none` |
| `app:ss_selectedIconTextColor` | Set Bottom Bar Selected Text Color | `#003F87` |
| `app:ss_iconTextSize` | Set Bottom Bar Text size  | `10sp` |
| `app:ss_waveHeight` | Set Wave Height | `7` |
| `app:ss_backgroundBottomColor` | Set Background Color for Bottom Bar | `#FF5733` |
| `app:ss_countBackgroundColor` | Set Background Color for Notification Badge | `#ff0000` |
| `app:ss_countTextColor` | Set Notification Badge text Color | `#9281c1` |
| `app:ss_countTypeface` | Set Font for Notification Badge | `none` |
| `app:ss_rippleColor` | Set Ripple Color | `#757575` |
| `app:ss_shadowColor` | Set Bottom Bar Shadow Color | `shadowColor` |

# Customization
------------------------
![alt text](https://github.com/simformsolutions/SSCustomBottomNavigation/blob/master/images/custom_bottom_navigation_image.png)


### Usage
------------------------

```
<com.simform.custombottomnavigation.SSCustomBottomNavigation
            android:id="@+id/bottomNavigation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            app:ss_backgroundBottomColor="#ffffff"
            app:ss_circleColor="#ff6f00"
            app:ss_waveHeight="7"
            app:ss_countBackgroundColor="#ff6f00"
            app:ss_countTextColor="#ffffff"
            app:ss_countTypeface="fonts/graphik_semibold.ttf"
            app:ss_defaultIconColor="#6200EE"
            app:ss_iconTextColor="#6200EE"
            app:ss_iconTextTypeface="fonts/graphik_semibold.ttf"
            app:ss_rippleColor="#2f424242"
            app:ss_iconTextSize="14sp"
            app:ss_selectedIconColor="#ff6f00"
            app:ss_selectedIconTextColor="#ff6f00"
            app:ss_shadowColor="#1f212121" />

```

### Credits
------------------------
- This library was inspired by [Meow Bottom Navigation](https://github.com/oneHamidreza/MeowBottomNavigation)

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/simformsolutions/SSCustomBottomNavigation/stargazers)__ for this repository. :star:

## iOS Library.
- Check our iOS Library also on [Github](https://github.com/simformsolutions/SSCustomTabbar)

## License

```
Copyright 2020 Simform Solutions

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
