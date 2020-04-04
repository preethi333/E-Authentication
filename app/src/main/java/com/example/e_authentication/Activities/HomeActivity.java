package com.example.e_authentication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e_authentication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    Button signout;
    private FirebaseAuth auth;
    Intent Homeactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //signout=(Button)findViewById(R.id.Signout);
        Homeactivity=new Intent(this,HomeActivity.class);


    }


    public void signout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent loginactivity=new Intent(this,LoginActivity.class);
        startActivity(loginactivity);
        finish();
    }

}
