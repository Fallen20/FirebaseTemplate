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
        holder.binding.autor.setText(postsList.get(position).getAuthorName());
        holder.binding.contenido.setText(postsList.get(position).getContent());
        Glide.with(context).load(postsList.get(position).getUrlDescarga()).into(holder.binding.imagen);//igual que picaso
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
