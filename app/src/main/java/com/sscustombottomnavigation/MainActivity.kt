package com.sscustombottomnavigation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.simform.custombottomnavigation.Model
import com.sscustombottomnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val ID_HOME = 0
        private const val ID_EXPLORE = 1
        private const val ID_MESSAGE = 2
        private const val ID_NOTIFICATION = 3
        private const val ID_ACCOUNT = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //setBottomNavigationInNormalWay(savedInstanceState)
        setBottomNavigationWithNavController(savedInstanceState)
    }

    private fun setBottomNavigationInNormalWay(savedInstanceState: Bundle?) {
        val tvSelected = binding.tvSelected
        //tvSelected.typeface = Typeface.createFromAsset(assets, "fonts/SourceSansPro-Regular.ttf")

        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 2

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
            add(
                Model(
                    R.drawable.ic_message,
                    id = ID_MESSAGE,
                    text = R.string.title_chat,
                    count = R.string.empty_value
                )
            )
            add(
                Model(
                    R.drawable.ic_notification,
                    id = ID_NOTIFICATION,
                    text = R.string.title_notifications,
                    count = R.string.count
                )
            )
            add(
                Model(
                    R.drawable.ic_account,
                    id = ID_ACCOUNT,
                    text = R.string.title_profile,
                    count = R.string.empty_value
                )
            )

            // If you want to change count
            setCount(ID_NOTIFICATION, R.string.count_update)

            setOnShowListener {
                val name = when (it.id) {
                    ID_HOME -> "Home"
                    ID_EXPLORE -> "Explore"
                    ID_MESSAGE -> "Message"
                    ID_NOTIFICATION -> "Notification"
                    ID_ACCOUNT -> "Account"
                    else -> ""
                }

                val bgColor = when (it.id) {
                    ID_HOME -> ContextCompat.getColor(this@MainActivity, R.color.color_home_bg)
                    ID_EXPLORE -> ContextCompat.getColor(
                        this@MainActivity,
                        R.color.color_favorite_bg
                    )
                    ID_MESSAGE -> ContextCompat.getColor(this@MainActivity, R.color.color_chat_bg)
                    ID_NOTIFICATION -> ContextCompat.getColor(
                        this@MainActivity,
                        R.color.color_notification_bg
                    )
                    ID_ACCOUNT -> ContextCompat.getColor(
                        this@MainActivity,
                        R.color.color_profile_bg
                    )
                    else -> ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
                }

                tvSelected.text = getString(R.string.main_page_selected, name)
                binding.lnrLayout.setBackgroundColor(bgColor)
            }

            setOnClickMenuListener {
                val name = when (it.id) {
                    ID_HOME -> "HOME"
                    ID_EXPLORE -> "EXPLORE"
                    ID_MESSAGE -> "MESSAGE"
                    ID_NOTIFICATION -> "NOTIFICATION"
                    ID_ACCOUNT -> "ACCOUNT"
                    else -> ""
                }
            }

            setOnReselectListener {
                Toast.makeText(context, "item ${it.id} is reselected.", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setBottomNavigationWithNavController(savedInstanceState: Bundle?) {

        // If you don't pass activeIndex then by default it will take 0 position
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
                icon = R.drawable.ic_home,
                destinationId = R.id.navigation_home,
                id = 0,
                text = R.string.title_home,
                count = R.string.empty_value
            ),
            Model(
                R.drawable.ic_favorite_border_black,
                R.id.navigation_favorite,
                id = 1,
                R.string.title_favorite,
                R.string.empty_value
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
            // If you don't pass activeIndex then by default it will take 0 position
            setMenuItems(menuItems, activeIndex)
            setupWithNavController(navController = navController, exitOnBack = false)

            // manually set the active item, so from which you can control which position item should be active when it is initialized.
            // onMenuItemClick(4)

            // If you want to change notification count
            setCount(ID_NOTIFICATION, R.string.count_update)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("activeIndex", binding.bottomNavigation.getSelectedIndex())
        super.onSaveInstanceState(outState)
    }
}