package com.fahrul.covid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fahrul.covid.model.DataSPUCovidModel;
import com.fahrul.covid.utility.SharedPrefUtil;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

public class HalamanUtama extends AppCompatActivity {

    CardView btnUpdate, btnLihat;
    TextView txtKondisi, txtKet;
    ImageView imgKondisi;
    private int REQUEST_CODE = 100;
    String time = "";
    String jam = "1600140708";
    LinearLayout linWar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);

        linWar = findViewById(R.id.linWar);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnLihat = findViewById(R.id.btnLihat);
        txtKondisi = findViewById(R.id.txtKondisi);
        txtKet = findViewById(R.id.txtKet);
        imgKondisi = findViewById(R.id.imgKondisi);

        String json = SharedPrefUtil.getInstance(HalamanUtama.this).getString("data_input");
        DataSPUCovidModel dataSPUCovidModel = new Gson().fromJson(json, DataSPUCovidModel.class);

        //TimeStamp
        time = getTimeStampRemains(dataSPUCovidModel.getTimestamp());
        changeStatus(dataSPUCovidModel.getKondisi());

        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HalamanUtama.this, MapsActivity.class);
                startActivity(in);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HalamanUtama.this, UpdateStatus.class);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

    }

    private String getTimeStampRemains(String timestamp) {
//        Long tsLong = System.currentTimeMillis()/1000;
        Long ts = Long.parseLong(timestamp);
//        Long mTimeStamp = tsLong - ts;
//        time = getDate(mTimeStamp);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(ts * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

//        Date dNow = new Date( );
//        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        return date;
    }

    private String getDate(long time) {
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", time).toString();
        return date;
    }

    public void changeStatus(String mKondisi) {
        switch (mKondisi) {
            case "sehat":
//                cvStatus.setCardBackgroundColor(Color.parseColor("#FFC1F486"));
                linWar.setBackgroundResource(R.drawable.shape);
                txtKondisi.setText("SEHAT");
                imgKondisi.setImageResource(R.drawable.ic_emoji);
                txtKet.setText("Terakhir update " + time);


                break;
            case "sakit":
//                cvStatus.setCardBackgroundColor(Color.parseColor("#FFFFF6A4"));
                linWar.setBackgroundResource(R.drawable.shape1);
                txtKondisi.setText("SAKIT");
                imgKondisi.setImageResource(R.drawable.ic_sick);
                txtKet.setText("Terakhir update " + time);

                break;
            case "covid":
//                cvStatus.setCardBackgroundColor(Color.parseColor("#FFFF9A9A"));
                linWar.setBackgroundResource(R.drawable.shape2);
                txtKondisi.setText("COVID");
                imgKondisi.setImageResource(R.drawable.ic_fever);
                txtKet.setText("Terakhir update " + time);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == UpdateStatus.RESULT_CODE) {
                time = getTimeStampRemains(data.getStringExtra("timeStamp"));
                changeStatus(data.getStringExtra("kondisi"));
            }
        }
    }
}