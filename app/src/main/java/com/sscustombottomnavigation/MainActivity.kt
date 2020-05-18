package com.sscustombottomnavigation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.simform.custombottomnavigation.SSCustomBottomNavigation
import com.sscustombottomnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ID_HOME = 1
        private const val ID_EXPLORE = 2
        private const val ID_MESSAGE = 3
        private const val ID_NOTIFICATION = 4
        private const val ID_ACCOUNT = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val tvSelected = binding.tvSelected
        //tvSelected.typeface = Typeface.createFromAsset(assets, "fonts/SourceSansPro-Regular.ttf")

        binding.bottomNavigation.apply {

            add(SSCustomBottomNavigation.Model(ID_HOME, R.drawable.ic_home, "Home"))
            add(SSCustomBottomNavigation.Model(ID_EXPLORE, R.drawable.ic_heart, "Favorite"))
            add(SSCustomBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_message, "Chat"))
            add(
                SSCustomBottomNavigation.Model(
                    ID_NOTIFICATION,
                    R.drawable.ic_notification,
                    "Notification"
                )
            )
            add(SSCustomBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_account, "Profile"))

            setCount(ID_NOTIFICATION, "10")

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
                    ID_EXPLORE -> ContextCompat.getColor(this@MainActivity, R.color.color_favorite_bg)
                    ID_MESSAGE -> ContextCompat.getColor(this@MainActivity, R.color.color_chat_bg)
                    ID_NOTIFICATION -> ContextCompat.getColor(this@MainActivity, R.color.color_notification_bg)
                    ID_ACCOUNT -> ContextCompat.getColor(this@MainActivity, R.color.color_profile_bg)
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
}