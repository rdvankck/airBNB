package com.emlakuygulamasi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.emlakuygulamasi.R;
import com.emlakuygulamasi.adapter.IlanAdapter;
import com.emlakuygulamasi.model.Ilan;
import com.emlakuygulamasi.viewmodel.ListingViewModel;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private IlanAdapter adapter;
    private ProgressBar progressBar;
    private ListingViewModel listingViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewIlanlar);
        progressBar = view.findViewById(R.id.progressBarList);
        listingViewModel = new ViewModelProvider(this).get(ListingViewModel.class);
        setupRecyclerView();
        observeViewModel();
        listingViewModel.fetchIlanlar();
        return view;
    }
    private void setupRecyclerView() {
        adapter = new IlanAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ilan -> {
            // Detay ekranına geçiş
            Toast.makeText(getContext(), "Detay ekranına geçilecek", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(getContext(), DetailActivity.class);
            // intent.putExtra("ILAN_ID", ilan.getDocumentId());
            // startActivity(intent);
        });
    }
    private void observeViewModel() {
        listingViewModel.getIlanlarLiveData().observe(getViewLifecycleOwner(), ilanlar -> {
            adapter.setIlanlar(ilanlar != null ? ilanlar : new ArrayList<>());
            progressBar.setVisibility(View.GONE);
        });
        listingViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), "Hata: " + error, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });
    }
} 