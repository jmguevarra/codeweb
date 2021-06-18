package com.app.codeweb.codeweb.Others;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.app.codeweb.codeweb.R;

import static java.lang.System.exit;


public class Connection extends ContextWrapper{
    public Connection(Context base) {
        super(base);
    }

    public boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(wifi != null && wifi.isConnectedOrConnecting()){
                return true;
            }else{
                return false;
            }

        } else {
            return false;
        }
    }

    public boolean isMobileConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(mobile != null && mobile.isConnectedOrConnecting()) {
                return true;
            }else{
                return false;
            }

        } else {
            return false;
        }
    }

    public AlertDialog.Builder ConnectionDialog(final Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setCancelable(false);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_no_net));
        builder.setMessage("Please Check Your Connection.");

        return builder;
    }
}
