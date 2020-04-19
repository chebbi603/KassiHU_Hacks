package com.javaislove.mycoronaguide;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String filename = "file.mt";
    String line;
    public static String value;
    public String language = HomeActivity.language ;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FileInputStream in = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    value = line;
                } else {
                    value = "light";
                }
            } catch (IOException e) {
                value = "light";
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            value = "light";
            e.printStackTrace();
        }
        if (value.equals("light"))
            setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        else if (value.equals("dark"))
            setTheme(android.R.style.Theme_Material_NoActionBar);
        setContentView(R.layout.activity_report);
        ImageView entete = (ImageView) findViewById(R.id.entete);
        Button report = (Button) findViewById(R.id.report);
        Button homebtn = (Button) findViewById(R.id.homebtn);
        Button changetheme = (Button) findViewById(R.id.themebtn);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (value.equals("light")) {
            entete.setImageResource(R.drawable.entete);
            changetheme.setBackground(getResources().getDrawable(R.drawable.darkmodebtn));
        }
        else if (value.equals("dark")) {
            entete.setImageResource(R.drawable.entetedk);
            changetheme.setBackground(getResources().getDrawable(R.drawable.lightmodebtn));
        }
        if(language.equals("arabe")){
            report.setText("تبليـــغ");
        }
        else if(language.equals("francais")){
            report.setText("Signaler");
        }
        else{
            report.setText("Report");
        }
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(language.equals("arabe")){
                    Toast.makeText(v.getContext(),"حاليا منجموش نبلغو على الإخلالات هذي",Toast.LENGTH_LONG).show();
                }
                else if(language.equals("francais")){
                    Toast.makeText(v.getContext(),"Cette fonctionalité est sous développement",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(v.getContext(),"This feature is currently under development",Toast.LENGTH_LONG).show();
                }
            }
        });
        changetheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value.equals("light")) {
                    try {
                        value = "dark";
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(value.getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                    }
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (value.equals("dark")) {
                    try {
                        value = "light";
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(value.getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                    }
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    boolean b = false;
    public void onMapReady(final GoogleMap googleMap) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            final Context context = this;
            if (value.equals("light"))
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.normal));
            else if (value.equals("dark"))
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.normaldk));
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    if (!b) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(myLoc)
                                .zoom(20)
                                .bearing(90)
                                .tilt(45)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        b = true;
                    }
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                    // Clears the previously touched position
                    googleMap.clear();

                    // Animating to the touched position
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Placing a marker on the touched position
                    googleMap.addMarker(markerOptions);
                }
            });
        }
    }
}
