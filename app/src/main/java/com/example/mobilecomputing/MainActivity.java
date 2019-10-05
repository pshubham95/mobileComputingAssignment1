package com.example.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    HashMap<String, String> values = new HashMap<>();
    List<String> gestureNames = new ArrayList<>();
    List<String> gestureActions = new ArrayList<>();
    private BroadcastReceiver networkReciever;
    String selectedGesture = "";
    String selectedGestureUrl = "";
    private void fillValues() {
        gestureNames = Arrays.asList(getResources().getStringArray(R.array.actions_array));
        gestureActions = Arrays.asList(getResources().getStringArray(R.array.actions_url));
        for(int i = 0; i < gestureNames.size(); i++) {
            System.out.println(gestureNames.get(i));
            values.put(gestureNames.get(i), gestureActions.get(i));
        }

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(MainActivity.this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner gestureSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.actions_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spin_item);
// Apply the adapter to the spinner
        gestureSpinner.setAdapter(adapter);
        gestureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
                selectedGesture = gestureNames.get(i);
                selectedGestureUrl = gestureActions.get(i);
                System.out.println(gestureNames.get(i));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });
        fillValues();
        networkReciever = new NetworkChangeReciever();
        registerNetworkBroadcast();
        /*if (!isOnline()) {
            //do whatever you want to do
            try {
                AlertDialog AlertDialog = new AlertDialog.Builder(MainActivity.this).create();

                AlertDialog.setTitle("Info");
                AlertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                AlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

                AlertDialog.show();
            } catch (Exception e) {
            }
        }*/
    }

    public void navigate(View view)
    {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("gestureName", selectedGesture);
        intent.putExtra("gestureUrl", selectedGestureUrl);
        startActivity(intent);
    }

    private void registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReciever, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkReciever, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReciever);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }


}
