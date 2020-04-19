package com.javaislove.mycoronaguide;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;

import android.content.Intent;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String filename = "file.mt";
    String line;
    public static String value;
    public String language = HomeActivity.language ;
    FileOutputStream outputStream;


    @RequiresApi(api = Build.VERSION_CODES.M)
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
        setContentView(R.layout.activity_main);
        ImageView entete = (ImageView) findViewById(R.id.entete);
        final ImageView popup = (ImageView) findViewById(R.id.popup);
        Button donate = (Button) findViewById(R.id.donate);
        Button homebtn = (Button) findViewById(R.id.homebtn);
        Button changetheme = (Button) findViewById(R.id.themebtn);
        final EditText getAmount = (EditText) findViewById(R.id.getAmount);
        final TextView title = (TextView) findViewById(R.id.title);
        final TextView subtitle = (TextView) findViewById(R.id.subtitle);
        final TextView note1 = (TextView) findViewById(R.id.note1);
        final TextView note2 = (TextView) findViewById(R.id.note2);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        final Animation in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        final Animation out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        mapFragment.getMapAsync(this);
        popup.setVisibility(View.INVISIBLE);
        note1.setVisibility(View.INVISIBLE);
        note2.setVisibility(View.INVISIBLE);
        getAmount.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        subtitle.setVisibility(View.INVISIBLE);
        if (value.equals("light")) {
            entete.setImageResource(R.drawable.entete);
            changetheme.setBackground(getResources().getDrawable(R.drawable.darkmodebtn));
            popup.setImageResource(R.drawable.popup);
        }
        else if (value.equals("dark")) {
            entete.setImageResource(R.drawable.entetedk);
            changetheme.setBackground(getResources().getDrawable(R.drawable.lightmodebtn));
            popup.setImageResource(R.drawable.popupdk);
        }
        if(language.equals("arabe")){
            donate.setText("التــــبرع");
        }
        else if(language.equals("francais")){
            donate.setText("Don");
        }
        else{
            donate.setText("Donate");
        }
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popup.getVisibility()==View.INVISIBLE){
                    if(language.equals("arabe")){
                        title.setText("تأكيد التبرع");
                        subtitle.setText("اختار بقداه تحب تتبرع لصندوق 1818");
                        getAmount.setHint("المبلغ بالمليم");
                        note1.setText("متنجمش حاليا تتبرع مباشرة للصبيطارات");
                        note2.setText("التبرع بش يتم من خلال USSD يعني من الهاتف");
                    }
                    else if(language.equals("francais")){
                        title.setText("Confirmer votre don");
                        subtitle.setText("Choisissez le montant du don à 1818");
                        getAmount.setHint("Don(en millim)");
                        note1.setText("Le don vers les hopitaux n'est pas disponible pour le moment");
                        note2.setText("Le don sera effectué à l'aide du service USSD");
                    }
                    else{
                        title.setText("Confirm your donation");
                        subtitle.setText("Choose the amount you want to give to 1818");
                        getAmount.setHint("Donation(millim)");
                        note1.setText("Donation to hospitals is currently unavailable");
                        note2.setText("the donation will use the USSD technology");
                    }
                    popup.startAnimation(in);
                    note1.startAnimation(in);
                    note2.startAnimation(in);
                    getAmount.startAnimation(in);
                    title.startAnimation(in);
                    subtitle.startAnimation(in);
                    popup.setVisibility(View.VISIBLE);
                    note1.setVisibility(View.VISIBLE);
                    note2.setVisibility(View.VISIBLE);
                    getAmount.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    subtitle.setVisibility(View.VISIBLE);
                }else {
                    if(TextUtils.isEmpty(getAmount.getText())){
                        if(language.equals("arabe"))Toast.makeText(v.getContext(), "الرجاء تعمير الفراغات الناقصة", Toast.LENGTH_LONG).show();
                        else if(language.equals("francais"))Toast.makeText(v.getContext(), "Veuillez remplir les champs vides", Toast.LENGTH_LONG).show();
                        else Toast.makeText(v.getContext(), "Please fill all the blanks", Toast.LENGTH_LONG).show();
                    }else {
                        String ussd = "*1818*" + getAmount.getText() + Uri.encode("#");
                        popup.startAnimation(out);
                        note1.startAnimation(out);
                        note2.startAnimation(out);
                        getAmount.startAnimation(out);
                        title.startAnimation(out);
                        subtitle.startAnimation(out);
                        popup.setVisibility(View.INVISIBLE);
                        note1.setVisibility(View.INVISIBLE);
                        note2.setVisibility(View.INVISIBLE);
                        getAmount.setVisibility(View.INVISIBLE);
                        title.setVisibility(View.INVISIBLE);
                        subtitle.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
                    }
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
    public void onMapReady (GoogleMap googleMap) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            final Context context = this;
            if (value.equals("light"))
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.raw));
            else if (value.equals("dark"))
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.rawdark));
            markthose();
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
        }
    }
    public void markthose(){
        LatLng hammamet = new LatLng(36.399748, 10.615390);
        LatLng nabeul = new LatLng(36.453807, 10.731919);
        LatLng tahermamouri = new LatLng(36.438262, 10.674122);
        LatLng darchabene = new LatLng(36.468054, 10.747244);
        LatLng bnikhiar = new LatLng(36.470562, 10.781354);
        LatLng mamoura = new LatLng(36.466458, 10.808050);
        LatLng tazarka = new LatLng(36.540915, 10.837097);
        LatLng korba1 = new LatLng(36.570284, 10.853661);
        LatLng korba2 = new LatLng(36.582070, 10.857567);
        LatLng menzeltmim = new LatLng(36.779101, 10.997792);
        LatLng kelibia = new LatLng(36.854711, 11.091573);
        LatLng hawaria = new LatLng(37.055882, 11.016761);
        LatLng abene = new LatLng(36.986539, 10.978949);
        LatLng bnikhalled = new LatLng(36.648869, 10.588594);
        LatLng menzelbouzalfa = new LatLng(36.692095, 10.588664);
        LatLng grombalia = new LatLng(36.595542, 10.494828);
        LatLng slimane = new LatLng(36.710271, 10.481601);
        Marker hammametcity = mMap.addMarker(new MarkerOptions()
                .position(hammamet)
                .anchor(0.5f, 0.5f)
                .title("Hopital Hammamet")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("5230 DT"));
        Marker nabeulcity = mMap.addMarker(new MarkerOptions()
                .position(nabeul)
                .anchor(0.5f, 0.5f)
                .title("Hopital Nabeul")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("3200 DT"));
        Marker darchabencity = mMap.addMarker(new MarkerOptions()
                .position(darchabene)
                .anchor(0.5f, 0.5f)
                .title("Hopital Dar Chabene")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("2600 DT"));
        Marker bnikhalledcity = mMap.addMarker(new MarkerOptions()
                .position(bnikhalled)
                .anchor(0.5f, 0.5f)
                .title("Hopital Bni Khalled")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("600 DT"));
        Marker bnikhiarcity = mMap.addMarker(new MarkerOptions()
                .position(bnikhiar)
                .anchor(0.5f, 0.5f)
                .title("Hopital Bni Khiar")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("962 DT"));
        Marker slimanesity = mMap.addMarker(new MarkerOptions()
                .position(slimane)
                .anchor(0.5f, 0.5f)
                .title("Hopital Slimane")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("320 DT"));
        Marker grombaliacity = mMap.addMarker(new MarkerOptions()
                .position(grombalia)
                .anchor(0.5f, 0.5f)
                .title("Hopital Grombalia")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("468 DT"));
        Marker menzelbousalfacity = mMap.addMarker(new MarkerOptions()
                .position(menzelbouzalfa)
                .anchor(0.5f, 0.5f)
                .title("Hopital Menzel Bouzalfa")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("367 DT"));
        Marker menzeltmimcity = mMap.addMarker(new MarkerOptions()
                .position(menzeltmim)
                .anchor(0.5f, 0.5f)
                .title("Hopital Menzel Tmim")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("854 DT"));
        Marker abenecity = mMap.addMarker(new MarkerOptions()
                .position(abene)
                .anchor(0.5f, 0.5f)
                .title("Hopital Abène")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("259 DT"));
        Marker tazarkacity = mMap.addMarker(new MarkerOptions()
                .position(tazarka)
                .anchor(0.5f, 0.5f)
                .title("Hopital Tazarka")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("394 DT"));
        Marker mamouracity = mMap.addMarker(new MarkerOptions()
                .position(mamoura)
                .anchor(0.5f, 0.5f)
                .title("Hopital Mamoura")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("760 DT"));
        Marker korba1city = mMap.addMarker(new MarkerOptions()
                .position(korba1)
                .anchor(0.5f, 0.5f)
                .title("Hopital Korba 1")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("125 DT"));
        Marker korba2city = mMap.addMarker(new MarkerOptions()
                .position(korba2)
                .anchor(0.5f, 0.5f)
                .title("Hopital Korba 2")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("367 DT"));
        Marker tahermamourimarker = mMap.addMarker(new MarkerOptions()
                .position(tahermamouri)
                .anchor(0.5f, 0.5f)
                .title("Hopital Universitaire Taher Mamouri")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("4520 DT"));
        Marker kelibiacity = mMap.addMarker(new MarkerOptions()
                .position(kelibia)
                .anchor(0.5f, 0.5f)
                .title("Hopital Grombalia")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("3200 DT"));
        Marker hawariacity = mMap.addMarker(new MarkerOptions()
                .position(hawaria)
                .anchor(0.5f, 0.5f)
                .title("Hopital Grombalia")
                .icon((BitmapDescriptorFactory.fromResource(R.drawable.cash2)))
                .snippet("162 DT"));
    }


}
