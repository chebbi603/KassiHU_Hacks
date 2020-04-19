package com.javaislove.mycoronaguide;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class GetStartedActivity extends AppCompatActivity {
    String filename = "file.mt";
    String line;
    public static String value;
    private static int TIME_OUT = 3000;

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
        setContentView(R.layout.activity_get_started);
        TextView createdby = (TextView) findViewById(R.id.textView);
        if (value.equals("light"))
            createdby.setTextColor(Color.BLACK);
        else if (value.equals("dark"))
            createdby.setTextColor(Color.WHITE);
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        final Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.longfadein);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(GetStartedActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }

}
