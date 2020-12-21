package com.example.speedpayscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static  final int REQUEST_CAMERA=1;
    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkPermission()){
                Toast.makeText(Scanner.this,"Permission is granted!",Toast.LENGTH_LONG).show();
            }
            else {
                requestPermission();
            }
        }
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(Scanner.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        //Try with backward code and add this code in another!
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode,String permission[],int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA :
                if (grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(Scanner.this,"Permission Granted",Toast.LENGTH_LONG).show();
                    }

                    //Try with backward code after Permission Denied!
                    else {
                        Toast.makeText(Scanner.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                //put by myself. Try with no Put...
                                                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {

                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);

                                                }

                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkPermission()){
                if (scannerView==null){
                    scannerView=new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    //Try with backward code in Cancel Action!
    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(Scanner.this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult=result.getText();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");



        //****Own created****
        String a = String.valueOf(scanResult.charAt(0));
        int first = Integer.parseInt(a);

        //Amount
        String b = String.valueOf(scanResult.subSequence(1,first+1));
        final String second = String.valueOf(Integer.parseInt(b));

        //User_ID
        String third = String.valueOf(scanResult.subSequence(first+1,scanResult.length()));

        //****End****



        //Button to Pay Cash
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Scanner.this, MainActivity.class);
                intent.putExtra("Amount",second);
                startActivity(intent);
                finish();

                //go to scan qr state
                //scannerView.resumeCameraPreview(Scanner.this);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //go to scan qr state
                scannerView.resumeCameraPreview(Scanner.this);

            }
        });

        /*
        //This code is for going to the website!

        builder.setNeutralButton("visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        */

        builder.setMessage("User Id :"+third+"\nAmount :"+second);
        AlertDialog alert=builder.create();
        alert.show();

    }
}