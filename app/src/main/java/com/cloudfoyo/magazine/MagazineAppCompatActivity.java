package com.cloudfoyo.magazine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedINstanceState)
    {
        super.onCreate(savedINstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

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
}
