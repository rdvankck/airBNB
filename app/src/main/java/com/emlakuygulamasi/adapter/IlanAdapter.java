package com.emlakuygulamasi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.emlakuygulamasi.R;
import com.emlakuygulamasi.model.Ilan;
import java.util.ArrayList;
import java.util.List;

public class IlanAdapter extends RecyclerView.Adapter<IlanAdapter.IlanViewHolder> {
    private List<Ilan> ilanList = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public IlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ilan, parent, false);
        return new IlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IlanViewHolder holder, int position) {
        Ilan ilan = ilanList.get(position);
        holder.textViewBaslik.setText(ilan.getBaslik());
        holder.textViewFiyat.setText(String.format("₺ %,.0f", ilan.getFiyat()));
        if (ilan.getFotografUrls() != null && !ilan.getFotografUrls().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(ilan.getFotografUrls().get(0))
                    .into(holder.imageViewIlan);
        }
    }

    @Override
    public int getItemCount() { return ilanList.size(); }

    public void setIlanlar(List<Ilan> ilanlar) {
        this.ilanList = ilanlar;
        notifyDataSetChanged();
    }

    public void addProperties(List<Ilan> newProperties) {
        int startPosition = ilanList.size();
        ilanList.addAll(newProperties);
        notifyItemRangeInserted(startPosition, newProperties.size());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class IlanViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIlan;
        TextView textViewBaslik;
        TextView textViewFiyat;
        public IlanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIlan = itemView.findViewById(R.id.imageViewIlan);
            textViewBaslik = itemView.findViewById(R.id.textViewBaslik);
            textViewFiyat = itemView.findViewById(R.id.textViewFiyat);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(ilanList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Ilan ilan);
    }
} 