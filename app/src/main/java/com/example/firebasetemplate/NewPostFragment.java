package com.example.firebasetemplate;

import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDate;
import java.util.UUID;

public class NewPostFragment extends AppFragment {
    private Uri uriImagen;
    private FragmentNewPostBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNewPostBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel.setUriImagenSeleccionada(null);
        binding.previsualizacion.setOnClickListener(v -> seleccionarImagen());

        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {//cuando cambie, la cargas en el imageview
           if(uri!=null){
               uriImagen = uri;
               Glide.with(this).load(uri).into(binding.previsualizacion);
           }
        });

        binding.publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pillamos la info de lo escrito+la foto y la ponemos en la base de datos
                //la base de datos trabaja con mongo
                //coleccion>tabla
                //doc>fila

                binding.publicar.setEnabled(false);//ahora no puede volver a apretar para volver a subir

                //si tiene fotos, la guardas
                if (uriImagen != null) {
                    FirebaseStorage.getInstance().getReference("/images/" + UUID.randomUUID() + ".jpg")//la ruta
                            .putFile(uriImagen)//urimagen es uri, pero como no podemos acceder hacemos una globar que tenga ese contenido
                            .continueWithTask(task -> task.getResult().getStorage().getDownloadUrl())
                            .addOnSuccessListener(url -> {//solo se ejecuta si sale bien

                                String photo = null;
                                if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){//si tiene foto de perfil
                                    //texto+img
                                    photo = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                                }


                                Posts posts = new Posts(
                                        binding.contenido.getText().toString(),
                                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),//si es con displayName no sirve con email, solo con google
                                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                        LocalDate.now().toString(),
                                        url.toString(),
                                        photo
                                );


                                FirebaseFirestore.getInstance().collection("posts").add(posts)
                                        .addOnCompleteListener(task -> {
                                            binding.publicar.setEnabled(true);//que no se vuelve a activar hasta que se suba a la base de datos
                                            appViewModel.setUriImagenSeleccionada(null);
                                            navController.popBackStack();//y que vuelva atras
                                        });//add es una fila


                            });
                } else {//no tiene fotos en el post

                    //texto

                    String photo = null;
                    if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){//si tiene foto de perfil
                        //texto+img
                        photo = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                    }

                    Posts posts = new Posts(
                            binding.contenido.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),//si es con displayName no sirve con email, solo con google
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            LocalDate.now().toString(),
                            photo
                    );


                    FirebaseFirestore.getInstance().collection("posts").add(posts)
                            .addOnCompleteListener(task -> {
                                binding.publicar.setEnabled(true);//que no se vuelve a activar hasta que se suba a la base de datos
                                appViewModel.setUriImagenSeleccionada(null);
                                navController.popBackStack();//y que vuelva atras
                            });//add es una fila



            }

                //cuando acabas, quitas la foto

            //no se pueden llamar igual o sino se sobrescribe t odo el rato, asi que le damos un numero solo random


        }
    });
}

    private void seleccionarImagen() {
        galeria.launch("image/*");//abre la libreria
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        //si le pones aqui uriUmagen=uri, al girar el movil has de volver a seleccionarla
        appViewModel.setUriImagenSeleccionada(uri);//le pasas la uri de la foto (direccion)
    });
}