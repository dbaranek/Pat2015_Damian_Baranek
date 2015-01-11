package com.blstream.damianbaranek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class MainScreen extends ActionBarActivity {
    protected Button exitButton, logoutButton;
    public final static String BASE_SERVER_URL = "http://192.168.1.3:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainScreen.this.finish();
                System.exit(0);
            }
        });
        logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences loginInformation = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
                SharedPreferences.Editor editLogIn = loginInformation.edit();
                editLogIn.putBoolean("isLogIn", false);
                editLogIn.apply();
                Intent mainIntent = new Intent(MainScreen.this,LoginScreen.class);
                startActivity(mainIntent);
                MainScreen.this.finish();
            }
        });
        new ObjectsDownloader(this,(ListView)findViewById(R.id.objectsList)).execute();
    }
    private class ObjectsDownloader extends AsyncTask<Void,ListView,Void> {
        protected Context activityContext;
        protected ArrayList<ListItem> listItems;
        protected JSONObject jsonResponse;
        protected JSONArray jsonArray;
        protected ListView listView;

        private ObjectsDownloader(Context activityContext, ListView listView) {
            this.activityContext = activityContext;
            this.listView = listView;
        }

        private String convertStreamToString(InputStream inputStream)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
        public String downloadJSON(String url)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response;
            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result= convertStreamToString(instream);
                    instream.close();
                    return result;
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            return null;
        }
        @Override
        protected Void doInBackground(Void... params) {
            listItems = new ArrayList<ListItem>();
            String jsonInfo = downloadJSON(BASE_SERVER_URL+"/page_0.json");
            if(jsonInfo != null) {
                try {
                    jsonResponse = new JSONObject(jsonInfo);
                    jsonArray = jsonResponse.optJSONArray("array");

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonArrayElement = jsonArray.optJSONObject(i);
                        Bitmap imageBitmap = null;
                        URL req = null;
                        try {
                            req = new URL(jsonArrayElement.optString("url"));
                            imageBitmap = BitmapFactory.decodeStream(req.openConnection().getInputStream());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //new ImageDownloader(imageBitmap).execute(jsonArrayElement.optString("url"));
                        listItems.add(new ListItem(jsonArrayElement, imageBitmap));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MyArrayAdapter adapter = new MyArrayAdapter(activityContext,R.layout.objects_list_item, listItems);
            listView.setAdapter(adapter);
            ProgressBar listProgressBar = (ProgressBar)findViewById(R.id.listProgressBar);
            listProgressBar.setVisibility(ProgressBar.GONE);
            super.onPostExecute(aVoid);
        }
        private class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
            private Bitmap imageBitmap;
            private ImageDownloader(Bitmap imageBitmap) {
                this.imageBitmap = imageBitmap;
            }
            @Override
            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                URL req = null;
                try {
                    req = new URL(urldisplay);
                    imageBitmap = BitmapFactory.decodeStream(req.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mIcon11;
            }
            protected void onPostExecute(Bitmap result) {
                imageBitmap = result;
                super.onPostExecute(result);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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
