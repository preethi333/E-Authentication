package com.example.e_authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_authentication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    String userid=mAuth.getCurrentUser().getUid();
    DocumentReference documentReference=firebaseFirestore.collection("users").document(userid);
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        surfaceView=(SurfaceView)findViewById(R.id.camerapreview);
        textView=(TextView)findViewById(R.id.nav_emailid);

        barcodeDetector=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(380,300).setAutoFocusEnabled(true).build();

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String qrcode=documentSnapshot.getString("Qrcode");
                    code=qrcode;
                }
                else{
                    Toast.makeText(ScannerActivity.this, "Document not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScannerActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("tag",e.toString());
                    }
                });


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();

                if(qrCodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            String  value=qrCodes.valueAt(0).displayValue;
                            //textView.setText(value);
                            if(value.equals(code)){
                                Intent intent=new Intent(getApplicationContext(),Home.class);
                                startActivity(intent);
                            }

                        }
                    });
                }
            }
        });
    }
}
