package com.example.firebasetemplate;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.TokenWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;


public class RegisterFragment extends AppFragment {
    private FragmentRegisterBinding binding;
    private Uri uriImagen;
    private String uidUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding.verifyEmailButton.setOnClickListener(v -> {});


        binding.previsualizacionIcono.setOnClickListener(v -> seleccionarImagen());

        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {//cuando cambie, la cargas en el imageview
            if(uri!=null){
                uriImagen=uri;
                Glide.with(this).load(uri).into(binding.previsualizacionIcono);
            }
        });

        binding.createAccountButton.setOnClickListener(v -> {
            if (binding.emailEditText.getText().toString().isEmpty()) {
                binding.emailEditText.setError("Este campo no puede estar nulo");
                return;
            }//si esta vacio para que no pete

            else if (binding.passwordEditText.getText().toString().isEmpty()) {
                binding.passwordEditText.setError("Este campo no puede estar nulo");
                return;
            }//si esta vacio para que no pete
            else if(binding.usernameEditText.getText().toString().isEmpty()){
                binding.usernameEditText.setError("Este campo no puede estar nulo");
            }
            else {
                auth.createUserWithEmailAndPassword(
                        binding.emailEditText.getText().toString(),
                        binding.passwordEditText.getText().toString()
                )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //si se ha registrado correctamente, logealo

                                    //mira si ha puesto foto
                                    if(uriImagen==null){

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(binding.usernameEditText.getText().toString())
                                                .setPhotoUri(null)
                                                .build();

                                        uidUser=user.getUid();
                                        user.updateProfile(profileUpdates);

                                        //guardar
                                        db.collection("users").document(binding.emailEditText.getText().toString()).set(
                                                new User(
                                                        uidUser,
                                                        binding.usernameEditText.getText().toString(),
                                                        binding.emailEditText.getText().toString(),
                                                        null
                                                )
                                        );


                                    }
                                    else{

                                        FirebaseStorage.getInstance().getReference("/iconos/"+ UUID.randomUUID()+".jpg")
                                                .putFile(uriImagen)
                                                .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                                                .addOnSuccessListener(url -> {
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(binding.usernameEditText.getText().toString())
                                                            .setPhotoUri(url)
                                                            .build();

                                                    uidUser=user.getUid();
                                                    user.updateProfile(profileUpdates);

                                                    //guardar
                                                    db.collection("users").document(binding.emailEditText.getText().toString()).set(
                                                            new User(
                                                                    uidUser,
                                                                    binding.usernameEditText.getText().toString(),
                                                                    binding.emailEditText.getText().toString(),
                                                                    url.toString()//ERROR
                                                            )
                                                    );

                                                });


                                    }



                                    navController.navigate(R.id.action_registerFragment_to_postsHomeFragment);




                                } else {
                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


        });

    }

    void updatePorfile(){

    }

    private void seleccionarImagen() {
        galeria.launch("image/*");//abre la libreria
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        appViewModel.setUriImagenSeleccionada(uri);//le pasas la uri de la foto (direccion)
    });
}