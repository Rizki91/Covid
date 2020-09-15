package com.fahrul.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HalamanUtama extends AppCompatActivity {

    CardView btnUpdate,btnLihat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnLihat = findViewById(R.id.btnLihat);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HalamanUtama.this,UpdateStatus.class);
                startActivity(i);
            }
        });

    }
}