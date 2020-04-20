package com.nejatboy.mediumappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTutucu, new AnasayfaFragment()).commit();   //Başlangıçta fragmenti

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment secilenFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.anasayfaNav:
                        secilenFragment = new AnasayfaFragment();
                        break;

                    case R.id.favorilerNav:
                        secilenFragment = new FavorilerFragment();
                        break;

                    case R.id.profilNav:
                        secilenFragment = new ProfilFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTutucu, secilenFragment).commit();
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ana_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionYeniGonderi) {
            //YENİ GÖNDERİ YAPILACAK
            dialog = new Dialog(this);
            Button buttonGonder;
            final EditText editTextBaslik, editTextIcerik;

            dialog.setContentView(R.layout.popup_yeni_gonderi);
            editTextBaslik = dialog.findViewById(R.id.editTextBaslik);
            editTextIcerik = dialog.findViewById(R.id.editTextIcerik);
            buttonGonder = dialog.findViewById(R.id.buttonGonder);
            dialog.show();

            buttonGonder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextBaslik.getText().toString().equals("") || editTextIcerik.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Lütfen başlık ve içeriğini yazınız.", Toast.LENGTH_SHORT).show();
                    } else {
                        //Veri kaydetmek    (ID'yi kendi otomatik string olarak UUID olarak kaydeder)
                        ParseObject object = new ParseObject("Gonderiler");
                        object.put("kullanici", ParseUser.getCurrentUser().getUsername());
                        object.put("baslik", editTextBaslik.getText().toString());
                        object.put("icerik", editTextIcerik.getText().toString());
                        object.put("begeni", 0);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {    //Hata yoksa
                                    Toast.makeText(MainActivity.this, "Gönderiniz Eklendi.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {    //Hata varsa
                                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });



        } else if (item.getItemId() == R.id.actionCikisYap) {
            ParseUser.logOut();
            Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), GirisActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
