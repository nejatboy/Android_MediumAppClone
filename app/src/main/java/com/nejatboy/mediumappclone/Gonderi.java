package com.nejatboy.mediumappclone;

public class Gonderi {

    private String id;
    private String icerik;
    private String baslik;
    private int begeni;
    private String kullaniciAdi;

    public Gonderi(String id, String icerik, String baslik, int begeni, String kullaniciAdi) {
        this.id = id;
        this.icerik = icerik;
        this.baslik = baslik;
        this.begeni = begeni;
        this.kullaniciAdi = kullaniciAdi;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public int getBegeni() {
        return begeni;
    }

    public void setBegeni(int begeni) {
        this.begeni = begeni;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }
}
