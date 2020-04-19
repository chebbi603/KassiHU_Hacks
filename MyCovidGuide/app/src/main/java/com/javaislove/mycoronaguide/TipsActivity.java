package com.javaislove.mycoronaguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

public class TipsActivity extends AppCompatActivity {
    String filename = "file.mt";
    String langfile = "language.mt";
    public String language = HomeActivity.language ;
    String line;
    public static String value;
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
        try {
            FileInputStream lan = openFileInput(langfile);
            InputStreamReader inputStreamReader4lang = new InputStreamReader(lan);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader4lang);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    language = line;
                } else {
                    language = "francais";
                }
            } catch (IOException e) {
                language = "francais";
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            language = "francais";
            e.printStackTrace();
        }
        if (value.equals("light"))
            setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        else if (value.equals("dark"))
            setTheme(android.R.style.Theme_Material_NoActionBar);
        setContentView(R.layout.activity_tips);
        ImageView entete = (ImageView) findViewById(R.id.entete);
        ImageView slideone = (ImageView) findViewById(R.id.slideone);
        ImageView slidetwo = (ImageView) findViewById(R.id.slidetwo);
        if(language.equals("francais")){
            slideone.setImageResource(R.drawable.slideonefr);
            slidetwo.setImageResource(R.drawable.slidetwofr);
        }
        else if(language.equals("arabe")){
            slideone.setImageResource(R.drawable.slideonear);
            slidetwo.setImageResource(R.drawable.slidetwoar);
        }
        else{
            slideone.setImageResource(R.drawable.slideoneeng);
            slidetwo.setImageResource(R.drawable.slidetwoeng);
        }
        final Button homebtn =(Button)findViewById(R.id.homebtn);
        Button changetheme =(Button)findViewById(R.id.themebtn);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        if (value.equals("light")) {
            entete.setImageResource(R.drawable.entete);
            changetheme.setBackground(getDrawable(R.drawable.darkmodebtn));
        }
        else if (value.equals("dark")) {
            entete.setImageResource(R.drawable.entetedk);
            changetheme.setBackground(getDrawable(R.drawable.lightmodebtn));
        }
        changetheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.equals("light")) {
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
                }else if(value.equals("dark")){
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

}
