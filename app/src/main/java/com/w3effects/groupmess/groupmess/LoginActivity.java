package com.w3effects.groupmess.groupmess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import ui.MyActivity;


public class LoginActivity extends Activity {

    TextView registerLabel;
    EditText mUsn;
    EditText mPassword;
    Button mLogin;
    Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();
        mUsn = (EditText)findViewById(R.id.usnTextFeildLogin);
        mPassword = (EditText)findViewById(R.id.passwordTextFeildLogin);

        mLogin = (Button)findViewById(R.id.loginButton);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mUsn.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty()){
                    mUsn.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
                    mPassword.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(findViewById(R.id.containerBox));
                }else {

                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    LoginActivity.this.progressBar = ProgressDialog.show(LoginActivity.this, "", getString(R.string.signing_in_msg), true);

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {

                                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                installation.put("userId", user.getObjectId());
                                if(user.get("type").equals("student")){
                                    installation.put("semester", user.get("semester"));
                                }
                                installation.saveInBackground();

                                Intent intent = new Intent(LoginActivity.this, MyActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                LoginActivity.this.progressBar.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage()).setTitle(getString(R.string.error_title)).setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        }

                    });

                }
            }
        });

        registerLabel = (TextView)findViewById(R.id.registerLabel);
        registerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerLabel = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerLabel);
            }
        });
    }
}
