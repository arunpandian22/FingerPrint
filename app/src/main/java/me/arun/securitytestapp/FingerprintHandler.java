package me.arun.securitytestapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by arun on 29/11/17.
 * a class created for the  get the results from the fingerprintmanager when user try to use the fingerprint sensor
 */


@RequiresApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    FingerprintFragment fingerprintFragment;


    // Constructor
    @TargetApi(Build.VERSION_CODES.M)
    public FingerprintHandler(Context mContext,FingerprintFragment fingerprintFragment) {
        context = mContext;
        this.fingerprintFragment=fingerprintFragment;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.updateStatus("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.updateStatus("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.updateStatus("Fingerprint Authentication failed.", false);


    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.updateStatus("Fingerprint Authentication succeeded.", true);

       fingerprintFragment.ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_fingerprint_success));
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();

    }






    public void updateStatus(String e, Boolean success){
        fingerprintFragment.tvStatus.setText(e);
        if (!success) {
            fingerprintFragment.ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_fingerprint_error));
            fingerprintFragment.tvStatus.setTextColor(context.getColor(R.color.warning_color));
        }else{
            fingerprintFragment.tvStatus.setTextColor(context.getColor(R.color.success_color));
        }

    }


    /**
     * A method to set up the finger print initial setup
     * @param fingerprintManager A class that coordinates access to the fingerprint hardware.
     * @param keyguardManager Class that can be used to lock and unlock the keyboard.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void fingerPrintSetup(FingerprintManager fingerprintManager,KeyguardManager keyguardManager)
    {


        // Check whether the device has a Fingerprint sensor.
        if(!fingerprintManager.isHardwareDetected()){

//                tv_fp_mes.setText("Your Device does not have a Fingerprint Sensor");
            if (fingerprintFragment.isVisible())
                fingerprintFragment.dismiss();
            Toast.makeText(context, ""+"Your Device does not have a Fingerprint Sensor", Toast.LENGTH_SHORT).show();

        }else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                   tv_fp_mes.setText("Fingerprint authentication permission not enabled");
                if (fingerprintFragment.isVisible())
                    fingerprintFragment.tvStatus.setText("Fingerprint authentication permission not enabled");
                else
                    Toast.makeText(context, "Fingerprint authentication permission not enabled", Toast.LENGTH_SHORT).show();
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    if (fingerprintFragment.isVisible())
                        fingerprintFragment.tvStatus.setText("Register at least one fingerprint in Settings");
                    else
                        Toast.makeText(context, "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
//                            tv_fp_mes.setText("Lock screen security not enabled in Settings");
                        if (fingerprintFragment.isVisible())
                            fingerprintFragment.tvStatus.setText("Lock screen security not enabled in Settings");
                        else
                            Toast.makeText(context, ""+"Lock screen security not enabled in Settings", Toast.LENGTH_SHORT).show();
                    }else{
                        GenerateKeyCipher generateKeyCipher =new GenerateKeyCipher();
                        generateKeyCipher.generateKey();
                        if (generateKeyCipher.cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(generateKeyCipher.getcipher());
                          startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }
    }


}
