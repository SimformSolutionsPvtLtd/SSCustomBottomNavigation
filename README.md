![alt text](https://github.com/simformsolutions/SSCustomBottomNavigation/blob/master/images/library_banner.png)

# SSCustomBottomNavigation
[![Kotlin Version](https://img.shields.io/badge/Kotlin-v1.6.10-blue.svg)](https://kotlinlang.org)  [![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=flat)](https://www.android.com/) [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19) [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-SSCustomBottomNavigation-green.svg?style=flat )](https://android-arsenal.com/details/1/8163)

Getting Started
------------------------
**SSCustomBottomNavigation** is a customizable bottom bar library with curved animations and Jetpack Navigation support.

The actual features are:

 * Bottom Bar which have customizable text, color, background, icon.
 * Animated wave with customizable height
 * Native control and Jetpack Navigation support

### Demo
------------------------

![demo_data](https://github.com/simformsolutions/SSCustomBottomNavigation/blob/master/images/custom_bottom_navigation.gif)

Reverse Curve

![demo_data](https://github.com/simformsolutions/SSCustomBottomNavigation/blob/master/images/custom_bottom_navigation_revese.gif)


### Gradle Dependency
* Add it in your root build.gradle at the end of repositories:

    - For Gradle version 5.x.x or less
    ```
    allprojects {
        repositories {
        ...
        maven { url 'https://jitpack.io' }
        }
    }
    ```
    - For Gradle version 6.x.x and above, in settings.gradle file inside `pluginManagement` block
    ```
      pluginManagement {
        repositories {
        ...
        maven { url 'https://jitpack.io' }
        }
    }
    ```

* Add the dependency in your app's build.gradle file

	```
	dependencies {
		implementation 'com.github.simformsolutions:SSCustomBottomNavigation:3.4'
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
| `app:ss_reverseCurve` | Set Reverse Bzier Curve | `false` |

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


## ``` Setup in Code for Jetpack Navigation Support:```

1. First of all, you need to pass activeIndex and which is bottom index only :(Without this it is 0 by default, here I passed 2 means it will start with 2nd position)
```kotlin
val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 2
```

2. In your **onCreate()** of Activity create a list of **CbnMenuItem** that you want to appear in the SSCustomBottomNavigation. 

3. Then pass the list to the **setMenuItems()** function that also takes *activeIndex*(which is 0 by default, and you need to pass specific index if you don't want to start with 0 position) from which you can control which position item should be active when it is initialized.

```kotlin
private fun setBottomNavigationWithNavController(savedInstanceState: Bundle?) {
	
        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 2

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorite,
                R.id.navigation_chat,
                R.id.navigation_notifications,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        
        val menuItems = arrayOf(
            Model(
                icon = R.drawable.ic_home,                // Icon
                destinationId = R.id.navigation_home,     // destinationID
                id = 0,                // ID
                text = R.string.title_home,               // Icon with Text, If you don't want text then don't pass it
                count = R.string.empty_value              // notification count if you want to show then pass specific count else pass R.string.empty_value or don't pass anything
            ),
            Model(
                icon = R.drawable.ic_favorite_border_black,
                destinationId = R.id.navigation_favorite,
                id = 1,
                text = R.string.title_favorite
                                                          // notification count not needed here so, not passed
            ),
            Model(
                R.drawable.ic_message,
                R.id.navigation_chat,
                2,
                R.string.title_chat,
                R.string.empty_value
            ),
            Model(
                R.drawable.ic_notification,
                R.id.navigation_notifications,
                3,
                R.string.title_notifications,
                R.string.count
            ),
            Model(
                R.drawable.ic_account,
                R.id.navigation_profile,
                4,
                R.string.title_profile,
                R.string.empty_value
            )
        )

        binding.bottomNavigation.apply {
            setMenuItems(menuItems, activeIndex)
            setupWithNavController(navController)
        }

    }
```

### Handling Navigation with Listener
To listen whenever the menu item is clicked you can pass a lambda to `setOnMenuItemClickListener`.
```kotlin
binding.navView.setOnMenuItemClickListener { cbnMenuItem, index -> 
    // handle your own navigation here
}
```

### Handling Navigation with Jetpack Navigation
If you are like :heart: Jetpack then there is a method called `setupWithNavController()` that accepts `NavController` and will handle the navigaiton for you. Just don't forget to pass the `id` of the destination when you are creating `CbnMenuItem`.
```kotlin
binding.navView.setupWithNavController(navController)
```

### Manually setting the active item
If you need to manually set the active item you can call the `onMenuItemClick()` function and pass the index that you would like to be selected.
```kotlin
binding.navView.onMenuItemClick(4)
```

### Manually change notification count
* If you want to change notification count manually or after bottom navigation initialization done call the `setCount()` function and pass the index and updated count.
```kotlin
setCount(4, R.string.count_update)
```

### Handling configuration changes
* Due to animations, you need to manually handle the configuration changes. You can refer to the sample app for simple implementation.


## ``` Setup in Code for without Jetpack Navigation Support(Normal way): ```
### Old way : (2.1 version)

```kotlin
binding.bottomNavigation.apply {

	add(
		SSCustomBottomNavigation.Model(
			ID_HOME, 
			R.drawable.ic_home, 
			"Home"
		)
	)
	add(
		SSCustomBottomNavigation.Model(
			ID_EXPLORE, 
			R.drawable.ic_heart, 
			"Favorite"
		)
	)
	add(
		SSCustomBottomNavigation.Model(
			ID_MESSAGE, 
			R.drawable.ic_message, 
			"Chat"
		)
	)
	add(
		SSCustomBottomNavigation.Model(
			ID_NOTIFICATION,
			R.drawable.ic_notification,
			"Notification"
		)
	)
}
```

### New Way: (If you use latest version then you need to change your implementation) (>= 3.0 version)

```kotlin

companion object {
	private const val ID_HOME = 0
	private const val ID_EXPLORE = 1
	private const val ID_MESSAGE = 2
	private const val ID_NOTIFICATION = 3
	private const val ID_ACCOUNT = 4
}

private fun setBottomNavigationInNormalWay(savedInstanceState: Bundle?) {

	val activeIndex = savedInstanceState?.getInt("activeIndex") ?: ID_MESSAGE

	binding.bottomNavigation.apply {

		// If you don't pass activeIndex then by pass 0 here or call setSelectedIndex function only
		// setSelectedIndex()        // It will take 0 by default
		setSelectedIndex(activeIndex)

		add(
			Model(
				icon = R.drawable.ic_home,
				id = ID_HOME,
				text = R.string.title_home,
			)
		)
		add(
			Model(
				icon = R.drawable.ic_favorite_border_black,
				id = ID_EXPLORE,
				text = R.string.title_favorite,
				count = R.string.empty_value
			)
		)
	}
}
```

### Credits
------------------------
- This library was inspired by: 
	* [Meow Bottom Navigation](https://github.com/oneHamidreza/MeowBottomNavigation)
	* [curved-bottom-navigation](https://github.com/susonthapa/curved-bottom-navigation)

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/simformsolutions/SSCustomBottomNavigation/stargazers)__ for this repository. :star:

## iOS Library.
- Check our iOS Library also on [Github](https://github.com/simformsolutions/SSCustomTabbar)

## Awesome Mobile Libraries
- Check out our other available [awesome mobile libraries](https://github.com/SimformSolutionsPvtLtd/Awesome-Mobile-Libraries)

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
