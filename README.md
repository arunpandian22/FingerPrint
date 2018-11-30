# FingerPrint
A Repo explains about the Finger print Implementation in android the app
# Project Title
A small project to explain the Fingerprint Authentication and overview

Fingerprint authentication is now mostly used and reliable on recently coming devices.after the marshmallow came, Google introduced the fingerprint authentication in Android. The main Advantage of this is it is a unique, fast, user-friendly, don’t have to remember the password and no one can access your device without you.

### Prerequisites
To use fingerprint device os should be above or equal to API 23

### Implementation
In manifest add the following permissions

```
<uses-permission
android:name=”android.permission.USE_FINGERPRINT”
android:requiredFeature=”false” />

```

When you declare android:required=”true” for a feature, you are specifying that the application cannot function, or is not designed to the devices, which don’t have the fingerprint sensor. Because some device doesn’t have fingerprint sensor you have to set it to false. If you didn’t mention the android:required=false it will take as true

Just copy and paste the file of [GenerateKeyCipher.java](https://github.com/arunpandian22/FingerPrint/blob/master/app/src/main/java/me/arun/androidexploredutil/FingerPrint/GenerateKeyCipher.java)

This class contains the

* A class to generate the key for Keystore and initialize the Cipher object
* KeyStore class represents a storage facility for cryptographic keys and certificates.
* Cipher class provides the functionality of a cryptographic Cipher for encryption and decryption. It forms the core of the Java Cryptographic Extension (JCE) framework.

just copy and paste the class of the [FingerprintHandler.java](https://github.com/arunpandian22/FingerPrint/blob/master/app/src/main/java/me/arun/securitytestapp/FingerprintHandler.java)


This class contains all fingerprint set up and callbacks for the fingerprint Authentication and all the things in inline if you want to line by line explanation and method explanation. because I don't want to repeat the same thing here

let see about the implementation of fingerprint authentication inactivity

Just implement the FingerprintHandler.FingerPrintAuthCallBack like this

```
public class SecurityActivity extends AppCompatActivity implements FingerprintHandler.FingerPrintAuthCallBack{


  @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
      //todo for handling error

    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
    //Called when a recoverable error has been encountered during authentication
//  to do handling this type of error

    }

    @Override
    public void onAuthenticationFailed() {
   // todo for handling Fingerprint Authentication failed.
     

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

     //todo for handling Fingerprint Authentication succeeded
      
    }
}

```

Then initialize the following class in Activity class and run the program you will get the result in callbacks when the user tries to authenticate. In the following example, I gave an instance of [FingerprintFragment class](https://github.com/arunpandian22/FingerPrint/blob/master/app/src/main/java/me/arun/androidexploredutil/FingerPrint/FingerprintFragment.java) which I created for the initial information and which also used for showing error also in fragment dialog. if you create any new popup fragment you can set it instead of that to handle errors and showing information.

```

  KeyguardManager keyguardManager;
  FingerprintManager fingerprintManager;
  FingerprintHandler fingerprintHandler;
  FingerprintFragment fingerprintFragment
                = new FingerprintFragment();
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

```

You can refer the [Activity here](https://github.com/arunpandian22/FingerPrint/blob/master/app/src/main/java/me/arun/androidexploredutil/FingerPrint/SecurityActivity.java)

Any doubt then call me for help or ask me a doubt.
