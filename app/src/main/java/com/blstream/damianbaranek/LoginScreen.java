package com.blstream.damianbaranek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginScreen extends ActionBarActivity {
    protected Button loginButton;
    protected EditText emailEditText, passwordEditText;
    protected TextView errorEmailText, errorPasswordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        viewInit();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidEmail, isValidPassword;
                isValidEmail = isValidPassword = true;
                errorEmailText.setText("");
                errorPasswordText.setText("");
                if(!validateEmail(emailEditText.getText().toString())) {
                    errorEmailText.setText(R.string.invalidEmail);
                    isValidEmail = false;
                }
                if(!validatePassword(passwordEditText.getText().toString())) {
                    errorPasswordText.setText(R.string.invalidPassword);
                    isValidPassword = false;
                }
                if(!isValidEmail || !isValidPassword)
                    return;
                //zalogowano
                SharedPreferences loginInformation = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                SharedPreferences.Editor editLogIn = loginInformation.edit();
                editLogIn.putBoolean("isLogIn", true);
                editLogIn.apply();
                Intent loginIntent = new Intent(LoginScreen.this,MainScreen.class);
                startActivity(loginIntent);
                LoginScreen.this.finish();
            }
        });
    }
    protected void viewInit() {
        loginButton = (Button)findViewById(R.id.buttonLogin);
        emailEditText = (EditText)findViewById(R.id.editTextEmail);
        errorEmailText = (TextView)findViewById(R.id.textViewEmailError);
        passwordEditText = (EditText)findViewById(R.id.editTextPassword);
        errorPasswordText = (TextView)findViewById(R.id.textViewPasswordError);
}
    protected boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    protected boolean validatePassword(final String password) {
        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
