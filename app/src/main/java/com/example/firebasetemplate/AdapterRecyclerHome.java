package com.example.firebasetemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.ViewholderPostBinding;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerHome extends RecyclerView.Adapter<AdapterRecyclerHome.MyViewHolder>{
    private Context context;
    private List<Posts> postsList=new ArrayList<>();

    public AdapterRecyclerHome(){}

    public AdapterRecyclerHome(Context context, List<Posts> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ViewholderPostBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerHome.MyViewHolder holder, int position) {
        Posts post = postsList.get(position);
        holder.binding.autor.setText(post.getAuthorName());
        holder.binding.contenido.setText(post.getContent());
        Glide.with(context).load(post.getUrlDescarga()).into(holder.binding.imagen);//igual que picaso

        if(post.getAuthorIcono() != null && !post.getAuthorIcono().isEmpty()){
            Glide.with(context).load(postsList.get(position).getAuthorIcono()).into(holder.binding.autorFoto);
        }
        else{
            holder.binding.autorFoto.setImageResource(R.drawable.ic_baseline_face_24);
        }

        holder.binding.favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                //cuando haces click, guardas que ese usuario le ha dado like
                FirebaseFirestore.getInstance().collection("posts")
                        .document(postsList.get(holder.getAdapterPosition()).getId())//recuperas el id
                        .update("likes."+ FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                postsList.get(holder.getAdapterPosition()).getLikes().containsKey(auth.getUid()) ? true : FieldValue.delete());//campo a actualizar y con que valor
                //si el hashmpa no tiene ese like, se lo pones.
                //sino lo tiene, lo eliminas del hashmap
            }
        });
        if(postsList.get(position).getLikes()!=null){
            //si el user tiene el like en el hashmap
            holder.binding.favorito.setChecked(postsList.get(position).getLikes().containsKey(FirebaseAuth.getInstance().getUid()));
        }

    }

    @Override
    public int getItemCount() {
        if(postsList==null){return 0;}
        else{return postsList.size();}

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ViewholderPostBinding binding;

        public MyViewHolder(ViewholderPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
