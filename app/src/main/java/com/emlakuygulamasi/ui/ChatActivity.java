package com.emlakuygulamasi.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.emlakuygulamasi.R;
import com.emlakuygulamasi.adapter.MessageAdapter;
import com.emlakuygulamasi.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton buttonSendMessage;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatRef;
    private String chatId;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String ownerId = getIntent().getStringExtra("ownerId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        setupRecyclerView();
        findOrCreateChat(ownerId);
        buttonSendMessage.setOnClickListener(v -> sendMessage());
    }
    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
    }
    private void findOrCreateChat(String ownerId) {
        List<String> participants = Arrays.asList(currentUserId, ownerId);
        db.collection("chats")
                .whereArrayContainsAny("participants", participants)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        chatId = task.getResult().getDocuments().get(0).getId();
                        loadMessages();
                    } else {
                        createNewChat(participants);
                    }
                });
    }
    private void createNewChat(List<String> participants) {
        db.collection("chats")
                .add(java.util.Collections.singletonMap("participants", participants))
                .addOnSuccessListener(documentReference -> {
                    chatId = documentReference.getId();
                    loadMessages();
                });
    }
    private void loadMessages() {
        chatRef = db.collection("chats").document(chatId).collection("messages");
        chatRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;
                    messageList.clear();
                    for (Message message : snapshots.toObjects(Message.class)) {
                        messageList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                });
    }
    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (messageText.isEmpty() || chatId == null) return;
        Message message = new Message(currentUserId, messageText);
        chatRef.add(message);
        editTextMessage.setText("");
        db.collection("chats").document(chatId)
                .update("lastMessage", messageText, "lastMessageTimestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());
    }
} 