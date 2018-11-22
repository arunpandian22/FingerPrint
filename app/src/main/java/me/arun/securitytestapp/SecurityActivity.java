package me.arun.securitytestapp;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecurityActivity extends AppCompatActivity implements FingerprintHandler.FingerPrintAuthCallBack
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
        fingerprintHandler=new FingerprintHandler(this,fingerprintFragment,this);
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        fingerprintHandler.fingerPrintSetup(fingerprintManager,keyguardManager);

    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        updateStatus("Fingerprint Authentication error\n" + errString, false);

    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
      updateStatus("Fingerprint Authentication help\n" + helpString, false);

    }

    @Override
    public void onAuthenticationFailed() {
      updateStatus("Fingerprint Authentication failed.", false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.updateStatus("Fingerprint Authentication succeeded.", true);

        fingerprintFragment.ivIcon.setImageDrawable(getDrawable(R.drawable.ic_fingerprint_success));
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    public void updateStatus(String e, Boolean success) {
        fingerprintFragment.tvStatus.setText(e);
        if (!success) {
            fingerprintFragment.ivIcon.setImageDrawable(getDrawable(R.drawable.ic_fingerprint_error));
            fingerprintFragment.tvStatus.setTextColor(getResources().getColor(R.color.warning_color));
        }else{
            fingerprintFragment.tvStatus.setTextColor(getResources().getColor(R.color.success_color));
        }

    }
}
