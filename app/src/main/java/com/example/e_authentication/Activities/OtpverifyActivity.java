package com.example.e_authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_authentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OtpverifyActivity extends AppCompatActivity {


    private String verifyid;
    ProgressBar progressBar;
    String phoneno;
    TextView textView;
    EditText editText;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);


        textView= findViewById(R.id.textView2);
        progressBar=findViewById(R.id.progressBar2);
        editText=findViewById(R.id.otp);

        phoneno=getIntent().getStringExtra("phonenumber");
         textView.setText("Otp will be sent to "+phoneno);

        sendVerificationCode(phoneno);

        findViewById(R.id.Signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=editText.getText().toString().trim();
                if (code.isEmpty()||code.length()<6){
                    editText.setError("enter code ..");
                    editText.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifycode(code);
            }
        });

    }
    private void verifycode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verifyid,code);
        signInWithcredential(credential);
    }

    private void signInWithcredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()) {
                          Intent intent=new Intent(OtpverifyActivity.this,Home.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                      }
                      else{
                          Toast.makeText(OtpverifyActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }
                    }
                });
    }


    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mcallBack
        );
    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifyid=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpverifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
