package com.example.recipedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NavbarMenu extends AppCompatActivity {
    BottomNavigationView navigationView;
    HomePage homeFragment = new HomePage();
    SearchRecipe searchFragment = new SearchRecipe();
    CreateRecipe createFragment = new CreateRecipe();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar_menu);

        /*profileFragment.getActivity().findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavbarMenu.this, "Tes", Toast.LENGTH_SHORT).show();
            }
        });*/
        navigationView = findViewById(R.id.navbar_view);
//        Bundle bundle = getIntent().getExtras();
//        String id = bundle.getString("id");
//
//        Bundle pass = new Bundle();
//        pass.putString("uid",id);
//        homeFragment.setArguments(pass);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();


        navigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                    if(item.getItemId() == R.id.homeNav){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                        return true;
                    }

                    else if(item.getItemId() == R.id.searchNav) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, searchFragment).commit();
                        return true;
                    }

                    else if(item.getItemId() == R.id.createNav) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, createFragment).commit();
                        return true;
                    }

                    else if(item.getItemId() == R.id.profileNav){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment).commit();
                        return true;
                    }

                return false;
            }
        });

    }


}
