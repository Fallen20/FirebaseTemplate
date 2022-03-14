package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentDetailedProfileBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class DetailedProfile extends AppFragment {

    String usermail;
    User user;

    private boolean post, fav;
    public static int cantidadPosts = 0;
    public static int cantidadFavs = 0;
    private List<Post> postArrayList =new ArrayList<>();
    private AdapterRecyclerHome adapter;
    private FragmentDetailedProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentDetailedProfileBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //estoy haciendo el binding mal

        usermail = DetailedProfileArgs.fromBundle(getArguments()).getUsermail();//recuperas el email del usuario

        //recuperar los datos
        if (usermail != null && !usermail.equals("abcd")) {
            db.collection("users").document(usermail).get().addOnSuccessListener(documentSnapshot -> {
                user = documentSnapshot.toObject(User.class);//recuperar el user

                if (user.getPhotoUser() != null) {
                    Glide.with(this).load(user.getPhotoUser()).circleCrop().into(binding.imagenPerfil2);
                } else {
                    binding.imagenPerfil2.setImageResource(R.drawable.ic_baseline_face_24);
                }

                binding.nombrePerfil2.setText(user.getUsername());


                getCantidad();

                //posts
                getPersonalQuery().addSnapshotListener((collectionSnapshot, e)->{
                    postArrayList.clear();//sino se duplica
                    for(DocumentSnapshot a:collectionSnapshot){
                        // a.toObject(Posts.class);//convertir cada documento en clase posts para mostrarlos con ese formato
                        Post post =a.toObject(Post.class);
                        post.setId(a.getId());

                        postArrayList.add(post);//añadir para el recycler
                    }
                    adapter.notifyDataSetChanged();//esto es que notifica que ha cambiado y se vuelve a ejecutar
                });

                adapter=new AdapterRecyclerHome(getContext(),navController,postArrayList, usermail);
                binding.recyclerPostProfile.setAdapter(adapter);

            });
        }
    }

    Query getPersonalQuery() {
        if(usermail!=null && !usermail.equals("abcd")){
            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("authorName", user.getUsername());
        }
        else if(usermail.equals("abcd")) {
            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("authorName", auth.getCurrentUser().getDisplayName());
        }

        return null;
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

    Query getFavsQuery() {
        if(usermail!=null && !usermail.equals("abcd")){//TODO
            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("likes." + user.getIdUser(), true);
        }
        else if(usermail.equals("abcd")){//el placeholder
            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("likes." + auth.getCurrentUser().getUid(), true);
        }
        return null;
    }

    void updateUI() {
        if (post && fav) {
            binding.cantidadProgressBarDetailedProfile.setVisibility(View.GONE);
            binding.reativeProfile2.setVisibility(View.VISIBLE);

            binding.postsNum2.setText(String.valueOf(cantidadPosts));
            binding.favsNum2.setText(String.valueOf(cantidadFavs));
        }
        // progress.setVisible(false);

    }
}