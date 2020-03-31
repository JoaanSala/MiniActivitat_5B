package com.example.miniactivitat_5b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

class NetworkManager extends AsyncTask {

    NetworkInfo networkInfo;
    boolean wifiConnected;
    boolean mobileConnected;
    Context context;

    public NetworkManager(MainActivity mainActivity) {
        context = mainActivity;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        ConnectivityManager connectivityManager = (ConnectivityManager) objects[0];
        String finalInfo = "";

        if (connectivityManager != null) {
            this.networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (wifiConnected) {
                finalInfo = context.getString(R.string.WifiConnection);
            } else if (mobileConnected) {
                finalInfo = context.getString(R.string.MobileConnected);
            }

        } else {
            finalInfo = context.getString(R.string.NoConnection);
        }

        return finalInfo;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        String finalInfo = (String) o;
        Toast.makeText(context,finalInfo,Toast.LENGTH_SHORT).show();
    }
}
