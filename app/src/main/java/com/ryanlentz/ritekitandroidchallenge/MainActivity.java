package com.ryanlentz.ritekitandroidchallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends AppCompatActivity {

    /**
     * CallbackManager for handling the callbacks from the Facebook login
     */
    private CallbackManager mCallbackManager;

    /**
     * LoginButton for Facebook login
     */
    private LoginButton mLoginButton;

    /**
     * Button to launch the HashtagActivity
     */
    private Button mExploreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes Facebook's SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Creates the CallbackManager
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        // Finds a reference to the LoginButton in the layout
        mLoginButton = (LoginButton)findViewById(R.id.login_button);

        // Finds a reference to the Button in the layout
        mExploreButton = (Button) findViewById(R.id.explore_button);

        // Hides the explore button
        mExploreButton.setVisibility(View.INVISIBLE);

        // Registers the CallbackManager on the LoginButton
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(getApplicationContext(), HashtagActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, R.string.login_cancelled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });

        mExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HashtagActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if (loggedIn) {
            // Displays the explore button
            mExploreButton.setVisibility(View.VISIBLE);
        } else {
            // Hides the explore button
            mExploreButton.setVisibility(View.INVISIBLE);
        }
    }
}
