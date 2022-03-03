package com.example.firebasetemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.firebasetemplate.databinding.FragmentSignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInFragment extends AppFragment {

    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentSignInBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signInProgressBar.setVisibility(View.GONE);

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))//el toquen para identificar
                .requestEmail()//lo que pides a google para comparar
                .build());

        firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(requireContext()));

        binding.googleSignIn.setOnClickListener(view1 -> {
            signInClient.launch(googleSignInClient.getSignInIntent());
        });


        binding.goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signInFragment_to_registerFragment);
            }
        });

        binding.emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.email.getText().toString().isEmpty()) {
                    binding.email.setError("Este campo no puede estar nulo");
                    return;
                }//si esta vacio para que no pete

                else if (binding.password.getText().toString().isEmpty()) {
                    binding.password.setError("Este campo no puede estar nulo");
                    return;
                }//si esta vacio para que no pete
                else {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            binding.email.getText().toString(),
                            binding.password.getText().toString()
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                navController.navigate(R.id.action_signInFragment_to_postsHomeFragment);
                            }
                            else{
                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

    }

    ActivityResultLauncher<Intent> signInClient = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class));
                        //autenticar al user en el firebase con los datos que recibe
                    } catch (ApiException e) {}
                }
            });

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if(account == null) return;

        binding.signInProgressBar.setVisibility(View.VISIBLE);
        binding.googleSignIn.setVisibility(View.GONE);

        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(requireActivity(), task -> {//cuando acabe de ponerse en la firebase
                    if (task.isSuccessful()) {
                        //TODO

                        db.collection("users").document(account.getEmail()).set(
                                new User(
                                        account.getId(),//todo
                                        account.getDisplayName(),
                                        account.getEmail(),
                                        account.getPhotoUrl().toString()
                                )
                        );



                        navController.navigate(R.id.action_signInFragment_to_postsHomeFragment);
                    } else {
                        binding.signInProgressBar.setVisibility(View.GONE);
                        binding.googleSignIn.setVisibility(View.VISIBLE);
                    }
                });
    }
}