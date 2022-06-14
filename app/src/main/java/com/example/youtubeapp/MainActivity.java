package com.example.youtubeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.youtubeapp.Fragment.ExploreFragment;
import com.example.youtubeapp.Fragment.HomeFragment;
import com.example.youtubeapp.Fragment.LibraryFragment;
import com.example.youtubeapp.Fragment.NotificationFragment;
import com.example.youtubeapp.Fragment.SubcriptionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    Toolbar tbNav;
    BottomNavigationView bnvFragment;
    FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbNav = findViewById(R.id.tb_nav);
        bnvFragment = findViewById(R.id.bnv_fragment);
        flContent = findViewById(R.id.fl_content);

        // Sự kiện click thanh menu bottom
        bnvFragment.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home:
                        HomeFragment homeFragment = new HomeFragment();
                        item.setChecked(true);
                        selectFragment(homeFragment);
                        break;
                    case R.id.mn_explore:
                        ExploreFragment exploreFragment = new ExploreFragment();
                        item.setChecked(true);
                        selectFragment(exploreFragment);
                        break;
                    case R.id.mn_subcription:
                        SubcriptionFragment subcriptionFragment = new SubcriptionFragment();
                        item.setChecked(true);
                        selectFragment(subcriptionFragment);
                        break;
                    case R.id.mn_notification:
                        NotificationFragment notificationFragment = new NotificationFragment();
                        item.setChecked(true);
                        selectFragment(notificationFragment);
                        break;
                    case R.id.mn_library:
                        LibraryFragment libraryFragment = new LibraryFragment();
                        item.setChecked(true);
                        selectFragment(libraryFragment);
                        break;
                }
                return false;
            }
        });

    }

    // Phương thức chọn fragment khi click menu
    private void selectFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.commit();
    }


}