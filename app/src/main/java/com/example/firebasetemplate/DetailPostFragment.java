package com.example.firebasetemplate;

import static com.example.firebasetemplate.NavigationDirections.actionToDetailProfileFragment;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class DetailPostFragment extends AppFragment {

    FragmentDetailPostBinding binding;
    private Post post;
    String postid;
    String email;
    private final List<Comment> commentList=new ArrayList<>();
    private AdapterComments adapterComments;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentDetailPostBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postid = DetailPostFragmentArgs.fromBundle(getArguments()).getPostid();


        adapterComments = new AdapterComments(getContext(), commentList, navController,null);
        binding.recyclerDetails.setAdapter(adapterComments);


        db.collection("posts").document(postid).addSnapshotListener((documentSnapshot,error) -> {
            post = documentSnapshot.toObject(Post.class);


            binding.cantFavs.setText(String.valueOf(post.getLikes().size()));
            binding.descDetails.setText(post.getContent());
            binding.usuarioDetails.setText(post.getAuthorName());

            //imagen post
            if (post.getUrlImagenPost() != null) {
                Glide.with(this).load(post.getUrlImagenPost()).centerCrop().into(binding.imageDetails);
            } else {
                binding.imageDetails.setVisibility(View.GONE);
            }

            //icono crador del post
            if (post.getAuthorIcono() != null) {
                Glide.with(this).load(post.getAuthorIcono()).centerCrop().into(binding.autorDetails);
            } else {
                binding.autorDetails.setImageResource(R.drawable.ic_baseline_face_24);
            }

            //icono de la persona que comenta
            if (auth.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).centerCrop().into(binding.iconoAddComment);
            } else {
                binding.iconoAddComment.setImageResource(R.drawable.ic_baseline_face_24);
            }




            //icono favs post
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



                }
            });



            //check added comments


            checkComments();


            //add comment
            binding.buttonAddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.buttonAddComment.setEnabled(false);
                    uploadComment();

                }
            });

        });

        binding.autorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDirections.ActionToDetailProfileFragment action2= actionToDetailProfileFragment();

                //pasar datos
                action2.setUsermail(post.getAuthorEmail());
                navController.navigate(action2);
            }
        });
    }

    private void checkComments() {
        //sacar comments
        db.collection("posts").document(postid).collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                commentList.clear();

                for(DocumentSnapshot a:value){
                    Comment comment = a.toObject(Comment.class);
                    comment.setCommentid(a.getId());
                    commentList.add(comment);
                }

                adapterComments.notifyDataSetChanged();

            }
        });
    }

    private void uploadComment() {
        Comment comment=new Comment();
        comment.setContenido(binding.inputComment.getText().toString().trim());
        comment.setNombreUsuario(auth.getCurrentUser().getDisplayName());
        comment.setEmail(auth.getCurrentUser().getEmail());
        comment.setPostid(postid);

        if (auth.getCurrentUser().getPhotoUrl() != null) {
            comment.setPhoto(auth.getCurrentUser().getPhotoUrl().toString());
        } else {
            comment.setPhoto(null);
        }


        FirebaseFirestore.getInstance().collection("posts").document(postid).collection("comments").add(comment)
                .addOnCompleteListener(task -> {
                    binding.buttonAddComment.setEnabled(true);
                    binding.inputComment.setText("");
                    binding.inputComment.setHint(R.string.addComment);
                });//add es una fila
        adapterComments.notifyDataSetChanged();
    }
}