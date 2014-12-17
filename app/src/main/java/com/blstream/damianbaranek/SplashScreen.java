package com.blstream.damianbaranek;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class SplashScreen extends ActionBarActivity {
    private static final int SPLASH_TIME = 5000;
    private int m_numBackPressed = 0;
    private Handler m_myHandler;
    private Runnable m_myRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        m_myRunnable = new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashScreen.this,MainScreen.class);
                startActivity(splashIntent);
                SplashScreen.this.finish();
            }
        };
        m_myHandler = new Handler();
        m_myHandler.postDelayed(m_myRunnable,SPLASH_TIME);
    }
    @Override
    public void onBackPressed(){
        m_numBackPressed++;
        if(m_numBackPressed >= 2)
        {
            System.exit(0);
        }
        m_myHandler.removeCallbacks(m_myRunnable);
        Toast.makeText(SplashScreen.this,R.string.onBackPressedMessage,Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
