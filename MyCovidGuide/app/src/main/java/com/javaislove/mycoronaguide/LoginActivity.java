package com.javaislove.mycoronaguide;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    Button btnConnect;
    String filename = "file.mt";
    String line, value;
    String langfile = "language.mt";
    public static String language ;
    TextView title,desc,alreadyacc;
    FileOutputStream outputStream;
    LottieAnimationView lottie;
    Button doctorbtn ;
    EditText dr_login_email,nrml_login_pass,nrml_login_phone, nrml_phone, nrml_email, nrml_password, nrml_name, dr_email,dr_phone,dr_name,dr_password;
    ConstraintLayout dr_signin, nrml_signin;
    Button nrmlpersonbtn ;
    boolean isDoctor;
    EditText dr_login_password;
    FirebaseAuth auth;
    DatabaseReference reference;
    String name_2, phone_2,name_3, phone_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), GetStartedActivity.class);
            startActivity(mainActivity);
            finish();
        }*/
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
        setContentView(R.layout.activity_login);
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
        doctorbtn = (Button) findViewById(R.id.doctorbtn);
        nrmlpersonbtn = (Button) findViewById(R.id.normalpersonbtn);
        alreadyacc =(TextView)findViewById(R.id.already_acc);
        nrml_login_pass = (EditText) findViewById(R.id.normal_pass);
        nrml_phone = findViewById(R.id.nrml_phone);
        nrml_name = findViewById(R.id.nrml_name);
        nrml_email = findViewById(R.id.nrml_email);
        nrml_password =  findViewById(R.id.nrml_password);
        dr_email = findViewById(R.id.dr_email);
        dr_phone = findViewById(R.id.dr_phone);
        dr_name = findViewById(R.id.dr_name);
        dr_password = findViewById(R.id.dr_password);
        dr_signin =(ConstraintLayout) findViewById(R.id.dr_signup);
        nrml_signin = (ConstraintLayout) findViewById(R.id.nrml_signup);
        nrml_login_phone = (EditText) findViewById(R.id.normal_phone);
        dr_login_email =(EditText) findViewById(R.id.dr_login_email);
        dr_login_password = (EditText) findViewById(R.id.dr_login_pass);
        title = (TextView) findViewById(R.id.title);
        lottie = (LottieAnimationView) findViewById(R.id.intro_img) ;
        desc = (TextView)findViewById(R.id.desc);
        auth = FirebaseAuth.getInstance();
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        final Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        btnConnect = findViewById(R.id.btn_connect);
        if (language.equals("francais")) {
            doctorbtn.setText("Je suis un docteur");
            nrmlpersonbtn.setText("J'ai besoin d'aide");
        } else if (language.equals("arabe")) {
            doctorbtn.setText("أنا طبيب");
            nrmlpersonbtn.setText("حاجتي بمساعدة");
        } else {
            doctorbtn.setText("I'm a doctor");
            nrmlpersonbtn.setText("I need help");
        }
        loadFirstScreen();
        nrmlpersonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNormalPersonSignInScreen();
                isDoctor = false;
            }
        });
        doctorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDrSignUpScreen();
                isDoctor = true;
            }
        });
        alreadyacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLogInScreen();
            }
        });
    }
    private boolean restorePrefData() {
        SharedPreferences doctorpref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isLoginActivityOpnendBefore = doctorpref.getBoolean("isLoginOpened",false);
        return  isLoginActivityOpnendBefore;
    }
    private void savePrefsData() {
        SharedPreferences doctorpref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = doctorpref.edit();
        editor.putBoolean("isLoginOpened",true);
        editor.commit();
    }

    private void loadNormalPersonSignInScreen() {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        if(doctorbtn.getVisibility()==View.VISIBLE){
            nrmlpersonbtn.startAnimation(fadeout);
            doctorbtn.startAnimation(fadeout);
            title.startAnimation(fadeout);
            lottie.startAnimation(fadeout);
            desc.startAnimation(fadeout);
        }
        if(language.equals("francais")){
            title.setText("Une étape nécessaire");
            desc.setText("Veuillez remplir les champs vides");
            btnConnect.setText("S'inscrire");
            nrml_password.setHint("Mot de passe");
            nrml_name.setHint("Nom et prénom");
            nrml_phone.setHint("Numéro de téléphone");
        }
        else if(language.equals("arabe")){
            title.setText("لازم تقيد");
            desc.setText("الرجاء تعمير الفراغات");
            btnConnect.setText("تسجيل");
            nrml_password.setHint("كلمة السر");
            nrml_name.setHint("الإسم و اللقب");
            nrml_phone.setHint("رقم الهاتف");
        }
        else{
            title.setText("One step left");
            desc.setText("Please fill the blanks");
            btnConnect.setText("Sign up");
            nrml_password.setHint("Password");
            nrml_name.setHint("Name");
            nrml_phone.setHint("Phone number");
        }
        nrmlpersonbtn.startAnimation(fadeout);
        doctorbtn.startAnimation(fadeout);
        nrml_login_pass.setVisibility(View.INVISIBLE);
        nrml_login_phone.setVisibility(View.INVISIBLE);
        nrml_signin.startAnimation(longfadein);
        btnConnect.setVisibility(View.VISIBLE);
        alreadyacc.setVisibility(View.VISIBLE);
        doctorbtn.setVisibility(View.INVISIBLE);
        nrmlpersonbtn.setVisibility(View.INVISIBLE);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Name = nrml_name.getText().toString();
                String txt_Phone = nrml_phone.getText().toString();
                String txt_email = nrml_email.getText().toString();
                String txt_pass = nrml_password.getText().toString();
                if(TextUtils.isEmpty(txt_Name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                    System.out.println("all fields are required");
                }else{
                    nrml_register(txt_Name,txt_Phone, txt_email, txt_pass);
                }
            }
        });
    }


    private void loadFirstScreen()  {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        if(btnConnect.getVisibility()==View.VISIBLE){
            btnConnect.startAnimation(fadeout);
        }
        if(language.equals("francais")){
            title.setText("Besoin d'assistance");
            desc.setText("Si vous êtes un médecin, vous pouver participer et donner des conseils");
            btnConnect.setText("Se connecter");
        }
        else if(language.equals("arabe")){
            title.setText("استشارة طبية");
            desc.setText("كانك طبيب تنجم تشارك و تقدم نصائح للي عندو سؤال");
            btnConnect.setText("تسجيل");
        }
        else{
            title.setText("Get assistance");
            desc.setText("If you are a doctor, you can give advice to others");
            btnConnect.setText("Sign Up");
        }

        lottie.setAnimation("get_assistance.json");
        desc.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        doctorbtn.startAnimation(longfadein);
        nrmlpersonbtn.startAnimation(longfadein);
        doctorbtn.setVisibility(View.VISIBLE);
        dr_signin.setVisibility(View.INVISIBLE);
        nrml_signin.setVisibility(View.INVISIBLE);
        dr_login_email.setVisibility(View.INVISIBLE);
        dr_login_password.setVisibility(View.INVISIBLE);
        nrmlpersonbtn.setVisibility(View.VISIBLE);
        nrml_login_pass.setVisibility(View.INVISIBLE);
        nrml_login_phone.setVisibility(View.INVISIBLE);
        alreadyacc.setVisibility(View.INVISIBLE);
    }

    public void loadLoginDone(){
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        title.startAnimation(fadeout);
        lottie.startAnimation(fadeout);
        desc.startAnimation(fadeout);
        btnConnect.startAnimation(fadeout);
        setContentView(R.layout.activity_finished);
        title = (TextView) findViewById(R.id.title);
        lottie = (LottieAnimationView) findViewById(R.id.intro_img) ;
        desc = (TextView)findViewById(R.id.desc);
        btnConnect = (Button) findViewById(R.id.btn_connect);
        if(language.equals("francais")){
            title.setText("Tout est prêt");
            desc.setText("Vous pouvez maintenant utiliser nos services");
            btnConnect.setText("Finir");
        }
        else if(language.equals("arabe")){
            title.setText("كملنا");
            desc.setText("توا تنجم تتمتع بخدماتنا الكل");
            btnConnect.setText("انهاء");
        }
        else{
            title.setText("We're done");
            desc.setText("You can now use our services");
            btnConnect.setText("Finish");
        }
        lottie.startAnimation(longfadein);
        btnConnect.startAnimation(longfadein);
        btnConnect.setVisibility(View.VISIBLE);
        lottie.setAnimation("completed.json");
        lottie.playAnimation();
        desc.startAnimation(longfadein);
        title.startAnimation(longfadein);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ChatActivity.class));
            }
        });
    }

    private void loadDrSignUpScreen() {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);
        if(doctorbtn.getVisibility()==View.VISIBLE){
            nrmlpersonbtn.startAnimation(fadeout);
            doctorbtn.startAnimation(fadeout);
            title.startAnimation(fadeout);
            lottie.startAnimation(fadeout);
            desc.startAnimation(fadeout);
        }
        if(language.equals("francais")){
            title.setText("Une étape nécessaire");
            desc.setText("Veuillez remplir les champs vides");
            btnConnect.setText("S'inscrire");
            dr_phone.setHint("Numéro de téléphone");
            dr_email.setHint("Adresse mail");
            dr_name.setHint("Nom et prénom");
            dr_password.setHint("Mot de passe");
        }
        else if(language.equals("arabe")){
            title.setText("لازم تقيد");
            desc.setText("الرجاء تعمير الفراغات");
            btnConnect.setText("تسجيل");
            dr_phone.setHint("رقم الهاتف");
            dr_email.setHint("البريد الإلكتروني");
            dr_name.setHint("الإسم و اللقب");
            dr_password.setHint("كلمة السر");
        }
        else{
            title.setText("One step left");
            desc.setText("Please fill the blanks");
            btnConnect.setText("Sign up");
            dr_phone.setHint("Phone number");
            dr_email.setHint("E-mail Adress");
            dr_name.setHint("Name");
            dr_password.setHint("Password");
        }
        lottie.startAnimation(longfadein);
        doctorbtn.setVisibility(View.INVISIBLE);
        nrmlpersonbtn.setVisibility(View.INVISIBLE);
        dr_signin.startAnimation(longfadein);
        dr_signin.setVisibility(View.VISIBLE);
        btnConnect.startAnimation(longfadein);
        btnConnect.setVisibility(View.VISIBLE);
        lottie.setAnimation("doctors.json");
        lottie.playAnimation();
        desc.startAnimation(longfadein);
        title.startAnimation(longfadein);
        alreadyacc.startAnimation(longfadein);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Name = dr_name.getText().toString();
                String txt_Phone = dr_phone.getText().toString();
                String txt_email = dr_email.getText().toString();
                String txt_pass = dr_password.getText().toString();
                if(TextUtils.isEmpty(txt_Name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                    System.out.println("all fields are required");
                }else{
                    Dr_register(txt_Name,txt_Phone, txt_email, txt_pass);
                }
            }
        });
    }
    
    private void loadLogInScreen() {
        final Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        final Animation longfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.longfadein);

            title.startAnimation(fadeout);
            desc.startAnimation(fadeout);
            dr_signin.startAnimation(fadeout);
            dr_signin.setVisibility(View.INVISIBLE);
            nrml_signin.startAnimation(fadeout);
            alreadyacc.startAnimation(fadeout);

        if(language.equals("francais")){
            title.setText("Connexion");
            desc.setText("Veuillez remplir les champs vides");
            btnConnect.setText("Se connecter");
            dr_login_password.setHint("Mot de passe");
            dr_login_email.setHint("Adresse mail");
        }
        else if(language.equals("arabe")){
            title.setText("تسجيل دخول");
            desc.setText("الرجاء تعمير الفراغات");
            btnConnect.setText("تسجيل الدخول");
            dr_login_password.setHint("كلمة السر");
            dr_login_email.setHint("البريد الإلكتروني");
        }
        else{
            title.setText("Connecting");
            desc.setText("Please fill the blanks");
            btnConnect.setText("Log In");
            dr_login_password.setHint("Password");
            dr_login_email.setHint("E-mail adress");
        }
        if(isDoctor)lottie.setAnimation("doctors.json");
        else lottie.setAnimation("get_assistance.json");
        lottie.playAnimation();
        title.startAnimation(longfadein);
        desc.startAnimation(longfadein);
        dr_login_email.setVisibility(View.VISIBLE);
        dr_login_password.setVisibility(View.VISIBLE);
        dr_login_email.startAnimation(longfadein);
        dr_login_password.startAnimation(longfadein);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = dr_login_email.getText().toString();
                String txt_pass = dr_login_password.getText().toString();
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                    System.out.println("all fields are required");
                }else{
                    auth.signInWithEmailAndPassword(txt_email, txt_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        String UserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(UserName).child("Name");
                                        System.out.println(ref.toString());//this is The Name
                                        Toast.makeText(LoginActivity.this,"Logged in as " + ref.toString(), Toast.LENGTH_LONG).show();
                                        loadLoginDone();
                                        ValueEventListener postListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                System.out.println(dataSnapshot.getValue(String.class));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                System.out.println("onCancelled" + databaseError.toException().toString());
                                            }
                                        };
                                        ref.addValueEventListener(postListener);
                                    }else{
                                        System.out.println("failed to login check email and password"+ task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }




    private void Dr_register(String name, String phone, String email, String password) {
        name_2 = name;
        phone_2 = phone;
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String userID = user.getUid();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("Dr " + name_2)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                System.out.println("User profile updated.");
                                            }
                                        }
                                    });
                            reference = FirebaseDatabase.getInstance().getReference("Users").child("Dr "+ name_2);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userID);
                            hashMap.put("Name", name_2);
                            hashMap.put("PhoneNb", phone_2);
                            hashMap.put("imageURL", "default");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("success");

                                    }
                                }
                            });
                            loadLoginDone();
                        } else {
                            if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")) {
                                System.out.println("This E-mail address is already in use");
                            }
                        }
                    }
                });
    }
    private void nrml_register(String name, String phone, String email, String password) {
        name_3 = name;
        phone_3 = phone;
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;
                    String userID = user.getUid();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name_3)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("User profile updated.");
                                    }
                                }
                            });
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(name_3);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userID);
                    hashMap.put("Name", name_3);
                    hashMap.put("PhoneNb", phone_3);
                    hashMap.put("imageURL", "default");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println("success");
                            }
                        }
                    });
                    loadLoginDone();
                } else {
                    if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")) {
                        System.out.println("This E-mail address is already in use");
                    }
                }
            }
        });
    }

}
