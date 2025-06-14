package com.emlakuygulamasi.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public MutableLiveData<String> getErrorLiveData() { return errorLiveData; }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userLiveData.postValue(firebaseAuth.getCurrentUser());
                } else {
                    errorLiveData.postValue(task.getException().getMessage());
                }
            });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userLiveData.postValue(firebaseAuth.getCurrentUser());
                } else {
                    errorLiveData.postValue(task.getException().getMessage());
                }
            });
    }

    public void logout() {
        firebaseAuth.signOut();
        userLiveData.postValue(null);
    }
} 