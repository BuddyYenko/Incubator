package com.example.s215087038.incubator.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.s215087038.incubator.R;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnScan;

    //qr code scanner object
    private IntentIntegrator qrScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnScan = (Button) findViewById(R.id.btnScan);



        //initializing scan object
        qrScanner = new IntentIntegrator(this);
        btnScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScanner.addExtra("SCAN_MODE", "QR_CODE_MODE");
        qrScanner.setCameraId(1);
        qrScanner.setPrompt("Please align your QR code with the tablet’s camera");

        qrScanner.initiateScan();

    }
}
