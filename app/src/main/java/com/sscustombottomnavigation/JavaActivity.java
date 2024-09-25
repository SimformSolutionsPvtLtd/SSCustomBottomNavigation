package com.sscustombottomnavigation;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.simform.custombottomnavigation.Model;
import com.sscustombottomnavigation.databinding.ActivityHomeBinding;

/**
 * Java Example for Custom Bottom Navigation
 */
public class JavaActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private static final int ID_HOME = 0;
    private static final int ID_EXPLORE = 1;
    private static final int ID_MESSAGE = 2;
    private static final int ID_NOTIFICATION = 3;
    private static final int ID_ACCOUNT = 4;
    private static final String KEY_ACTIVE_INDEX = "activeIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        // Uncomment to use the bottom navigation in traditional way, such as without using Navigation Component
        // setBottomNavigationInNormalWay(savedInstanceState);
        setBottomNavigationWithNavController(savedInstanceState);
    }

    private void setBottomNavigationInNormalWay(Bundle savedInstanceState) {
        TextView tvSelected = binding.tvSelected;
        // Uncomment to set the typeface of text
        // tvSelected.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Regular.ttf"));

        int activeIndex = savedInstanceState == null ? ID_MESSAGE : savedInstanceState.getInt(KEY_ACTIVE_INDEX);

        binding.bottomNavigation.setSelectedIndex(activeIndex);

        binding.bottomNavigation.add(new Model(R.drawable.ic_home, ID_HOME, ID_HOME, R.string.title_home, R.string.empty_value));
        binding.bottomNavigation.add(new Model(R.drawable.ic_favorite_border_black, ID_EXPLORE, ID_EXPLORE, R.string.title_favorite, R.string.empty_value));
        binding.bottomNavigation.add(new Model(R.drawable.ic_message, ID_MESSAGE, ID_MESSAGE, R.string.title_chat, R.string.empty_value));
        binding.bottomNavigation.add(new Model(R.drawable.ic_notification, ID_NOTIFICATION, ID_NOTIFICATION, R.string.title_notifications, R.string.count));
        binding.bottomNavigation.add(new Model(R.drawable.ic_account, ID_ACCOUNT, ID_ACCOUNT, R.string.title_profile, R.string.empty_value));

        // If you want to change count
        binding.bottomNavigation.setCount(ID_NOTIFICATION, R.string.count_update);

        binding.bottomNavigation.setOnShowListener(item -> {
            String name;
            switch (item.getId()) {
                case ID_HOME:
                    name = "Home";
                    break;
                case ID_EXPLORE:
                    name = "Explore";
                    break;
                case ID_MESSAGE:
                    name = "Message";
                    break;
                case ID_NOTIFICATION:
                    name = "Notification";
                    break;
                case ID_ACCOUNT:
                    name = "Account";
                    break;
                default:
                    name = "";
            }

            int bgColor;
            switch (item.getId()) {
                case ID_HOME:
                    bgColor = ContextCompat.getColor(JavaActivity.this, R.color.color_home_bg);
                    break;
                case ID_EXPLORE:
                    bgColor = ContextCompat.getColor(JavaActivity.this, R.color.color_favorite_bg);
                    break;
                case ID_MESSAGE:
                    bgColor = ContextCompat.getColor(JavaActivity.this, R.color.color_chat_bg);
                    break;
                case ID_NOTIFICATION:
                    bgColor = ContextCompat.getColor(JavaActivity.this, R.color.color_notification_bg);
                    break;
                case ID_ACCOUNT:
                    bgColor = ContextCompat.getColor(JavaActivity.this, R.color.color_profile_bg);
                    break;
                default:
                    bgColor = ContextCompat.getColor(JavaActivity.this, R.color.colorPrimary);
            }

            tvSelected.setText(getString(R.string.main_page_selected, name));
            binding.lnrLayout.setBackgroundColor(bgColor);
            return null;
        });

        binding.bottomNavigation.setOnClickMenuListener(item -> {
            String name;
            switch (item.getId()) {
                case ID_HOME:
                    name = "HOME";
                    break;
                case ID_EXPLORE:
                    name = "EXPLORE";
                    break;
                case ID_MESSAGE:
                    name = "MESSAGE";
                    break;
                case ID_NOTIFICATION:
                    name = "NOTIFICATION";
                    break;
                case ID_ACCOUNT:
                    name = "ACCOUNT";
                    break;
                default:
                    name = "";
            }
            // Use the 'name' variable here if needed
            return null;
        });

        binding.bottomNavigation.setOnReselectListener(item -> {
            Toast.makeText(JavaActivity.this, "item " + item.getId() + " is reselected.", Toast.LENGTH_LONG).show();
            return null;
        });
    }

    private void setBottomNavigationWithNavController(Bundle savedInstanceState) {
        // If you don't pass activeIndex then by default it will take 0 position
        int activeIndex = savedInstanceState == null ? 2 : savedInstanceState.getInt("activeIndex");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_favorite,
                R.id.navigation_chat,
                R.id.navigation_notifications,
                R.id.navigation_profile
        ).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Model[] menuItems = new Model[]{
                new Model(R.drawable.ic_home, R.id.navigation_home, ID_HOME, R.string.title_home, R.string.empty_value),
                new Model(R.drawable.ic_favorite_border_black, R.id.navigation_favorite, ID_EXPLORE, R.string.title_favorite, R.string.empty_value),
                new Model(R.drawable.ic_message, R.id.navigation_chat, ID_MESSAGE, R.string.title_chat, R.string.empty_value),
                new Model(R.drawable.ic_notification, R.id.navigation_notifications, ID_NOTIFICATION, R.string.title_notifications, R.string.count),
                new Model(R.drawable.ic_account, R.id.navigation_profile, ID_ACCOUNT, R.string.title_profile, R.string.empty_value)
        };

        binding.bottomNavigation.setMenuItems(menuItems, activeIndex);
        binding.bottomNavigation.setupWithNavController(navController, false);

        // manually set the active item, so from which you can control which position item should be active when it is initialized.
        // binding.bottomNavigation.onMenuItemClick(4);

        // If you want to change notification count
        binding.bottomNavigation.setCount(ID_NOTIFICATION, R.string.count_update);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_ACTIVE_INDEX, binding.bottomNavigation.getSelectedIndex());
        super.onSaveInstanceState(outState);
    }
}
