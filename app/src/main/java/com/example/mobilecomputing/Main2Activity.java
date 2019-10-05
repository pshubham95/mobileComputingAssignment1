package com.example.mobilecomputing;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Main2Activity extends AppCompatActivity {
    String videoPath = "";
    String gestureName = "";
    String gestureUrl = "";
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public String downloadVideo() {
        try {
            URL url = new URL(gestureUrl);

            //create the new connection
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            //set up some things on the connection
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //and connect!
            urlConnection.connect();

            //set the path where we want to save the file
            //in this case, going to save it on the root directory of the
            //sd card.
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.
            File file = new File(SDCardRoot, gestureName+".mp4");
            System.out.println(SDCardRoot+"/"+gestureName+".mp4");

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            //this will be used in reading the data from the internet
            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file
            int totalSize = urlConnection.getContentLength();
            //variable to store total downloaded bytes
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the prgress, like this maybe
                //updateProgress(downloadedSize, totalSize);

            }
            //close the output stream when done
            fileOutput.close();
            return SDCardRoot+"/"+gestureName+".mp4";
//catch some possible errors...
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public void playAgain(View view) {
        VideoView videoView = findViewById(R.id.videoView5);
        System.out.println("videopath:"+videoPath);
        videoView.setVideoPath(videoPath);
        videoView.start();
    }

    public void startDownload() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    videoPath = downloadVideo();
                    Main2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoView videoView = findViewById(R.id.videoView5);
                            System.out.println("videopath:"+videoPath);
                            videoView.setVideoPath(videoPath);
                            videoView.start();
                        }
                    });

                    //Your code goes here
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startDownload();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            startDownload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gestureName = extras.getString("gestureName");
            TextView gestureNameText = (TextView)findViewById(R.id.textView3);
            gestureNameText.setText("Gesture: "+gestureName);
            gestureUrl = extras.getString("gestureUrl");
            System.out.println(gestureName+"here"+gestureName);
            requestPermission();
            //The key argument here must match that used in the other activity
        }
    }

}