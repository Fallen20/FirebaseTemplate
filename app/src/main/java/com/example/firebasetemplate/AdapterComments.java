package com.example.firebasetemplate;

import static com.example.firebasetemplate.NavigationDirections.actionToDetailProfileFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.LayoutCommentBinding;
import com.example.firebasetemplate.databinding.ViewholderPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder>{

    private Context context;
    private List<Comment> commentList;
    NavController navController;
    private String email;

    public AdapterComments(Context context, List<Comment> commentList, NavController navController, String authorEmail) {
        this.context = context;
        this.commentList = commentList;
        this.navController = navController;
        this.email=authorEmail;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutCommentBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        holder.binding.textComment.setText(commentList.get(position).getContenido());
        holder.binding.nameCommenter.setText(commentList.get(position).getNombreUsuario());

        //foto comentador
        if(commentList.get(position).getPhoto()!=null){
            Glide.with(context).load(commentList.get(position).getPhoto()).centerCrop().into(holder.binding.iconAuthorComment);
        }
        else{
            holder.binding.iconAuthorComment.setImageResource(R.drawable.ic_baseline_face_24);
        }


        //si tiene fav
        if(comment.getLikes()!=null){
            //si el user tiene el like en el hashmap
            holder.binding.favorito2.setChecked(comment.getLikes().containsKey(FirebaseAuth.getInstance().getUid()));
        }

        holder.binding.favorito2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                //cuando haces click, guardas que ese usuario le ha dado like
                FirebaseFirestore.getInstance().
                        collection("posts").document(comment.getPostid())
                        .collection("comments").document(comment.getCommentid())//recuperas el id
                        .update("likes."+ FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                !comment.getLikes().containsKey(auth.getUid()) ? true : FieldValue.delete());
            }
        });

        if(comment.getLikes()!=null){
            holder.binding.cantidadFavsComent.setText(String.valueOf(comment.getLikes().size()));
        }
        else{
            holder.binding.cantidadFavsComent.setText("0");
        }


        //intent
        holder.binding.cardComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDirections.ActionToDetailProfileFragment action2= actionToDetailProfileFragment();
                //ActionToProfileFragment action2= actionToProfileFragment();

                //pasar datos
                action2.setUsermail(comment.getEmail());
                navController.navigate(action2);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(commentList ==null){return 0;}
        else{return commentList.size();}
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LayoutCommentBinding binding;

        public MyViewHolder(LayoutCommentBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
