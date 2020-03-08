package com.blueradix.android.mygooglesigninapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import static com.blueradix.android.mygooglesigninapp.Constants.TAG;

public class MyAppMainActivity extends AppCompatActivity {

    private GoogleSignInClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app_main);

        //configure Google Sign-In to request the user data required by your app
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        client = GoogleSignIn.getClient(this, gso);

        showUserInfo();

        Button signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_out_button:
                        signOut();//create this method
                        break;
                }
            }
        });

        Button signOutRevokeButton = findViewById(R.id.sign_out_revoke_button);
        signOutRevokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sign_out_revoke_button:
                        revokeAccess();
                        break;
                }
            }
        });

    }

    private void revokeAccess() {
        client.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "The user has disconnected its google account from your app");
                        goBackToLogIn();
                    }
                });
    }

    private void showUserInfo() {
        TextView info =  findViewById(R.id.textView);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        info.setText("Welcome " + account.getDisplayName() + " " + account.getEmail());
        ImageView profilePic = findViewById(R.id.imgProfilePic);
        Picasso.get().load(account.getPhotoUrl()).into(profilePic);
    }

    private void signOut() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // To do after sign out.
                    goBackToLogIn();
                }
            });

        }
    }

    private void goBackToLogIn(){
        Log.i(TAG, "going back to the login page");
        Intent goingBackToLogIn = new Intent();
        setResult(RESULT_OK, goingBackToLogIn);

        finish();
    }
}
