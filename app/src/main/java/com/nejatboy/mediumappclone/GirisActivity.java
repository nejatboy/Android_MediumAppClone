package com.nejatboy.mediumappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class GirisActivity extends AppCompatActivity {

    private EditText editTextKullaniciAdi, editTextSifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        editTextKullaniciAdi = findViewById(R.id.editTextKullaniciAdi);
        editTextSifre = findViewById(R.id.editTextSifre);

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && parseUser.getUsername() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }




    public void buttonKayitOl (View view) {
        ParseUser parseUser = new ParseUser();
        String kullaniciAdi = editTextKullaniciAdi.getText().toString();
        String sifre = editTextSifre.getText().toString();

        if (kullaniciAdi.equals("") || sifre.equals("")) {
            Toast.makeText(this, "Lütfen kullanıcı adı ve şifre giriniz.", Toast.LENGTH_SHORT).show();
        } else {
            parseUser.setUsername(kullaniciAdi);
            parseUser.setPassword(sifre);
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(GirisActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GirisActivity.this, "Kayıt başarılı.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }




    public void buttonGirisYap (View view) {
        String kullaniciAdi = editTextKullaniciAdi.getText().toString();
        String sifre = editTextSifre.getText().toString();
        ParseUser.logInInBackground(kullaniciAdi, sifre, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast.makeText(GirisActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GirisActivity.this, "Giriş başarılı.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
