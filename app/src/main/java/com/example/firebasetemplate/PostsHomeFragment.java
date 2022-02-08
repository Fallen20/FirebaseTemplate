package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebasetemplate.databinding.FragmentPostsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PostsHomeFragment extends AppFragment {

    private FragmentPostsBinding binding;

    private List<Posts> postsArrayList=new ArrayList<>();
    private AdapterRecyclerHome adapterRecyclerHome;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentPostsBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(v -> navController.navigate(R.id.newPostFragment));
        adapterRecyclerHome=new AdapterRecyclerHome(requireContext(), postsArrayList);

        binding.postsRecyclerView.setAdapter(adapterRecyclerHome);//al principio estara vacio porque se llena abajo, pero al cabo de unos segundos se llena

        //cuando se inicie la pantalla, que mire la base de datosy los muestre
        //que coleccion? post. Y de cada uno, todos los datos. get() solo mira los post una vez
        //con addSnapshotListener lo mira a tiempo real
        db.collection("posts")
                        .addSnapshotListener((collectionSnapshot, e)->{
                            //convertir cada documento en clase posts para mostrarlos con ese formato
                            for(DocumentSnapshot a:collectionSnapshot){
                               // a.toObject(Posts.class);//lo pasas a esa clase
                                postsArrayList.add(a.toObject(Posts.class));//añadir para el recycler
                            }
                            adapterRecyclerHome.notifyDataSetChanged();//esto es que notifica que ha cambiado y se vuelve a ejecutar
                        });

    }
}