package hcmute.edu.vn.fitnesstrackerapp;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

    private static final int REQUEST_CAMERA = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        acquirePermission();

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

    private void acquirePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

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