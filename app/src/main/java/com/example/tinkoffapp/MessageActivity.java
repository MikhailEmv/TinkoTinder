package com.example.tinkoffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ArrayList<Message> messages;
    private ImageView imgToolBar, imgSend;
    private MessagesAdapter messagesAdapter;

    String usernameOfTheRoommate, emailOfRoommate, chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        usernameOfTheRoommate = getIntent().getStringExtra("name_roommate");
        emailOfRoommate = getIntent().getStringExtra("email_roommate");

        recyclerView = findViewById(R.id.recyclerMessages);
        edtMessageInput = findViewById(R.id.edtText);
        txtChattingWith = findViewById(R.id.txtChattingWith);
        progressBar = findViewById(R.id.progressMessages);
        imgToolBar = findViewById(R.id.img_toolbar);
        imgSend = findViewById(R.id.imgSendMessage);
        txtChattingWith.setText(usernameOfTheRoommate);
        messages = new ArrayList<Message>();

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId)
                        .push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                emailOfRoommate,
                                edtMessageInput.getText().toString()));
                edtMessageInput.setText("");
            }
        });

        messagesAdapter = new MessagesAdapter(messages,
                getIntent().getStringExtra("my_img"),
                getIntent().getStringExtra("img_roommate"),
                MessageActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messagesAdapter);

        Glide.with(MessageActivity.this)
                .load(getIntent()
                        .getStringExtra("img_roommate"))
                .placeholder(R.drawable.account_img)
                .error(R.drawable.account_img)
                .into(imgToolBar);

        setUpChatRoom();
    }

    private void setUpChatRoom(){
        FirebaseDatabase
                .getInstance()
                .getReference("user/" + FirebaseAuth
                        .getInstance()
                        .getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUserName = snapshot.getValue(User.class).getUsername();
                if (usernameOfTheRoommate.compareTo(myUserName)>0){
                    chatRoomId = myUserName + usernameOfTheRoommate;
                } else if(usernameOfTheRoommate.compareTo(myUserName) == 0){
                    chatRoomId = myUserName + usernameOfTheRoommate;
                } else{
                    chatRoomId = usernameOfTheRoommate + myUserName;
                }
                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachMessageListener(String chatRoomId){
        FirebaseDatabase
                .getInstance()
                .getReference("messages/" + chatRoomId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messagesAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}