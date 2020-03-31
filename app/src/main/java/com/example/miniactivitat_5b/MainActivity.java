package com.example.miniactivitat_5b;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    private static final int TIMEOUT = 3000;
    private Button b_DownloadWeb;
    private Button b_DownloadImage;
    private TextView tv_WebContent;
    private ImageView iv_ImageContent;

    ConnectivityManager connectivityManager;
    NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_WebContent = findViewById(R.id.tv_WebContent);
        iv_ImageContent = findViewById(R.id.iv_ImageContent);
        b_DownloadImage = findViewById(R.id.b_DownloadImage);
        b_DownloadWeb = findViewById(R.id.b_DownloadWeb);

        this.connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkManager = new NetworkManager(this);
        this.networkManager.execute(this.connectivityManager);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.networkManager.cancel(true);
    }

    public void downloadWebPage(View view) throws MalformedURLException {
        downloadWeb(new URL("https://www.as.com/"));
    }

    private void downloadWeb(final URL url) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                InputStream stream = null;
                HttpsURLConnection connection = null;

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                try {
                    connection = (HttpsURLConnection) url.openConnection();

                    connection.setReadTimeout(TIMEOUT);
                    connection.setConnectTimeout(TIMEOUT);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }

                    // Retrieve the response body as an InputStream.
                    stream = connection.getInputStream();
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        tv_WebContent.setText(readStream(stream));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Close Stream and disconnect HTTPS connection.
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        };
        handler.post(runnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readStream(InputStream stream) throws IOException {

        Reader reader = null;
        reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader, 1000);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str);
        }

        return sb.toString();
    }


    public void downloadImage(View view) throws MalformedURLException {
        downloadImageWeb(new URL("https://upload.wikimedia.org/wikipedia/ca/thumb/f/f5/FC_Barcelona_escut.png/1200px-FC_Barcelona_escut.png"));
    }

    private void downloadImageWeb(final URL url) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                InputStream stream = null;
                HttpsURLConnection connection = null;

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                try {
                    connection = (HttpsURLConnection) url.openConnection();

                    connection.setReadTimeout(TIMEOUT);
                    connection.setConnectTimeout(TIMEOUT);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }

                    // Retrieve the response body as an InputStream.
                    stream = connection.getInputStream();
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        iv_ImageContent.setImageBitmap(BitmapFactory.decodeStream(stream));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Close Stream and disconnect HTTPS connection.
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        };
        handler.post(runnable);
    }
}
