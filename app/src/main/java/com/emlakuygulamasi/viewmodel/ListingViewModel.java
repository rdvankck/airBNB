package com.emlakuygulamasi.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.emlakuygulamasi.model.Ilan;
import com.emlakuygulamasi.repository.ListingRepository;
import java.util.List;

public class ListingViewModel extends ViewModel {
    private ListingRepository repository = new ListingRepository();
    public LiveData<List<Ilan>> getIlanlarLiveData() { return repository.getIlanlarLiveData(); }
    public LiveData<String> getErrorLiveData() { return repository.getErrorLiveData(); }
    public LiveData<Boolean> getCreationStatusLiveData() { return repository.getCreationStatusLiveData(); }
    public LiveData<Ilan> getIlanDetayLiveData() { return repository.getIlanDetayLiveData(); }
    public void fetchIlanlar() { repository.fetchIlanlar(); }
    public void createIlan(Ilan ilan, List<Uri> imageUris) { repository.createIlan(ilan, imageUris); }
    public void fetchIlanDetay(String ilanId) { repository.fetchIlanDetay(ilanId); }
} 