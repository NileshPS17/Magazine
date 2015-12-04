package com.cloudfoyo.magazine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

/**
 *
 * MagazineAppCompatActivity is an extended version of AppCompatActivity
 * which facilitates user session management. It prevents the need to check
 * for a valid user session in every Activity.
 * Use as instructed :=
 *
 *   1) All activities that require a valid user login must extend MagazineAppCompatActivity
 *      instead of AppCompatActivity.
 *
 *   2) Do not forget to call the Parent's onCreate() from the child activity.
 *      i.e super.onCreate({Bundle: savedInstanceState});
 */
public class MagazineAppCompatActivity extends AppCompatActivity {


    public static final String FB_TOKEN = "com.cloudfoyo.magazine.FB_TOKEN";

    private AccessTokenTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {


                if ( currentAccessToken == null )
                    redirectToLogin();
                else
                    Toast.makeText(getApplicationContext(), "Welcome Back " , Toast.LENGTH_SHORT).show();
            }
        };


        //Check for login here ....
        /**
         *
         * if failed to retrieve a valid user session
         *              redirect to login activity
         *            + clear all cookies
         *            + start a new User Session
         *
         */
    }




    public boolean isLoggedInWithFacebook() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void redirectToLogin() {
        Toast.makeText(this, "Please login", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, Login.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            tracker.stopTracking();
        }catch(NullPointerException e) {
            // Handle Exception
        }
    }
}
