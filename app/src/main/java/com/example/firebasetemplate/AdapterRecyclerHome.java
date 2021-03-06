package com.example.firebasetemplate;


import static com.example.firebasetemplate.NavigationDirections.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.ViewholderPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerHome extends RecyclerView.Adapter<AdapterRecyclerHome.MyViewHolder>{
    private Context context;
    private List<Post> postList =new ArrayList<>();
    NavController navController;
    private String usermail;

    public AdapterRecyclerHome(){}

    public AdapterRecyclerHome(Context context, NavController navController, List<Post> postList,String usermail) {
        this.context = context;
        this.postList = postList;
        this.navController = navController;
        this.usermail=usermail;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ViewholderPostBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerHome.MyViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.binding.autor.setText(post.getAuthorName());
        holder.binding.contenido.setText(post.getContent());

        //foto post
        if(post.getUrlImagenPost()!=null){
            Glide.with(context).load(post.getUrlImagenPost()).centerCrop().into(holder.binding.imagen);//igual que picaso
            holder.binding.imagen.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.imagen.setVisibility(View.GONE);
        }

        //icono
        if(post.getAuthorIcono() != null && !post.getAuthorIcono().isEmpty()){
            Glide.with(context).load(postList.get(position).getAuthorIcono()).centerCrop().into(holder.binding.autorFoto);
        }
        else{
            holder.binding.autorFoto.setImageResource(R.drawable.ic_baseline_face_24);
        }

        holder.binding.autorFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, post.getAuthorIcono(), Toast.LENGTH_SHORT).show();
                //navigate al profile con los datos son los de este usuario



                ActionToDetailProfileFragment action2= actionToDetailProfileFragment();
                //ActionToProfileFragment action2= actionToProfileFragment();

                //pasar datos
                action2.setUsermail(post.getAuthorEmail());
                navController.navigate(action2);

            }
        });


        //favs
        holder.binding.favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                //cuando haces click, guardas que ese usuario le ha dado like
                FirebaseFirestore.getInstance().collection("posts")
                        .document(postList.get(holder.getAdapterPosition()).getId())//recuperas el id
                        .update("likes."+ FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                !postList.get(holder.getAdapterPosition()).getLikes().containsKey(auth.getUid()) ? true : FieldValue.delete());//campo a actualizar y con que valor
                //si el hashmpa no tiene ese like, se lo pones.
                //sino lo tiene, lo eliminas del hashmap
            }
        });

        holder.binding.cantidadFavsPost.setText(String.valueOf(post.getLikes().size()));

        if(postList.get(position).getLikes()!=null){
            //si el user tiene el like en el hashmap
            holder.binding.favorito.setChecked(postList.get(position).getLikes().containsKey(FirebaseAuth.getInstance().getUid()));
        }

        //a details
        holder.binding.cardViewholder.setOnClickListener(v -> {
            //aqui se ha de hacer que pueda venir de otras actividades

                ActionToDetailFragment action = actionToDetailFragment();

                action.setPostid(post.getId());//le enviamos el post id y asi lo recuperamos
                navController.navigate(action);


        });


        //cantidad comments
        FirebaseFirestore.getInstance().collection("posts").document(post.getId()).collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int cantidad=0;
                for(DocumentSnapshot a:value){cantidad++;}

                holder.binding.cantidadCommentsPost.setText(String.valueOf(cantidad));
            }

        });


    }


    @Override
    public int getItemCount() {
        if(postList ==null){return 0;}
        else{return postList.size();}

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ViewholderPostBinding binding;

        public MyViewHolder(ViewholderPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
