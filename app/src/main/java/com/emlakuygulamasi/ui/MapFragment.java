package com.emlakuygulamasi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.emlakuygulamasi.R;
import com.emlakuygulamasi.model.Ilan;
import com.emlakuygulamasi.viewmodel.ListingViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ListingViewModel listingViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        listingViewModel = new ViewModelProvider(this).get(ListingViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng istanbul = new LatLng(41.0082, 28.9784);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul, 10));
        listingViewModel.getIlanlarLiveData().observe(getViewLifecycleOwner(), this::addMarkersToMap);
        listingViewModel.fetchIlanlar();
        mMap.setOnInfoWindowClickListener(marker -> {
            String ilanId = (String) marker.getTag();
            if (ilanId != null) {
                // Detay ekranına geçiş
                // Intent intent = new Intent(getActivity(), DetailActivity.class);
                // intent.putExtra("ILAN_ID", ilanId);
                // startActivity(intent);
            }
        });
    }
    private void addMarkersToMap(List<Ilan> ilanlar) {
        if (mMap == null || ilanlar == null) return;
        mMap.clear();
        for (Ilan ilan : ilanlar) {
            if (ilan.getKonum() != null) {
                LatLng konum = new LatLng(ilan.getKonum().getLatitude(), ilan.getKonum().getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(konum)
                        .title(ilan.getBaslik())
                        .snippet(String.format("₺ %,.0f", ilan.getFiyat())));
                marker.setTag(ilan.getDocumentId());
            }
        }
    }
} 