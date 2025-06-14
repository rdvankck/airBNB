package com.emlakuygulamasi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.emlakuygulamasi.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository = new AuthRepository();

    public LiveData<FirebaseUser> getUserLiveData() { return authRepository.getUserLiveData(); }
    public LiveData<String> getErrorLiveData() { return authRepository.getErrorLiveData(); }
    public void register(String email, String password) { authRepository.register(email, password); }
    public void login(String email, String password) { authRepository.login(email, password); }
    public void logout() { authRepository.logout(); }
} 