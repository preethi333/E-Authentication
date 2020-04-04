package com.example.e_authentication.Activities;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_authentication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class VerifyActivity extends AppCompatActivity {

    String TAG = "GenerateQRCode";
    String phoneno;
    ImageView Image;
    Button qrbtn, otpbtn,logout;
    String inputValue,msg;
    Bitmap bitmap;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();

    QRGEncoder qrgEncoder;
    TextView textView,textView1;
    String userid=mAuth.getCurrentUser().getUid();
    DocumentReference documentReference=firebaseFirestore.collection("users").document(userid);


    Uri uri = null;
    String[] TO_EMAILS=new String[1];
    Random random=new Random();
    final String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        textView1=findViewById(R.id.textView6);
        textView= findViewById(R.id.nav_emailid);
        Image =findViewById(R.id.imageView);
        qrbtn =findViewById(R.id.qrbtn);
        otpbtn=findViewById(R.id.otpbtn);

        //mAuth= FirebaseAuth.getInstance();
        //firebaseFirestore= FirebaseFirestore.getInstance();
        //userid=mAuth.getCurrentUser().getUid();
        //DocumentReference documentReference=firebaseFirestore.collection("users").document(userid);


        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginactivity=new Intent(VerifyActivity.this,LoginActivity.class);
                startActivity(loginactivity);
                finish();
            }
        });



        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                          String name=documentSnapshot.getString("name");
                          String email=documentSnapshot.getString("email");
                          String number=documentSnapshot.getString("phoneno");
                          phoneno=number;

                          textView1.setText("Welcome "+name+ " !!" );
                          textView.setText("Choose anyone of the below Option to proceed further");
                          TO_EMAILS[0]=email;
                        }
                        else{
                            Toast.makeText(VerifyActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerifyActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("tag",e.toString());
                    }
                });


        msg = String.valueOf(random.nextInt(10000));
        while (msg.length() != 4) {
            msg = String.valueOf(random.nextInt(10000));
        }
        HashMap<String,Object> hm=new HashMap<>();
        hm.put("Qrcode",msg);
        documentReference.set(hm, SetOptions.merge());

    }

    public  void start(View view){
        inputValue = msg;
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                //  qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        }
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
        }

//        Intent intent=new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.putExtra(Intent.EXTRA_EMAIL,TO_EMAILS);
//        intent.putExtra(Intent.EXTRA_SUBJECT,"this is the subject");
//        intent.putExtra(Intent.EXTRA_TEXT,"this is the body of the email");
//        intent.setType("image/*");


       // startActivityForResult(Intent.createChooser(intent, "choose one application"),0);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MailSender sender = new MailSender("preethireddy952@gmail.com", "9492752119");
                    //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/to_share.png","this is the multipart subject");
                    sender.addAttachment(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/to-share.png","this is the multipart");
                    sender.sendMail("This is Subject",
                            "This is Body",
                            "preethireddy952@gmail.com",
                            TO_EMAILS[0]);
                    Toast.makeText(VerifyActivity.this,"qr code sucessfully sent to ur mail",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }).start();

        Intent i =new Intent(VerifyActivity.this,ScannerActivity.class);
        startActivity(i);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            Intent intent1=new Intent(getApplicationContext(),ScannerActivity.class);
            startActivity(intent1);
            finish();
        }
    }



    public void otp(View view) {
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       String phonenumber=documentSnapshot.getString("phoneno");
                       String number="+91"+phonenumber;
                        Intent i=new Intent(VerifyActivity.this,OtpverifyActivity.class);
                        i.putExtra("phonenumber",number);
                        startActivity(i);
                    }
                });

    }
}
