package me.arun.securitytestapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecurityActivity extends AppCompatActivity
{

    public Button btProceed;
    SecurityApp securityApp;
    FingerprintFragment fingerprintFragment;
    KeyguardManager keyguardManager;
    FingerprintManager fingerprintManager;
    FingerprintHandler fingerprintHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btProceed=(Button)findViewById(R.id.btProceed);
        securityApp=(SecurityApp)getApplication();
        intialdialogSetup();
        fingerPrintManagerSetup();
        if (securityApp.getIsFingerPrintNeed())
        fingerprintFragment.show(getFragmentManager(),"FingerPrintDialog");

        btProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent=new Intent(SecurityActivity.this,HomeActivity.class);
                startActivity(homeIntent);
            }
        });
    }

    /**
     * A method to set the initial finger print dialog setup
     */
    public void intialdialogSetup()
    {
        fingerprintFragment
                = new FingerprintFragment();
        fingerprintFragment.setCancelable(false);
    }


    /**
     * A method initialize the object of KeyGuard manager and finger print manager
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void fingerPrintManagerSetup()
    {
        fingerprintHandler=new FingerprintHandler(this,fingerprintFragment);
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        fingerprintHandler.fingerPrintSetup(fingerprintManager,keyguardManager);

    }

}
