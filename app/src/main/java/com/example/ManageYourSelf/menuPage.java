package com.example.ManageYourSelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ManageYourSelf.databinding.ActivityMenuPageBinding;

public class menuPage extends AppCompatActivity {

    ActivityMenuPageBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replacceFragment(new HomeFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.home:
                    replacceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replacceFragment(new ProfileFragment());
                    break;
                case R.id.catalog:
                    replacceFragment(new CatalogFragment());
                    break;
            }

            return true;
        });

    }

    private void replacceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}