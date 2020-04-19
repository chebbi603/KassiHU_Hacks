package com.javaislove.mycoronaguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    String filename = "file.mt";
    String langfile = "language.mt";
    public static String language;
    String line;
    public static String value;
    FileOutputStream outputStream;
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    static String NbCas;
    static String NbMorts;
    String nbmortsfile = "nbmort.mt";
    String nbcasfile = "nbcas.mt";
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

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
        if (value.equals("light"))
            setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        else if (value.equals("dark"))
            setTheme(android.R.style.Theme_Material_NoActionBar);
        setContentView(R.layout.activity_home);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        ImageView entete = (ImageView) findViewById(R.id.entete);
        Button changetheme = (Button) findViewById(R.id.themebtn);
        final Button aboutus = (Button) findViewById(R.id.aboutus);
        final ImageView popup = (ImageView) findViewById(R.id.popup);
        final TextView ourname = (TextView)findViewById(R.id.ourname);
        final TextView ourdesc = (TextView)findViewById(R.id.ourdesc);
        popup.setVisibility(View.INVISIBLE);
        ourname.setVisibility(View.INVISIBLE);
        ourdesc.setVisibility(View.INVISIBLE);
        final Animation in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        final Animation out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        if (value.equals("light")) {
            entete.setImageResource(R.drawable.entete);
            changetheme.setBackground(getResources().getDrawable(R.drawable.darkmodebtn));
            popup.setImageResource(R.drawable.popup);
        }
        else if (value.equals("dark")) {
            changetheme.setBackground(getResources().getDrawable(R.drawable.lightmodebtn));
            entete.setImageResource(R.drawable.entetedk);
            popup.setImageResource(R.drawable.popupdk);
        }
        models = new ArrayList<>();

        if(language.equals("francais")) {
            models.add(new Model(R.drawable.newspic, "Actualité", ("Plus d'information sur le virus")));
            models.add(new Model(R.drawable.statspic, "Statistiques", "L'état mondial en chiffres"));
            models.add(new Model(R.drawable.charitypic, "Charité", "Vous pouvez participer à l'amélioration de nos hopitaux"));
            models.add(new Model(R.drawable.pandemicpic, "Prévention", "On vous donne plusieur conseils pour rester protégés"));
            models.add(new Model(R.drawable.image_2, "Signaler", "Aider pour la lutte contre les rassemblements"));
            models.add(new Model(R.drawable.doctor_1, "Assistance médicale", "Parler avec un docteur pour plus d'aide"));
            aboutus.setText("Sur nous");
        } else if(language.equals("arabe")){
            models.add(new Model(R.drawable.newspic, "الأخبــــار", ("أكثر معلومات على الحالة الوبائية في تونس")));
            models.add(new Model(R.drawable.statspic, "الأحصائيات", "انتشار كورونا في العالم بالأرقام"));
            models.add(new Model(R.drawable.charitypic, "التـــــبرع", "اي نعم تنجم تعاون صبيطاراتنا"));
            models.add(new Model(R.drawable.pandemicpic, "الوقـــاية", "نصائحنا بش تقعدوا في صحة و عافية"));
            models.add(new Model(R.drawable.image_2, "الرصــــــد", "عاونا بش نحاولو انقصو من التجمعات"));
            models.add(new Model(R.drawable.doctor_1, "مساعدة طبية", "أحكي مع طبيب مختص بش يجاوبك على تساؤلاتك الكل"));
            aboutus.setText("أحنا شكون");
        }else{
            models.add(new Model(R.drawable.newspic, "News", ("More informations about this virus")));
            models.add(new Model(R.drawable.statspic, "Statistics", "Coronavirus in numbers"));
            models.add(new Model(R.drawable.charitypic, "Charity", "You can help our hospitals with your donations"));
            models.add(new Model(R.drawable.pandemicpic, "Prevention", "We are giving you some tips to stay safe"));
            models.add(new Model(R.drawable.image_2, "Report", "Help us to prevent major gatherings"));
            models.add(new Model(R.drawable.doctor_1, "Get assistance", "Talk with a doctor to get help"));
            aboutus.setText("About us");
        }
        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(50, 0, 50, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color5),
                getResources().getColor(R.color.color6)
        };
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
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popup.getVisibility()==View.INVISIBLE){
                    if(language.equals("francais")) {
                        aboutus.setText("Retour");
                        ourdesc.setText("Nous sommes un groupe de jeunes qui ont pour but aider notre pays. On a crée cette application pour vous donner chaque détail sur ce virus. Veuillez noter que chaque installation de notre application sera un don pour 1818. Merci pour avoir installé notre application <3");
                    } else if(language.equals("arabe")){
                        aboutus.setText("العودة");
                        ourdesc.setText("أحنا مجموعة من الشباب نسعاو بش نساعدو بلادنا بش نخرجو من الأزمة هذي . حبينا نقدمولكم كل جزئية تتعلق بالفيروس هذا بش ننشرو الوعي عند الناس الكل . كل شخص يستعمل التطبيقة هذي بش يساهم في التبرع لصندوق 1818. نشكروكم على ثيقتكم فينا");
                    }else{
                        aboutus.setText("Return");
                        ourdesc.setText("We are a group of teenagers aiming to help our country to overcome this pandemic. We created this app in order to spread awareness by giving you every detail about this virus. Note that the outcome of this app will be given to 1818. Thank you for installing our app <3");
                    }
                    popup.startAnimation(in);
                    ourdesc.startAnimation(in);
                    ourname.startAnimation(in);
                    popup.setVisibility(View.VISIBLE);
                    ourname.setVisibility(View.VISIBLE);
                    ourdesc.setVisibility(View.VISIBLE);

                }else {
                    popup.startAnimation(out);
                    ourname.startAnimation(out);
                    ourname.startAnimation(out);
                    popup.setVisibility(View.INVISIBLE);
                    ourname.setVisibility(View.INVISIBLE);
                    ourdesc.setVisibility(View.INVISIBLE);
                    if(language.equals("francais")) {
                        aboutus.setText("Sur nous");
                    } else if(language.equals("arabe")){
                        aboutus.setText("أحنا شكون");
                    }else{
                        aboutus.setText("About us");
                    }
                }
            }
        });

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
