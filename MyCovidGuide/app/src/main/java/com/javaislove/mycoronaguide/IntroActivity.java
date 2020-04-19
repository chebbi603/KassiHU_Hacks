package com.javaislove.mycoronaguide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;
    String filename = "file.mt";
    String line;
    String langfile = "language.mt";
    public static String language ;
    FileOutputStream outputStream;
    Button langsel ;
    Button sellangsel ;
    Button frbtn ;
    Button arbtn ;
    Button engbtn ;
    boolean b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(),GetStartedActivity.class );
            startActivity(mainActivity);
            finish();
        }
        setContentView(R.layout.activity_intro);
        try {
            FileInputStream in = openFileInput(langfile);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    language = line;
                } else {
                    language = "arabe";
                }
            } catch (IOException e) {
                language = "arabe";
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            language = "arabe";
            e.printStackTrace();
        }
        langsel = (Button) findViewById(R.id.selectlang);
        sellangsel = (Button) findViewById(R.id.selectlangsel);
        frbtn = (Button) findViewById(R.id.darkbtn);
        arbtn = (Button) findViewById(R.id.lightbtn);
        engbtn = (Button) findViewById(R.id.engbtn);
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_connect);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        final List<ScreenItem> mList = new ArrayList<>();
        if(language.equals("francais")){
            arbtn.setBackground(getDrawable(R.drawable.langbtn));
            engbtn.setBackground(getDrawable(R.drawable.langbtn));
            frbtn.setBackground(getDrawable(R.drawable.langbtnsel));
            sellangsel.setText("Changer la langue");
            langsel.setText("Changer la langue");
            btnNext.setText("Suivant");
            btnGetStarted.setText("Autoriser");
            mList.add(new ScreenItem("دليلي ضد الكورونا"," ","logoanimation/logo.json"));
            mList.add(new ScreenItem("Configuration rapide","Veuillez autoriser à cette application à accéder à votre localisation","health.json"));
            mList.add(new ScreenItem("Parfait!","Votre application est prete. Merci pour avoir utiliser nos services","completed.json"));
        }
        else if(language.equals("arabe")){
            arbtn.setBackground(getDrawable(R.drawable.langbtnsel));
            engbtn.setBackground(getDrawable(R.drawable.langbtn));
            frbtn.setBackground(getDrawable(R.drawable.langbtn));
            sellangsel.setText("بدل اللغة");
            langsel.setText("بدل اللغة");
            btnNext.setText("تابع");
            btnGetStarted.setText("موافق");
            mList.add(new ScreenItem("دليلي ضد الكورونا"," ","logoanimation/logo.json"));
            mList.add(new ScreenItem("خطوات بسيطة ناقصة","لازم تخلي التطبيقة تستعمل الموقع متاعك","health.json"));
            mList.add(new ScreenItem("كل شي كمل","خدماتنا لكل حاضرة. شكرا لإستعمال التطبيقة","completed.json"));
        }
        else{
            arbtn.setBackground(getDrawable(R.drawable.langbtn));
            engbtn.setBackground(getDrawable(R.drawable.langbtnsel));
            frbtn.setBackground(getDrawable(R.drawable.langbtn));
            sellangsel.setText("Change language");
            btnNext.setText("Next");
            btnGetStarted.setText("Allow");
            langsel.setText("Change language");
            mList.add(new ScreenItem("دليلي ضد الكورونا"," ","logoanimation/logo.json"));
            mList.add(new ScreenItem("Quick setup","Please allow this app to access to your location","health.json"));
            mList.add(new ScreenItem("Great!","Your app is ready now. Thanks for using this app","completed.json"));
        }
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);
        loadFirstScreen();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position == mList.size()-3) {
                    position++;
                    screenPager.setCurrentItem(position);
                    loadSecScreen();
                }
                else if (position == mList.size()-2) {
                    if (b) {
                        position++;
                        screenPager.setCurrentItem(position);
                        loaddLastScreen();
                    }else{

                    }

                }
                else{
                    if(btnNext.getText().equals("Finish")||btnNext.getText().equals("Finir")||btnNext.getText().equals("انهاء")){
                        savePrefsData();
                        Intent intent = new Intent(v.getContext(),HomeActivity.class);
                        startActivity(intent);

                    }

                }

            }
        });
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                }
                else if(tab.getPosition() == mList.size()-3){

                        loadFirstScreen();

                }
                else{
                    loadSecScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
            }
        });
        langsel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sellangsel.startAnimation(fadein);
                langsel.startAnimation(fadeout);
                sellangsel.setVisibility(View.VISIBLE);
                langsel.setVisibility(View.INVISIBLE);
                arbtn.startAnimation(fadein);
                frbtn.startAnimation(fadein);
                engbtn.startAnimation(fadein);
                frbtn.setVisibility(View.VISIBLE);
                arbtn.setVisibility(View.VISIBLE);
                engbtn.setVisibility(View.VISIBLE);
            }
        });
        sellangsel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sellangsel.startAnimation(fadeout);
                langsel.startAnimation(fadein);
                sellangsel.setVisibility(View.INVISIBLE);
                langsel.setVisibility(View.VISIBLE);
                arbtn.startAnimation(fadeout);
                frbtn.startAnimation(fadeout);
                engbtn.startAnimation(fadeout);
                frbtn.setVisibility(View.INVISIBLE);
                arbtn.setVisibility(View.INVISIBLE);
                engbtn.setVisibility(View.INVISIBLE);
            }
        });
        arbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arbtn.setBackground(getDrawable(R.drawable.langbtnsel));
                try {
                    language = "arabe";
                    outputStream = openFileOutput(langfile, Context.MODE_PRIVATE);
                    outputStream.write(language.getBytes());
                    outputStream.close();
                } catch (IOException e) {
                }
                finish();
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        engbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engbtn.setBackground(getDrawable(R.drawable.langbtnsel));
                try {
                    language = "english";
                    outputStream = openFileOutput(langfile, Context.MODE_PRIVATE);
                    outputStream.write(language.getBytes());
                    outputStream.close();
                } catch (IOException e) {
                }
                finish();
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }
    private void loaddLastScreen() {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        if(langsel.getVisibility()==View.VISIBLE){
            langsel.startAnimation(fadeout);
        }
        if(btnGetStarted.getVisibility()==View.VISIBLE){
            btnGetStarted.startAnimation(fadeout);
            btnGetStarted.setVisibility(View.INVISIBLE);
        }
        if(language.equals("francais")){
            btnNext.setText("Finir");
        }
        else if(language.equals("arabe")){
            btnNext.setText("انهاء");
        }
        else{
            btnNext.setText("Finish");
        }
        sellangsel.setVisibility(View.INVISIBLE);
        langsel.setVisibility(View.INVISIBLE);
        frbtn.setVisibility(View.INVISIBLE);
        arbtn.setVisibility(View.INVISIBLE);
        engbtn.setVisibility(View.INVISIBLE);
    }
    private void loadFirstScreen()  {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        //if(langsel.getVisibility()==View.INVISIBLE){
            langsel.startAnimation(longfadein);
       //}
        if(btnGetStarted.getVisibility()==View.VISIBLE){
            btnGetStarted.startAnimation(fadeout);
            btnGetStarted.setVisibility(View.INVISIBLE);
        }
        if(language.equals("francais")){
            btnNext.setText("Suivant");
        }
        else if(language.equals("arabe")){
            btnNext.setText("تابع");
        }
        else{
            btnNext.setText("Next");
        }
        sellangsel.setVisibility(View.INVISIBLE);
        langsel.setVisibility(View.VISIBLE);
        frbtn.setVisibility(View.INVISIBLE);
        arbtn.setVisibility(View.INVISIBLE);
        engbtn.setVisibility(View.INVISIBLE);
    }
    private void loadSecScreen() {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        if(langsel.getVisibility()==View.VISIBLE){
            langsel.startAnimation(fadeout);
        }
        if(language.equals("francais")){
            btnNext.setText("Suivant");
        }
        else if(language.equals("arabe")){
            btnNext.setText("تابع");
        }
        else{
            btnNext.setText("Next");
        }
        btnGetStarted.startAnimation(longfadein);
        btnGetStarted.setVisibility(View.VISIBLE);
        sellangsel.setVisibility(View.INVISIBLE);
        langsel.setVisibility(View.INVISIBLE);
        frbtn.setVisibility(View.INVISIBLE);
        arbtn.setVisibility(View.INVISIBLE);
        engbtn.setVisibility(View.INVISIBLE);
    }
    private final int REQUEST_LOCATION_PERMISSION = 1;
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
            b = false;
        }else b = true;
    }
}
