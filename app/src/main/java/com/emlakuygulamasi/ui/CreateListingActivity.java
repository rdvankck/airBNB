package com.emlakuygulamasi.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.emlakuygulamasi.R;
import com.emlakuygulamasi.adapter.ImagePreviewAdapter;
import com.emlakuygulamasi.model.Ilan;
import com.emlakuygulamasi.viewmodel.ListingViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;
import java.util.List;

public class CreateListingActivity extends AppCompatActivity {
    private EditText editTextBaslik, editTextAciklama, editTextFiyat, editTextOdaSayisi;
    private Button buttonSelectImages, buttonSaveIlan, buttonSelectLocation;
    private RecyclerView recyclerViewImagePreview;
    private ProgressBar progressBarCreate;
    private ListingViewModel listingViewModel;
    private List<Uri> selectedImageUris = new ArrayList<>();
    private ImagePreviewAdapter imagePreviewAdapter;
    private GeoPoint selectedLocation = null;
    private ActivityResultLauncher<String> mGetContent;
    private ActivityResultLauncher<Intent> locationPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        editTextBaslik = findViewById(R.id.editTextBaslik);
        editTextAciklama = findViewById(R.id.editTextAciklama);
        editTextFiyat = findViewById(R.id.editTextFiyat);
        editTextOdaSayisi = findViewById(R.id.editTextOdaSayisi);
        buttonSelectImages = findViewById(R.id.buttonSelectImages);
        buttonSaveIlan = findViewById(R.id.buttonSaveIlan);
        buttonSelectLocation = findViewById(R.id.buttonSelectLocation);
        recyclerViewImagePreview = findViewById(R.id.recyclerViewImagePreview);
        progressBarCreate = findViewById(R.id.progressBarCreate);
        listingViewModel = new ViewModelProvider(this).get(ListingViewModel.class);
        setupRecyclerView();
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
            if (uris != null && !uris.isEmpty()) {
                selectedImageUris.clear();
                selectedImageUris.addAll(uris);
                imagePreviewAdapter.notifyDataSetChanged();
            }
        });
        locationPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        double latitude = result.getData().getDoubleExtra("latitude", 0);
                        double longitude = result.getData().getDoubleExtra("longitude", 0);
                        selectedLocation = new GeoPoint(latitude, longitude);
                        Toast.makeText(this, "Konum başarıyla seçildi.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        buttonSelectImages.setOnClickListener(v -> mGetContent.launch("image/*"));
        buttonSelectLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectLocationActivity.class);
            locationPickerLauncher.launch(intent);
        });
        buttonSaveIlan.setOnClickListener(v -> saveIlan());
        listingViewModel.getCreationStatusLiveData().observe(this, success -> {
            progressBarCreate.setVisibility(View.GONE);
            buttonSaveIlan.setEnabled(true);
            if (success) {
                Toast.makeText(this, "İlan başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Hata: İlan kaydedilemedi.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupRecyclerView() {
        imagePreviewAdapter = new ImagePreviewAdapter(selectedImageUris);
        recyclerViewImagePreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImagePreview.setAdapter(imagePreviewAdapter);
    }
    private void saveIlan() {
        String baslik = editTextBaslik.getText().toString().trim();
        String aciklama = editTextAciklama.getText().toString().trim();
        String fiyatStr = editTextFiyat.getText().toString().trim();
        String odaSayisiStr = editTextOdaSayisi.getText().toString().trim();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (baslik.isEmpty() || aciklama.isEmpty() || fiyatStr.isEmpty() || odaSayisiStr.isEmpty() || selectedImageUris.isEmpty() || selectedLocation == null) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun, en az bir fotoğraf ve konum seçin.", Toast.LENGTH_LONG).show();
            return;
        }
        double fiyat = Double.parseDouble(fiyatStr);
        int odaSayisi = Integer.parseInt(odaSayisiStr);
        progressBarCreate.setVisibility(View.VISIBLE);
        buttonSaveIlan.setEnabled(false);
        Ilan yeniIlan = new Ilan(baslik, aciklama, fiyat, odaSayisi, userId, null);
        yeniIlan.setKonum(selectedLocation);
        listingViewModel.createIlan(yeniIlan, selectedImageUris);
    }
} 