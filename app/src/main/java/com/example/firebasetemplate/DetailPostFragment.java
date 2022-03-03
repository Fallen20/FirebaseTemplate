package com.example.firebasetemplate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentDetailPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class DetailPostFragment extends AppFragment {

    FragmentDetailPostBinding binding;
    private Post post;
    private Post post2;
    String postid;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentDetailPostBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postid = DetailPostFragmentArgs.fromBundle(getArguments()).getPostid();


        db.collection("posts").document(postid).get().addOnSuccessListener(documentSnapshot -> {
            post = documentSnapshot.toObject(Post.class);

            binding.descDetails.setText(post.getContent());
            binding.usuarioDetails.setText(post.getAuthorName());

            //num fav
            if (post.getLikes() == null || post.getLikes().isEmpty()) {
                binding.cantFavs.setText("0");
            } else {
                String valor= String.valueOf(post.getLikes().size());
                binding.cantFavs.setText(valor);
            }

            //imagen post
            if (post.getUrlImagenPost() != null) {
                Glide.with(this).load(post.getUrlImagenPost()).into(binding.imageDetails);
            } else {
                binding.imageDetails.setVisibility(View.GONE);
            }

            //icono
            if (post.getAuthorIcono() != null) {
                Glide.with(this).load(post.getAuthorIcono()).into(binding.autorDetails);
            } else {
                binding.autorDetails.setImageResource(R.drawable.ic_baseline_face_24);
            }

            //icono favs
            if(post.getLikes()!=null){
                //si el user tiene el like en el hashmap
                binding.favoritoDet.setChecked(post.getLikes().containsKey(FirebaseAuth.getInstance().getUid()));
            }


            //fav
            binding.favoritoDet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    //cuando haces click, guardas que ese usuario le ha dado like
                    FirebaseFirestore.getInstance().collection("posts")
                            .document(postid)//recuperas el id
                            .update("likes."+ FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    !post.getLikes().containsKey(auth.getUid()) ? true : FieldValue.delete());

                    recuperarLikes();

                }
            });



            //actualizar los favs al momento


        });



    }

    private void recuperarLikes() {
        db.collection("posts").document(postid).get().addOnSuccessListener(documentSnapshot1 -> {
            //no actualiza al quitar
            binding.cantFavs.setText(String.valueOf(documentSnapshot1.toObject(Post.class).getLikes().size()));
        });
    }

    Query setQuery(){
        return db.collection("posts");
    }





}