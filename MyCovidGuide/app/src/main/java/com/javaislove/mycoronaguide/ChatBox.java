package com.javaislove.mycoronaguide;

import android.graphics.Color;
import android.os.Bundle;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;




public class ChatBox extends AppCompatActivity {
    class Message{
        public boolean Sender = false;
        public String Text = "";
        public Message(String s , boolean b){
            Sender = b;
            Text = s;
        }
    }
    public static String receiverUserName = "";
    private static String currentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference Receiver;
    private int NumberOfMessages = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox);
        TextView textView = findViewById(R.id.textView2);
        textView.setText(receiverUserName);
        currentUser = ChatActivity.currentUser;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Messages").child(receiverUserName);
        Receiver = FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUserName).child("Messages").child(currentUser);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    NumberOfMessages = dataSnapshot.child("Number").getValue(Integer.class);
                }
                catch (Exception e){
                        mDatabase.child("Number").setValue(0);
                        Receiver.child("Number").setValue(0);
                        mDatabase.child("Read").setValue(0);
                        Receiver.child("Read").setValue(0);
                }
                try {
                    int i =  Integer.valueOf(dataSnapshot.child("Read").getValue().toString());
                    System.out.println(i);
                    for(; i<NumberOfMessages; i++){
                        String result = dataSnapshot.child(i + "").getValue(String.class);
                        addMessage(new Message( result.substring(1) , result.charAt(0) == '0'));
                    }
                    mDatabase.child("Read").setValue(NumberOfMessages);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
    public void sendMessage(View view){
        EditText editText = findViewById(R.id.edittext);
        String s = editText.getText().toString();
        if(s.isEmpty())return;
        editText.setText("");
        mDatabase.child("Number").setValue(NumberOfMessages+1);
        Receiver.child("Number").setValue(NumberOfMessages+1);
        mDatabase.child(NumberOfMessages + "").setValue(0 + s);
        Receiver.child(NumberOfMessages + "").setValue(1 + s);

    }
    public void addMessage(Message message){
        String text = message.Text;
        boolean sender = message.Sender;
        LinearLayout linearLayout = findViewById(R.id.chat);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(30, 10, 30, 10);
        TextView textView = new TextView(linearLayout.getContext());
        textView.setText(message.Text);
        if(sender){
            layoutParams.gravity = Gravity.END;
            textView.setBackground(getDrawable(R.drawable.chat_text_background_sender));
            textView.setTextColor(Color.BLACK);
            linearLayout.addView(textView,layoutParams);
        }
        else{
            layoutParams.gravity = Gravity.START;
            textView.setBackground(getDrawable(R.drawable.chat_text_background_receiver));
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView,layoutParams);
        }
        textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        textView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        textView.setPadding(10,10,10,10);
        textView.requestLayout();
    }
}
