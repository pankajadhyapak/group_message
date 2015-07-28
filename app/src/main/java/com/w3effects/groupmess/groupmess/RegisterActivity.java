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
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ui.MyActivity;


public class RegisterActivity extends Activity {

    EditText mUsn;
    EditText mUsername;
    EditText mPassword;
    EditText mEmail;
    Button mRegisterBtn;
    TextView mCancelBtn;
    Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().hide();
        
        mUsn = (EditText)findViewById(R.id.usnTextFeild);
        mUsername = (EditText)findViewById(R.id.firstnameflied);
        mEmail = (EditText)findViewById(R.id.emailTextField);
        mPassword = (EditText)findViewById(R.id.passwordTextField);
        mRegisterBtn = (Button)findViewById(R.id.Registerbutton);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mUsn.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String first_name = mUsername.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty() || email.isEmpty() || first_name.isEmpty()){
                    mUsn.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
                    mPassword.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
                    mEmail.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
                    mUsername.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(findViewById(R.id.containerBox));
                }else {
                    RegisterActivity.this.progressBar = ProgressDialog.show(RegisterActivity.this, "", getString(R.string.registering_in_msg), true);

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.put("first_name", first_name);

                if(isAlphaNumeric(username)){
                    user.put("type", "student");
                    user.put("semester",1);
                }else{
                    user.put("type", "teacher");
                }


                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser currentUser = ParseUser.getCurrentUser();

                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userId", currentUser.getObjectId());
                            if(currentUser.get("type").equals("student")){
                                installation.put("semester", 1);
                            }

                            installation.saveInBackground();

                            Intent success = new Intent(RegisterActivity.this, MyActivity.class);
                            success.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            success.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(success);
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong

                            RegisterActivity.this.progressBar.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(e.getMessage()).setTitle(getString(R.string.error_title)).setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
            }
        });

mCancelBtn = (TextView)findViewById(R.id.cacelLabel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(back);
            }
        });

    }


    /*
        Check whether the username is alphanumeric or not
     */
    public boolean isAlphaNumeric(String s){
        Pattern pattern = Pattern.compile("((?:[0-9][a-z]+[a-z0-9]*))");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return true;
        }else{
            return false;
        }
    }

}
