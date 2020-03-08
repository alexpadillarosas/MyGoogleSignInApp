package com.blueradix.android.mygooglesigninapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.blueradix.android.mygooglesigninapp.Constants.RC_MAIN_APP;
import static com.blueradix.android.mygooglesigninapp.Constants.RC_SIGN_IN;
import static com.blueradix.android.mygooglesigninapp.Constants.TAG;

public class MainActivity extends AppCompatActivity {

    /*
    * Important when creating an app the app note the package name and the app name
    *
    * All steps are in the following link: https://developers.google.com/identity/sign-in/android/start-integrating
    *
    * First Step we need to be sure we have added Google play services
    *       ( SDK Manager -> Android SDK -> SDK Tools -> Google Play Services )
    *
    * Second Step:
    *       Then, in your app-level build.gradle file, declare Google Play services as a dependency:
    *   dependencies {
            implementation 'com.google.android.gms:play-services-auth:17.0.0'
        }
    *
    * Third Step:
    *   Get a certificate for our app (SHA-1)
    *   Click on the Gradle tab ( usually on the right panel )
    *   Expand the app folder -> Tasks -> android -> signingReport  (double click, and you'll see
    *   on the run terminal tab the SHA-1 certificate fingerprint ie:
    *       SHA1: C3:9C:F3:C5:9C:05:66:9E:F7:E4:E3:26:B6:0D:FB:5C:84:E8:49:D6
    *
    * Fourth Step:
    *   Configure your project to work with OAuth 2.0 client ID
    *   Click in Configure a Project Button and follow the steps.
    *   For example I configured MySignInApp
    *   Select Android as OAuth client.
    *   place your package name and the SHA1 key
    *   Then click in API console
    *
    *   Now you can start creating your first android with google sign in app.
    *
    *   Happy coding :)
    * */

    private  GoogleSignInClient client;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //configure Google Sign-In to request the user data required by your app
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        client = GoogleSignIn.getClient(this, gso);

        //In your activity's onStart method, check if a user has already signed in to your app with Google.
        // override the method onStart


        //go to the activity and add the  SignInButton to your app

        //set the size of your SignInButton
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        //Start the sign-in flow
        //register your button's OnClickListener to sign in the user when clicked:
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:

                        signIn();//create this method

                        break;
                }
            }
        });

        //This is the end of part One
        //Now lets code the sign out button
        //create a new activity that is your app main activity (MyAppMainActivity)



    }



    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // update the UI depending on the result of account
        updateUI(account);

    }

    private void updateUI(GoogleSignInAccount account) {


        //If GoogleSignIn.getLastSignedInAccount returns a GoogleSignInAccount object (rather than null),
        // the user has already signed in to your app with Google. Update your UI accordingly—that is,
        // hide the sign-in button, launch your main activity, or whatever is appropriate for your app.
        if(account != null) {
            Log.i(TAG, account.getEmail());
            Log.i(TAG, account.getDisplayName());
            Log.i(TAG, account.getId());
            Log.i(TAG, account.isExpired()? "true":"false");

            //This is for the LOG OUT
            showMyAppActivity();

        }else {
            //If GoogleSignIn.getLastSignedInAccount returns null, the user has not yet signed in to
            // your app with Google. Update your UI to display the Google Sign-in button.


        }
    }

    private void showMyAppActivity() {
        Intent goToMyAppIntent = new Intent(this, MyAppMainActivity.class);


        startActivity(goToMyAppIntent);
    }

    private void signIn() {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //now go to code the onActivityResult
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){

            // The Task returned from this call is always completed, no need to attach a listener.
            //The GoogleSignInAccount object contains information about the signed-in user, such as the user's name.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
