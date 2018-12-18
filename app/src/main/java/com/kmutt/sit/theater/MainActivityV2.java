package com.kmutt.sit.theater;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.kmutt.sit.theater.booking.movies.MoviesFragment;
import com.kmutt.sit.theater.fromweb.LocationsMapFragment;

public class MainActivityV2 extends AppCompatActivity {

    //
    // VIEWS
    //
    private View fragmentFrame;

    //
    // FRAGMENTS
    //
    private Fragment membershipFragment;
    private Fragment locationsMapFragment;
    private Fragment moviesFragment;

    //
    // On tab selection changed
    //
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movies:
//                    mTextMessage.setText(R.string.title_movies);
                    loadTab(0);
                    return true;
                case R.id.navigation_locations:
//                    mTextMessage.setText(R.string.title_promotions);
                    loadTab(1);
                    return true;
                case R.id.navigation_membership:
//                    mTextMessage.setText(R.string.title_membership);
                    loadTab(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);

        fragmentFrame = findViewById(R.id.fragmentFrame);

        //
        // BottomNavigation
        //
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_movies);
    }

    public void loadTab(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                if (moviesFragment == null)
                    moviesFragment = new MoviesFragment();
                fragment = moviesFragment;
                break;
            case 1:
                if (locationsMapFragment == null)
                    locationsMapFragment = new LocationsMapFragment();
                fragment = locationsMapFragment;
                break;
            case 2:
                if (membershipFragment == null)
                    membershipFragment = new MembershipFragment();
                fragment = membershipFragment;
                break;
            default:
                fragment = new Fragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentFrame, fragment)
                .commit();
    }

}
