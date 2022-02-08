package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentNewPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Date;

public class NewPostFragment extends AppFragment {

    private FragmentNewPostBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNewPostBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.previsualizacion.setOnClickListener(v -> seleccionarImagen());

        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            Glide.with(this).load(uri).into(binding.previsualizacion);
        });

        binding.publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pillamos la info de lo escrito+la foto y la ponemos en la base de datos
                //la base de datos trabaja con mongo
                //coleccion>tabla
                //doc>fila

                binding.publicar.setEnabled(false);//ahora no puede volver a apretar para volver a subir

                Posts posts=new Posts(
                            binding.contenido.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),//si es con email no sirve, solo con google
                            LocalDate.now().toString()
                        );


                FirebaseFirestore.getInstance().collection("posts").add(posts)
                                .addOnCompleteListener(task->{
                                    binding.publicar.setEnabled(true);//que no se vuelve a activar hasta que se suba a la base de datos
                                    navController.popBackStack();//y que vuelva atras
                                });//add es una fila


            }
        });
    }

    private void seleccionarImagen() {
        galeria.launch("image/*");
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        appViewModel.setUriImagenSeleccionada(uri);
    });
}