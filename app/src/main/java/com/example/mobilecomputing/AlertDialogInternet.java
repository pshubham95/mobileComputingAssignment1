package com.example.mobilecomputing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class AlertDialogInternet extends Activity {
    AlertDialog AlertDialog;
    public void finish() {
        super.finish();
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("finishing");
            AlertDialog.dismiss();
            finish();

        }
    };

    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterNetworkChanges();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();

        filter.addAction("com.hello.action");
        registerReceiver(receiver, filter);
        try {
            AlertDialog = new AlertDialog.Builder(this).create();
            AlertDialog.setCancelable(false);
            AlertDialog.setCanceledOnTouchOutside(false);
            Toast.makeText(getApplicationContext(), "Internet Connection is required to use this app",Toast.LENGTH_LONG ).show();
            AlertDialog.setTitle("Info");
            AlertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            AlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            /*AlertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });*/

            AlertDialog.show();
        } catch (Exception e) {
        }
    }
}
