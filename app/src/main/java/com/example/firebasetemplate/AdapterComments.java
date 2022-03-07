package com.example.firebasetemplate;

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

import java.util.ArrayList;
import java.util.List;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder>{

    private Context context;
    private List<Comment> commentList;
    NavController navController;

    public AdapterComments(Context context, List<Comment> commentList, NavController navController) {
        this.context = context;
        this.commentList = commentList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("creating viewholder");
        return new MyViewHolder(LayoutCommentBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        System.out.println(comment.getContenido());
        holder.binding.textComment.setText(commentList.get(position).getContenido());
        holder.binding.nameCommenter.setText(commentList.get(position).getNombreUsuario());

        if(commentList.get(position).getPhoto()!=null){
            Glide.with(context).load(commentList.get(position).getPhoto()).into(holder.binding.iconAuthorComment);
        }
        else{
            holder.binding.iconAuthorComment.setImageResource(R.drawable.ic_baseline_face_24);
        }

    }

    @Override
    public int getItemCount() {
        System.out.println("SIZWEEEEE " + commentList.size());
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
