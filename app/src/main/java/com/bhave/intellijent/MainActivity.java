package com.bhave.intellijent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        sp = getSharedPreferences("userInfo", Context.MODE_APPEND);
        spe = sp.edit();


        if(Profile.getCurrentProfile() == null){
            Toast.makeText(this,"Please login for further use",Toast.LENGTH_SHORT);
        }



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                Profile p = Profile.getCurrentProfile();
                spe.putString("email",p.getId());
                spe.commit();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt failed.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

    }

    public void listSongs(View view){
        Intent i = new Intent(this,SongList.class);
        startActivity(i);
    }

    public void songRecs(View view){
        Intent i = new Intent(this,Recommendations.class);
        startActivity(i);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
