package hcmute.edu.vn.fitnesstrackerapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.fitnesstrackerapp.fragment.CaloriesFragment;
import hcmute.edu.vn.fitnesstrackerapp.fragment.HomeFragment;
import hcmute.edu.vn.fitnesstrackerapp.fragment.PostureFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // check user choose which item, we will return to the fragment
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home){
                loadFragment(new HomeFragment());
            }else if (itemId == R.id.nav_calories){
                loadFragment(new CaloriesFragment());
            }else if (itemId == R.id.nav_post){
                loadFragment(new PostureFragment());
            }else {
                loadFragment(new HomeFragment());
            }
            return true;
        });

    }

    private void loadFragment(Fragment fragment){
        currentFragment = fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction =manager.beginTransaction();
        // replace fragment into container
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.commit();
    }
}