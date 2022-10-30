package com.example.tinkoffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnSubmit;
    private TextView txtLoginInfo;
    private ArrayList<String> tegs;
    private String tegsStr;



    private boolean isSigningUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tegs = new ArrayList<String>(Arrays.asList("Рыбалка", "Еда"));
        btnSubmit = findViewById(R.id.btnSubmit);
        tegsStr = "";
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this,
                    FriendsActivity.class));
            finish();
        }

        Button bth_tags = findViewById(R.id.bth_tags);
        bth_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner1 = findViewById(R.id.spinner1);
                String selected1 = spinner1.getSelectedItem().toString();


                Spinner spinner2 = findViewById(R.id.spinner2);
                String selected2 = spinner2.getSelectedItem().toString();


                Spinner spinner3 = findViewById(R.id.spinner3);
                String selected3 = spinner3.getSelectedItem().toString();


                Spinner spinner4 = findViewById(R.id.spinner4);
                String selected4 = spinner4.getSelectedItem().toString();


                Spinner spinner5 = findViewById(R.id.spinner5);
                String selected5 = spinner5.getSelectedItem().toString();



                tegs.add(selected1);
                tegs.add(selected2);
                tegs.add(selected3);
                tegs.add(selected4);
                tegs.add(selected5);
                tegsStr = new Gson().toJson(tegs);
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtEmail.getText().toString().isEmpty() ||
                        edtPassword.getText().toString().isEmpty()) {
                    if (isSigningUp && edtUsername.getText().toString().isEmpty()) {
                        Toast.makeText(
                                MainActivity.this,
                                "Invalid Input",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (isSigningUp) {
                    handleSignUp();
                }
                else {
                    handleLogin();
                }
            }
        });

        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSigningUp) {
                    isSigningUp = false;
                    edtUsername.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Don't have an account? Sign up");
                }
                else {
                    isSigningUp = true;
                    edtUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Sign up");
                    txtLoginInfo.setText("Already have an account? Log in");
                }
            }
        });
    }

    private void handleLogin() {
        FirebaseAuth.
                getInstance().
                signInWithEmailAndPassword(edtEmail.getText().toString(),
                        edtPassword.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this,
                                    FriendsActivity.class));
                            Toast.makeText(
                                    MainActivity.this,
                                    "Logged in successfully",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleSignUp() {
         FirebaseAuth.
                 getInstance().
                 createUserWithEmailAndPassword(edtEmail.getText().toString(),
                 edtPassword.getText().toString()).
                 addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     FirebaseDatabase.getInstance().getReference("user/" +
                             FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(
                             edtUsername.getText().toString(),
                             edtEmail.getText().toString(),
                             ""
                     ));
                     startActivity(new Intent(MainActivity.this,
                             FriendsActivity.class));
                     Toast.makeText(
                             MainActivity.this,
                             "Signed up successfully",
                             Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(
                             MainActivity.this,
                             task.getException().getLocalizedMessage(),
                             Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }
}