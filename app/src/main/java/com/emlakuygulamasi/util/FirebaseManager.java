package com.emlakuygulamasi.util;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.emlakuygulamasi.model.Chat;
import com.emlakuygulamasi.model.Message;
import com.emlakuygulamasi.model.Ilan;
import com.emlakuygulamasi.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private static FirebaseManager instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage mStorage;
    private CollectionReference usersRef;
    private CollectionReference propertiesRef;
    private CollectionReference chatsRef;

    private FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        usersRef = firestore.collection("users");
        propertiesRef = firestore.collection("ilanlar");
        chatsRef = firestore.collection("chats");
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    // Example: User registration
    public void registerUser(String email, String password, final OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    // Example: User login
    public void loginUser(String email, String password, final OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    // Example: Add property
    public void addProperty(Ilan ilan, final OnCompleteListener<DocumentReference> listener) {
        propertiesRef.add(ilan).addOnCompleteListener(listener);
    }

    // Example: Upload image
    public void uploadImage(Uri imageUri, final OnCompleteListener<Uri> listener) {
        final StorageReference imageRef = mStorage.getReference().child("ilan_gorselleri/" + UUID.randomUUID().toString());
        imageRef.putFile(imageUri)
            .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnCompleteListener(listener));
    }

    // Example: Send message
    public void sendMessage(String chatId, Message message, final OnCompleteListener<DocumentReference> listener) {
        chatsRef.document(chatId).collection("messages").add(message).addOnCompleteListener(listener);
    }

    // Kullanıcı bilgilerini alma
    public void getUser(String userId, final OnCompleteListener<DocumentSnapshot> listener) {
        usersRef.document(userId).get().addOnCompleteListener(listener);
    }

    // Mesajları alma
    public void getMessages(String chatId, final OnCompleteListener<QuerySnapshot> listener) {
        chatsRef.document(chatId).collection("messages").get().addOnCompleteListener(listener);
    }

    // İlanları alma
    public void getProperties(final OnCompleteListener<QuerySnapshot> listener) {
        propertiesRef.get().addOnCompleteListener(listener);
    }

    // Kullanıcı çıkışı
    public void logoutUser() {
        mAuth.signOut();
    }

    // ... Diğer işlemler için benzer metotlar eklenebilir ...
} 