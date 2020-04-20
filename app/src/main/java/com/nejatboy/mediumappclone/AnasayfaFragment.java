package com.nejatboy.mediumappclone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AnasayfaFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAnasayfaAdapter adapter;
    private List<Gonderi> gonderiler = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        verileriCek();

        View view = inflater.inflate(R.layout.fragment_anasayfa, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return view;
    }


    public void verileriCek() {
        ParseQuery<ParseObject> sorgu = ParseQuery.getQuery("Gonderiler").orderByDescending("createdAt");
        sorgu.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object: objects) {
                            String baslik = object.getString("baslik");
                            int begeni = object.getInt("begeni");
                            String id = object.getObjectId();
                            String icerik = object.getString("icerik");
                            String kullaniciAdi = object.getString("kullanici");

                            gonderiler.add(new Gonderi(id, icerik, baslik, begeni, kullaniciAdi));
                        }
                        adapter = new RecyclerAnasayfaAdapter(getContext(), gonderiler);
                        recyclerView.setAdapter(adapter);
                    }
                } else {    //hata varsa
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
