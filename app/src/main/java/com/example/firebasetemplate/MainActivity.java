package com.example.firebasetemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.example.firebasetemplate.databinding.NavHeaderMainBinding;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        FirebaseFirestore.getInstance().setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build());
        //mira en internet, no en cache. Por si se borra cosas que no de probelmas por guardarlo

        setSupportActionBar(binding.toolbar);

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.postsHomeFragment, R.id.postsLikeFragment, R.id.postsMyFragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();



        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId() == R.id.signInFragment || destination.getId()==R.id.registerFragment) {
                binding.toolbar.setVisibility(View.GONE);
                binding.bottomNavView.setVisibility(View.GONE);
            } else {
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.bottomNavView.setVisibility(View.VISIBLE);
            }
        });

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                    Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).circleCrop().into(navHeaderMainBinding.headerImg);
                }//poner la imagen en el cajon
                else{
                   // Glide.with(this).load(R.drawable.ic_baseline_face_24).circleCrop().into(navHeaderMainBinding.headerImg);
                    navHeaderMainBinding.headerImg.setImageResource(R.drawable.ic_baseline_face_24);
                }
                navHeaderMainBinding.headerTitle.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                navHeaderMainBinding.subtiteHeader.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                Log.e("sdfdfs","USER:" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}