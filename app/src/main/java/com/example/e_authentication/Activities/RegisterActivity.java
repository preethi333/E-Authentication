package com.example.e_authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_authentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ImageView userphoto;
    static int PReqCode=1;
    static int REQUESTCODE=1;
    Uri pickedimageuri;

    private EditText useremail,userpassword,userphoneno,username;
    private ProgressBar loadingProgress;
    private Button btn1;
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String userid;
    EditText editText;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textView=findViewById(R.id.textView5);
        useremail=(EditText)findViewById(R.id.Regmail);
        userpassword=(EditText)findViewById(R.id.Regpassword);
        userphoneno=(EditText)findViewById(R.id.Regphoneno);
        username=(EditText)findViewById(R.id.Regname);
        loadingProgress=findViewById(R.id.progressBar);
        btn1=findViewById(R.id.Regbtn);
        loadingProgress.setVisibility(View.INVISIBLE);
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(login);
                finish();
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email=useremail.getText().toString();
                final String password=userpassword.getText().toString();
                final String phoneno=userphoneno.getText().toString();
                final String name=username.getText().toString();

                if(email.isEmpty() || name.isEmpty()|| password.isEmpty() || phoneno.isEmpty()){


                    showmessage("Please verfiy all fields");
                    btn1.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else {
                    createuseaccount(email,name,password,phoneno);
                }
            }
        });
       // userphoto = findViewById(R.id.Reguserphoto);

//        userphoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 22) {
//                    checkandrequestforpermissions();
//                } else {
//                    openGallery();
//                }
//            }
//        });
    }

    private void createuseaccount(final String email, final String name, String password, final String phoneno) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showmessage("Account created");
                    userid=mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference=firebaseFirestore.collection("users").document(userid);
                    HashMap<String,Object> hm=new HashMap<>();
                    hm.put("name",name);
                    hm.put("email",email);
                    hm.put("phoneno",phoneno);
                    documentReference.set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG","on success");
                        }
                    });
                   // updateuserinfo(name,pickedimageuri,mAuth.getCurrentUser());
                    updateurl();
                }
                else {
                    showmessage("account creation failed"+task.getException().getMessage());
                    btn1.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.VISIBLE);
                }
            }
        });

    }

//    private void updateuserinfo(final String name, Uri pickedimageuri, final FirebaseUser currentUser) {
//        StorageReference mstorage=FirebaseStorage.getInstance().getReference().child("user_photo");
//        final StorageReference imageFilepath=mstorage.child(pickedimageuri.getLastPathSegment());
//        imageFilepath.putFile(pickedimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imageFilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                       UserProfileChangeRequest profileupdate=new UserProfileChangeRequest.Builder()
//                       .setDisplayName(name)
//                       .setPhotoUri(uri)
//                       .build();
//
//                       currentUser.updateProfile(profileupdate)
//                               .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                   @Override
//                                   public void onComplete(@NonNull Task<Void> task) {
//
//                                       if (task.isSuccessful()){
//                                           showmessage("register complete");
//                                           updateurl();
//                                       }
//                                   }
//                               });
//                    }
//                });
//            }
//        });
//    }


//    private void createuseaccount(String email, final String name, String password) {
//
//
//        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    showmessage("Account Created");
//                    updateuserinfo(name,pickedimageuri,mAuth.getCurrentUser());
//                }
//                else{
//                    showmessage("account creation failed");
//                    btn1.setVisibility(View.VISIBLE);
//                    loadingProgress.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//    }

    private void updateurl() {
        Intent i=new Intent(getApplicationContext(),VerifyActivity.class);
        startActivity(i);
        finish();
    }

//    private void updateuserinfo(final String name, Uri pickedimageuri, final FirebaseUser currentUser) {
//
//
//        StorageReference mstorage= FirebaseStorage.getInstance().getReference().child("user_photos");
//        final StorageReference imageFilepath=mstorage.child(pickedimageuri.getLastPathSegment());
//        imageFilepath.putFile(pickedimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                imageFilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()
//                                .setDisplayName(name)
//                                .setPhotoUri(uri)
//                                .build();
//
//                        currentUser.updateProfile(profileChangeRequest)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful()){
//                                            showmessage("Register Complete");
//                                            updateurl();
//                                        }
//                                    }
//                                });
//                    }
//                });
//            }
//        });
//    }

    private void showmessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
//    private void openGallery() {
//        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
//        i.setType("image/*");
//        startActivityForResult(i,REQUESTCODE);
//    }

//    private void checkandrequestforpermissions() {
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//                Toast.makeText(RegisterActivity.this,"Please accept for required permission", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
//            }
//        }
//        else{
//            openGallery();
//        }
//    }


  //  @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode==RESULT_OK && requestCode==REQUESTCODE && data!=null){
//
//            pickedimageuri=data.getData();
//            userphoto.setImageURI(pickedimageuri);
//        }
//    }
}
