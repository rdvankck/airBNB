package com.emlakuygulamasi.repository;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.emlakuygulamasi.model.Ilan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class ListingRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Ilan>> ilanlarLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> creationStatusLiveData = new MutableLiveData<>();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private MutableLiveData<Ilan> ilanDetayLiveData = new MutableLiveData<>();

    public LiveData<List<Ilan>> getIlanlarLiveData() { return ilanlarLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getCreationStatusLiveData() { return creationStatusLiveData; }
    public LiveData<Ilan> getIlanDetayLiveData() { return ilanDetayLiveData; }

    public void fetchIlanlar() {
        db.collection("ilanlar").addSnapshotListener((value, error) -> {
            if (error != null) {
                errorLiveData.postValue(error.getMessage());
                return;
            }
            if (value != null) {
                List<Ilan> ilanList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Ilan ilan = doc.toObject(Ilan.class);
                    ilan.setDocumentId(doc.getId());
                    ilanList.add(ilan);
                }
                ilanlarLiveData.postValue(ilanList);
            }
        });
    }

    public void createIlan(Ilan ilan, List<Uri> imageUris) {
        if (imageUris == null || imageUris.isEmpty()) {
            saveIlanToFirestore(ilan);
            return;
        }
        List<String> downloadUrls = new ArrayList<>();
        for (int i = 0; i < imageUris.size(); i++) {
            Uri imageUri = imageUris.get(i);
            StorageReference fileReference = storage.getReference().child("ilan_gorselleri/" + System.currentTimeMillis() + "_" + i);
            fileReference.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        downloadUrls.add(task.getResult().toString());
                        if (downloadUrls.size() == imageUris.size()) {
                            ilan.setFotografUrls(downloadUrls);
                            saveIlanToFirestore(ilan);
                        }
                    } else {
                        creationStatusLiveData.postValue(false);
                    }
                });
        }
    }

    private void saveIlanToFirestore(Ilan ilan) {
        db.collection("ilanlar").add(ilan)
            .addOnSuccessListener(documentReference -> creationStatusLiveData.postValue(true))
            .addOnFailureListener(e -> creationStatusLiveData.postValue(false));
    }

    public void fetchIlanDetay(String ilanId) {
        db.collection("ilanlar").document(ilanId).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Ilan ilan = documentSnapshot.toObject(Ilan.class);
                    ilan.setDocumentId(documentSnapshot.getId());
                    ilanDetayLiveData.postValue(ilan);
                } else {
                    ilanDetayLiveData.postValue(null);
                }
            })
            .addOnFailureListener(e -> errorLiveData.postValue(e.getMessage()));
    }
} 