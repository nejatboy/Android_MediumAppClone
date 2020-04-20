package com.nejatboy.mediumappclone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProfilFragment extends Fragment {

    private CircularImageView circularImageView;
    private TextView textViewKullaniciAdi;
    private Button buttonSifreDegistir;
    private ListView listView;

    private List<Gonderi> gonderim = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private Dialog dialogResimSec;
    private Button buttonResimEkle;
    private ImageView imageViewPopup;

    private Bitmap secilenBitmapResim;
    private Bitmap indirilenBitmap;
    private Uri imageData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        verileriCek();
        profilVerileriniCek();

        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        circularImageView = view.findViewById(R.id.circularImageViewProfil);
        textViewKullaniciAdi = view.findViewById(R.id.textViewKullaniciAdi);
        buttonSifreDegistir = view.findViewById(R.id.buttonSifreDegistir);
        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), gonderim.get(position).getBaslik(), Toast.LENGTH_SHORT).show();
            }
        });

        textViewKullaniciAdi.setText(ParseUser.getCurrentUser().getUsername());

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogResimSec = new Dialog(getContext());
                dialogResimSec.setContentView(R.layout.popup_resim_secme);
                dialogResimSec.show();

                imageViewPopup = dialogResimSec.findViewById(R.id.imageViewPopUp);
                buttonResimEkle = dialogResimSec.findViewById(R.id.buttonResimEkle);


                buttonSifreDegistir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View alertTasarim = getLayoutInflater().inflate(R.layout.alert_sifre_degistir, null);
                        final EditText editTextYeniSifre = alertTasarim.findViewById(R.id.editTextAlertYeniSifre);
                        final EditText editTextYeniSifreTekrar = alertTasarim.findViewById(R.id.editTextAlertYeniSifreTekrar);
                        Button buttonDegistir = alertTasarim.findViewById(R.id.buttonAlertSifreDegistir);

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Şifre Değiştir");
                        alert.setView(alertTasarim);

                        alert.setPositiveButton("Değiştir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editTextYeniSifre.getText().toString().equals(editTextYeniSifreTekrar.getText().toString())) {
                                    ParseUser user = ParseUser.getCurrentUser();
                                    user.setPassword(editTextYeniSifre.getText().toString());
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Toast.makeText(getContext(), "Şifreniz değiştirildi.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    
                                } else {
                                    Toast.makeText(getContext(), "Hata oluştur. Lütfen kontrol edin ve tekrar deneyin.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alert.show();
                    }
                });



                imageViewPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {  //İzin vrilmemişse (iste)
                            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        } else {    //İzin verilmişsie (Galeriye git)
                            Intent galeriyeGit = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galeriyeGit, 2);
                        }
                    }
                });

                buttonResimEkle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (secilenBitmapResim != null) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            secilenBitmapResim.compress(Bitmap.CompressFormat.JPEG,  50, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            ParseFile parseFileResim = new ParseFile("resim.jpg", bytes);

                            ParseObject object = new ParseObject("ProfilResimleri");
                            object.put("kullanicilar", ParseUser.getCurrentUser().getObjectId());
                            object.put("resimler", parseFileResim);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Resim eklendi", Toast.LENGTH_SHORT).show();
                                        dialogResimSec.dismiss();
                                        profilVerileriniCek();
                                    }
                                }
                            });
                        }else  {
                            Toast.makeText(getContext(), "Lütfen Resim Seçiniz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }


    public void verileriCek () {
        ParseQuery<ParseObject> sorgu = ParseQuery.getQuery("Gonderiler").whereEqualTo("kullanici", ParseUser.getCurrentUser().getUsername()).orderByDescending("createdAt");
        sorgu.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        ArrayList<String> gonderiBasliklari = new ArrayList<>();
                        for (ParseObject object: objects) {
                            String baslik = object.getString("baslik");
                            int begeni = object.getInt("begeni");
                            String id = object.getObjectId();
                            String icerik = object.getString("icerik");
                            String kullaniciAdi = object.getString("kullanici");

                            gonderim.add(new Gonderi(id, icerik, baslik, begeni, kullaniciAdi));
                            gonderiBasliklari.add(baslik + "");
                        }
                        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, gonderiBasliklari);
                        listView.setAdapter(adapter);
                    }
                } else {    //hata varsa
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void profilVerileriniCek() {
        ParseQuery<ParseObject> sorgu = ParseQuery.getQuery("ProfilResimleriniCek").whereEqualTo("kullanici", ParseUser.getCurrentUser().getUsername()).orderByDescending("createdAt");
        sorgu.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object: objects) {
                            ParseFile resim = (ParseFile) object.get("resimler");
                            resim.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {    //Buraya da girerse resmi çekebiliriz demektir
                                        indirilenBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        circularImageView.setImageBitmap(indirilenBitmap);

                                    }
                                }
                            });
                        }
                    }
                } else {    //hata varsa
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galeriyeGit = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeriyeGit, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageData);
                    secilenBitmapResim = ImageDecoder.decodeBitmap(source);
                } else {
                    secilenBitmapResim = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageData);
                }
                secilenBitmapResim = resmiKucult(secilenBitmapResim, 500);
                imageViewPopup.setImageBitmap(secilenBitmapResim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public Bitmap resmiKucult(Bitmap image, int maximumBuyukluk) {
        int genislik = image.getWidth();
        int yukseklik = image.getHeight();

        float resimOrani = (float) genislik / (float) yukseklik;
        if (resimOrani > 1) {   //resim yatay ise
            genislik = maximumBuyukluk;
            yukseklik = (int) (genislik / resimOrani);

        } else { //resim dikey ise
            yukseklik = maximumBuyukluk;
            genislik = (int) (yukseklik * resimOrani);
        }

        return Bitmap.createScaledBitmap(image, genislik, yukseklik, true);
    }
}
