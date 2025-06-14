package com.emlakuygulamasi.model;

import com.google.firebase.firestore.GeoPoint;
import java.util.List;

public class Ilan {
    private String documentId;
    private String baslik;
    private String aciklama;
    private double fiyat;
    private int odaSayisi;
    private String kullaniciId;
    private List<String> fotografUrls;
    private GeoPoint konum;

    public Ilan() {}

    public Ilan(String baslik, String aciklama, double fiyat, int odaSayisi, String kullaniciId, List<String> fotografUrls) {
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
        this.odaSayisi = odaSayisi;
        this.kullaniciId = kullaniciId;
        this.fotografUrls = fotografUrls;
    }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }
    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }
    public double getFiyat() { return fiyat; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }
    public int getOdaSayisi() { return odaSayisi; }
    public void setOdaSayisi(int odaSayisi) { this.odaSayisi = odaSayisi; }
    public String getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(String kullaniciId) { this.kullaniciId = kullaniciId; }
    public List<String> getFotografUrls() { return fotografUrls; }
    public void setFotografUrls(List<String> fotografUrls) { this.fotografUrls = fotografUrls; }
    public GeoPoint getKonum() { return konum; }
    public void setKonum(GeoPoint konum) { this.konum = konum; }
} 