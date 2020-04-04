package com.example.e_authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_authentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {


    private EditText useremail,userpassword;
    private Button btn1;
    private ProgressBar loginprogress;
    private FirebaseAuth auth;
    private Intent Homeactivity;
    private ImageView loginphoto;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        useremail=(EditText)findViewById(R.id.loginmail);
        userpassword=(EditText)findViewById(R.id.loginpassword);
        btn1=(Button)findViewById(R.id.button2);
        loginprogress=findViewById(R.id.loginprogress);
        auth=FirebaseAuth.getInstance();
        Homeactivity=new Intent(this,VerifyActivity.class);
        textView=findViewById(R.id.textView9);

        loginprogress.setVisibility(View.INVISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginprogress.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.INVISIBLE);

                final String email=useremail.getText().toString();
                final String password=userpassword.getText().toString();

                if(email.isEmpty() || password.isEmpty() ){
                    showMessage("Verify all fields");
                    btn1.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);
                }
                else{
                    signIn(email,password);
                }
            }
        });



    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginprogress.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else{
                    showMessage(task.getException().getMessage());
                    btn1.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();

        if(user!=null){
            updateUI();
        }
    }

    private void updateUI() {

        startActivity(Homeactivity);
        finish();
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
    }
}
