package com.example.firebasetemplate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.TokenWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasetemplate.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterFragment extends AppFragment {
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding.verifyEmailButton.setOnClickListener(v -> {});

        binding.createAccountButton.setOnClickListener(v -> {
            if (binding.emailEditText.getText().toString().isEmpty()) {
                binding.emailEditText.setError("Este campo no puede estar nulo");
                return;
            }//si esta vacio para que no pete

            else if (binding.passwordEditText.getText().toString().isEmpty()) {
                binding.passwordEditText.setError("Este campo no puede estar nulo");
                return;
            }//si esta vacio para que no pete
            else {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(
                                binding.emailEditText.getText().toString(),
                                binding.passwordEditText.getText().toString()
                        )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //si se ha registrado correctamente, logealo
                                    navController.navigate(R.id.action_registerFragment_to_postsHomeFragment);
                                } else {
                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


        });
    }
}