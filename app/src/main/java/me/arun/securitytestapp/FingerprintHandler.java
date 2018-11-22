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


/**
 * Created by arun on 29/11/17.
 * a class created for the  get the results from the fingerprintmanager when user try to use the fingerprint sensor
 */


@RequiresApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    FingerprintFragment fingerprintFragment;
    FingerPrintAuthCallBack fingerPrintAuthCallBack;


    // Constructor
    @TargetApi(Build.VERSION_CODES.M)
    public FingerprintHandler(Context mContext,FingerprintFragment fingerprintFragment,FingerPrintAuthCallBack fingerPrintAuthCallBack) {
        context = mContext;
        this.fingerprintFragment=fingerprintFragment;
        this.fingerPrintAuthCallBack=fingerPrintAuthCallBack;
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
        fingerPrintAuthCallBack.onAuthenticationError(errMsgId,errString);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        fingerPrintAuthCallBack.onAuthenticationHelp(helpMsgId,helpString);
    }


    @Override
    public void onAuthenticationFailed() {
        fingerPrintAuthCallBack.onAuthenticationFailed();


    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result)
    {
        fingerPrintAuthCallBack.onAuthenticationSucceeded(result);
       fingerprintFragment.ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_fingerprint_success));
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();

    }


    /**
     * A interface for the call back of finger print authentication
     */
    public interface FingerPrintAuthCallBack
    {

         void onAuthenticationError(int errMsgId, CharSequence errString);

         void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

         void onAuthenticationFailed();

         void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result);

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

            if (fingerprintFragment.isVisible())
                fingerprintFragment.dismiss();
            Toast.makeText(context, ""+"Your Device does not have a Fingerprint Sensor", Toast.LENGTH_SHORT).show();

        }else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
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
