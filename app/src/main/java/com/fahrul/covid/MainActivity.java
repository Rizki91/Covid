package com.fahrul.covid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fahrul.covid.model.DataSPUCovidModel;
import com.fahrul.covid.model.Register;
import com.fahrul.covid.model.register.Data;
import com.fahrul.covid.model.register.RegisterModel;
import com.fahrul.covid.service.APIClient;
import com.fahrul.covid.service.APIInterfacesRest;
import com.fahrul.covid.utility.SharedPrefUtil;
import com.google.gson.Gson;
import com.location.aravind.getlocation.GeoLocator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.balakrishnan.easycam.CameraBundleBuilder;
import in.balakrishnan.easycam.CameraControllerActivity;
import in.balakrishnan.easycam.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText txtUsername, txtNama, txtUmur, txtKota, txtTelepon, txtKondisi, txtStatus;
    Spinner spnJk;
    TextView txtGps;
    ImageView imgPhoto;
    Button btnGambar, btnRegister;
    GeoLocator geoLocator;
    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geoLocator = new GeoLocator(getApplicationContext(), MainActivity.this);


        txtNama = findViewById(R.id.txtNama);
        txtUmur = findViewById(R.id.txtUmur);
        txtKota = findViewById(R.id.txtKota);
        txtTelepon = findViewById(R.id.txtTelepon);
//        txtGps = findViewById(R.id.txtGps);
        spnJk = findViewById(R.id.spnJk);
//        imgPhoto = findViewById(R.id.gambar);
//        btnGambar = findViewById(R.id.btn_Kamera);
        btnRegister = findViewById(R.id.btn_register);


//        btnGambar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ambilCamera();
//            }
//        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Date dNow = new Date();
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                register();
//                Intent i = new Intent(MainActivity.this,HalamanUtama.class);
//                startActivity(i);


            }
        });

    }


    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;


    File file;

    public void register() {
        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        String nama = txtNama.getText().toString();
        String arr[] = nama.split(" ", 2);
        String namadepan = arr[0];


        Long tsLong = System.currentTimeMillis()/1000;
       String ts = tsLong.toString();


//        RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/jpeg"), file);
//        MultipartBody.Part bodyImg1 =
//                MultipartBody.Part.createFormData("picture", file.getName(), requestFile1);

        Call<RegisterModel> absentAdd = apiInterface.addUser(
                toRequestBody(namadepan),
                toRequestBody("sehat"),
                toRequestBody(String.valueOf(geoLocator.getLattitude())),
                toRequestBody(String.valueOf(geoLocator.getLongitude())),
                toRequestBody(ts),
                toRequestBody("0 0 0 0 0"),
                toRequestBody(txtNama.getText().toString()),
                toRequestBody(txtUmur.getText().toString()),
                toRequestBody(spnJk.getSelectedItem().toString()),
                toRequestBody(txtKota.getText().toString()),
                toRequestBody(txtTelepon.getText().toString()),
                toRequestBody("")


        );


        absentAdd.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {

                progressDialog.dismiss();

                RegisterModel status = response.body();

                if (status != null) {

                    if (status.getStatus()) {
                        Gson gson = new Gson();
                        String json = gson.toJson(status.getData());
                        SharedPrefUtil.getInstance(MainActivity.this).put("data_input", json);

                        Intent intent = new Intent(MainActivity.this, HalamanUtama.class);
                        startActivity(intent);
                        finish();


                    } else {
                        progressDialog.dismiss();

                        try {
                            JSONObject jObjError = new JSONObject(response.body().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } else {

                    progressDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String error = jObjError.get("status_detail").toString();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

//    public void ambilCamera() {
//        Intent intent = new Intent(this, CameraControllerActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("inputData", new CameraBundleBuilder()
//                .setFullscreenMode(false)
//                .setDoneButtonString("Add")
//                .setSinglePhotoMode(false)
//                .setMax_photo(1)
//                .setManualFocus(true)
//                .setBucketName(getClass().getName())
//                .setPreviewEnableCount(true)
//                .setPreviewIconVisiblity(true)
//                .setPreviewPageRedirection(true)
//                .setEnableDone(false)
//                .setClearBucket(true)
//                .createCameraBundle());
//        startActivityForResult(intent, 214);
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 214) {
//            if (resultCode == RESULT_OK) {
//                assert data != null;
//                list = data.getStringArrayExtra("resultData");
//                file = new File(list[0]);
//                Picasso.get().load(file).into(imgPhoto);
//
////                txtGps.setText(String.valueOf(geoLocator.getLattitude()) +";"+ String.valueOf(geoLocator.getLongitude()));
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        FileUtils.clearAllFiles(this, getClass().getName());
//    }
}