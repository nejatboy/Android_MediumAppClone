package com.nejatboy.mediumappclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAnasayfaAdapter extends RecyclerView.Adapter<RecyclerAnasayfaAdapter.SatirNesneTutucu>{

    private Context thisContex;
    private List<Gonderi> gelenGonderiler;

    public RecyclerAnasayfaAdapter(Context thisContex, List<Gonderi> gelenGonderiler) {
        this.thisContex = thisContex;
        this.gelenGonderiler = gelenGonderiler;
    }




    @NonNull
    @Override
    public SatirNesneTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(thisContex).inflate(R.layout.satir_tasarimi, parent, false);
        return new SatirNesneTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SatirNesneTutucu holder, int position) {
        Gonderi gonderi = gelenGonderiler.get(position);

        holder.textViewBaslik.setText(gonderi.getBaslik());
        holder.textViewBegeni.setText("" + gonderi.getBegeni());
        holder.textViewIcerik.setText(gonderi.getIcerik());
        holder.textViewKullanici.setText(gonderi.getKullaniciAdi());

        holder.buttonBegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(thisContex, "beğendin", Toast.LENGTH_SHORT).show();  //şaksdkşasldşkasşd
            }
        });

        holder.buttonFavorilereEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(thisContex, "favorilere ekledin", Toast.LENGTH_SHORT).show();        //ajdlasdjaskd
            }
        });
    }

    @Override
    public int getItemCount() {
        return gelenGonderiler.size();
    }





    //inner class
    public class SatirNesneTutucu extends RecyclerView.ViewHolder {
        TextView textViewBaslik, textViewIcerik, textViewBegeni, textViewKullanici;
        Button buttonBegen, buttonFavorilereEkle;


        public SatirNesneTutucu(@NonNull View itemView) {
            super(itemView);
            textViewBaslik = itemView.findViewById(R.id.textViewBaslik);
            textViewIcerik = itemView.findViewById(R.id.textViewIcerik);
            textViewBegeni = itemView.findViewById(R.id.textViewBegeni);
            buttonBegen = itemView.findViewById(R.id.buttonBegen);
            buttonFavorilereEkle = itemView.findViewById(R.id.buttonFavorilereEkle);
            textViewKullanici = itemView.findViewById(R.id.textViewKullanici);
        }
    }
}
