package com.example.e_authentication.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_authentication.R;

public class LogoActivity extends AppCompatActivity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        start=findViewById(R.id.button3);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LogoActivity.this,"button clicked",Toast.LENGTH_LONG).show();
                Log.d("this is msg","button clicked");
                Intent i=new Intent(LogoActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });




    }
}
