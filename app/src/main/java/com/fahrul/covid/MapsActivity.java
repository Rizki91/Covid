package com.fahrul.covid;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fahrul.covid.model.DataSPUCovidModel;
import com.fahrul.covid.model.getall.Covid;
import com.fahrul.covid.model.getall.GetAllModel;
import com.fahrul.covid.service.APIClient;
import com.fahrul.covid.service.APIInterfacesRest;
import com.fahrul.covid.utility.SharedPrefUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.location.aravind.getlocation.GeoLocator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    Double lat = 0.0, lon = 0.0,  radius = 50.0;;
    LinearLayout linlayKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        linlayKembali = findViewById(R.id.linlayKembali);
        linlayKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {

//        GeoLocator geoLocator = new GeoLocator(getApplicationContext(),this);
//        lat = geoLocator.getLattitude();
//        lon = geoLocator.getLongitude();
//
        String json = SharedPrefUtil.getInstance(MapsActivity.this).getString("data_input");
        DataSPUCovidModel dataSPUCovidModel = new Gson().fromJson(json, DataSPUCovidModel.class);

        mMap = map;

        // Add a marker in MyLocation and move the camera
        float zoomLevel = 16.0f; //This goes up to 21
        LatLng myLocation = new LatLng(dataSPUCovidModel.getLat(),dataSPUCovidModel.getLon());
        mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .title("Lokasi Saya"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoomLevel)); // zoom


        mMap.addCircle(new CircleOptions() // radius cicle
                .center(myLocation)
                .radius(radius)
                .strokeWidth(0f)
                .fillColor(0x550000FF));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoomLevel)); // zoom

        getAll();

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    List<Covid> list = new ArrayList<>();
    public void getAll(){
        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<GetAllModel> call3 = apiInterface.GeatAll();
        call3.enqueue(new Callback<GetAllModel>() {
            @Override
            public void onResponse(Call<GetAllModel> call, Response<GetAllModel> response) {
                progressDialog.dismiss();
                GetAllModel listCovid = response.body();
                float[] dis = new float[1];

                for (int i=0;i<listCovid.getData().getCovid().size();i++){

                    // if marker in radius google maps
                    Location.distanceBetween(
                            Double.parseDouble(listCovid.getData().getCovid().get(i).getLat()),
                            Double.parseDouble(listCovid.getData().getCovid().get(i).getLon()),
                            lat,
                            lon,
                            dis);

                    if( dis[0] > radius  ){
                    } else {
                        list.add(listCovid.getData().getCovid().get(i));
                    }
                }

                if (listCovid !=null) {
                    for (int i=0;i<listCovid.getData().getCovid().size();i++){
                        list.add(listCovid.getData().getCovid().get(i));
                    }

                    // Add some markers to the map, and add a data object to each marker.
                    for (int x=0; x<list.size(); x++){
                        LatLng latLng = new LatLng(Double.parseDouble(list.get(x).getLat()), Double.parseDouble(list.get(x).getLon()));
                        float color;

                        switch (list.get(x).getKondisi()) {
                            case "sehat":
                                color = BitmapDescriptorFactory.HUE_GREEN;
                                break;
                            case "sakit":
                                    color = BitmapDescriptorFactory.HUE_ORANGE;
                                break;
                            case "covid":
                                color = BitmapDescriptorFactory.HUE_RED;
                                break;
                            default:
                                color = BitmapDescriptorFactory.HUE_CYAN;
                                break;
                        }
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(color))
                                .title(list.get(x).getUsername()));


//            mPerth.setTag(0);
                    }
                    Toast.makeText(MapsActivity.this, "berhasil ambil data", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MapsActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}