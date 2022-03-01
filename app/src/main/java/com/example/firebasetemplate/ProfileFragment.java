package com.example.firebasetemplate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ProfileFragment extends AppFragment {
    private FragmentProfileBinding binding;

    public static int cantidadPosts = 0;
    public static int cantidadFavs = 0;
    private boolean post, fav;

    String username;
    String usermail;
    String userphoto;
    String useruid;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentProfileBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username=ProfileFragmentArgs.fromBundle(getArguments()).getUsername();
        usermail=ProfileFragmentArgs.fromBundle(getArguments()).getUseremail();
        userphoto=ProfileFragmentArgs.fromBundle(getArguments()).getUserphoto();
        useruid=ProfileFragmentArgs.fromBundle(getArguments()).getUseruuid();


        //System.out.println("accendiento a los metodos desde el onview");
        getCantidad();


        //cargar la foto
        if (userphoto != null) {
            Glide.with(this).load(userphoto).circleCrop().into(binding.imagenPerfil);
        } else {
            binding.imagenPerfil.setImageResource(R.drawable.ic_baseline_face_24);
        }

        binding.nombrePerfil.setText(username);
        binding.emailPerfil.setText(usermail);


        //System.out.println("post tras metodo "+cantidadPosts);
        //System.out.println("fav tras metodo "+cantidadPosts);

        //para que salga has de recargar, aka salir y volver a entrar
        //asignar


        //reset
        cantidadPosts = 0;
        cantidadFavs = 0;


    }


    Query getPersonalQuery() {
        return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("authorName", username);
    }

    Query getFavsQuery() {
        return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("likes." + useruid, true);
    }

    private void getCantidad() {
        getPersonalQuery().addSnapshotListener((collectionSnapshot, e) -> {
            cantidadPosts = collectionSnapshot.size();
            post = true;
            updateUI();
        });
        getFavsQuery().addSnapshotListener((collectionSnapshot, e) -> {
            cantidadFavs = collectionSnapshot.size();
            fav = true;
            updateUI();
        });

    }


    void updateUI() {
        if (post && fav) {
            binding.CantidadProgressBar.setVisibility(View.GONE);
            binding.relativeCantidad.setVisibility(View.VISIBLE);

            binding.postsNum.setText(String.valueOf(cantidadPosts));
            binding.favsNum.setText(String.valueOf(cantidadFavs));
        }
        // progress.setVisible(false);

    }
}