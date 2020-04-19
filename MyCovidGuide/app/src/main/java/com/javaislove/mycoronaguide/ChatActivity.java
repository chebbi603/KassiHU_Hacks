package com.javaislove.mycoronaguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    ListView listView0;
    Adapter adapter;
    public static String currentUser;
    private DatabaseReference mDatabase;
    FirebaseAuth auth;

    HashMap<String , Object> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listView0 = findViewById(R.id.listview0);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getDisplayName();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                update();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(postListener);
        listView0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ChatActivity.this , ChatBox.class);
                    startActivity(intent);
                    ChatBox.receiverUserName = (String) ((TextView) view.findViewById(R.id.textview0)).getText();
            }
        });

    }
    void update(){
        hashMap.remove(currentUser);
        ArrayList<String> list = new ArrayList<>();
        for(String s : hashMap.keySet()){
            if(s.substring(0 , 2).equals("Dr")  ^ currentUser.substring(0 , 2).equals("Dr"))list.add(s);
        }
        adapter = new MyAdapter(this , list);
        listView0.setAdapter((ListAdapter) adapter);
    }
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rTitle;
        MyAdapter (Context c, ArrayList<String> title) {
            super(c, R.layout.row, R.id.textview0, title);
            this.context = c;
            this.rTitle = title;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle = row.findViewById(R.id.textview0);
            myTitle.setText(rTitle.get(position));
            return row;
        }
    }
}
