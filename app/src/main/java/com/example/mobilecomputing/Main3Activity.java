package com.example.mobilecomputing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.FileUtils;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;


public class Main3Activity extends AppCompatActivity {

    private Button bRecord, bPlay, bUpload;
    private VideoView video;
    private int START_CAMERA = 0;
    private int praticeNum = 1;
    String gestureName = "", lastName = "", asuId = "", groupNumber = "10";
    String serverUrl = "http://34.66.8.146/video/upload_video.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "/MobileComputing/PraticeVideos/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gestureName = extras.getString("gestureName");
        }

        bRecord = (Button) findViewById(R.id.rbutton);
        bPlay = (Button) findViewById(R.id.pbutton);
        bUpload = (Button) findViewById(R.id.ubutton);
        if(praticeNum == 1) {
            bPlay.setEnabled(false);
        }
        video = findViewById(R.id.rvideoView);
        TextView heading = findViewById(R.id.textView4);
        heading.setText("Gesture: "+gestureName);

        bRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordVideo = new Intent();
                recordVideo.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                recordVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                recordVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                File SDCardRoot = Environment.getExternalStorageDirectory();
                String fileName = SDCardRoot+"/MobileComputing/PraticeVideos/"+gestureName.toUpperCase()+"_PRATICE_("+praticeNum+")_"+lastName.toUpperCase()+".mp4";
                Uri videoUri = Uri.fromFile(new File(fileName));
                recordVideo.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

                startActivityForResult(recordVideo, START_CAMERA);
            }
        });
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(Main3Activity.this); // this = YourActivity
                System.out.println("set dialog");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setTitle("Uploading");
                dialog.setMessage("Uploading video to server. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            int bytesRead, bytesAvailable, bufferSize;
                            String lineEnd = "\r\n";
                            String twoHyphens = "--";
                            String boundary = "*****";
                            byte[] buffer;
                            int maxBufferSize = 20 * 1024 * 1024;
                            File SDCardRoot = Environment.getExternalStorageDirectory();

                            int temp = praticeNum - 1;
                            // open a URL connection to the Servlet
                            String fileName = SDCardRoot+"/MobileComputing/PraticeVideos/"+gestureName.toUpperCase()+"_PRATICE_("+temp+")_"+lastName.toUpperCase()+".mp4";
                            System.out.println("File"+fileName);
                            File sourceFile = new File(fileName);
                            if (!sourceFile.isFile()) {
                                Toast.makeText(Main3Activity.this, "Recording does not exist, please try again", Toast.LENGTH_LONG).show();
                                System.out.println("file des not ");
                                return;
                            }
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(serverUrl);
                            System.out.println("Uploading1");

                            // Open a HTTP  connection to  the URL
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("uploaded_file", fileName);
                            conn.setRequestProperty("group_id", groupNumber);
                            conn.setRequestProperty("id", asuId);
                            System.out.println("bhbhbhb");
                            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                            System.out.println("knjnjjn");
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+
                                    fileName + '\"' + lineEnd);

                            dos.writeBytes(lineEnd);

                            // create a buffer of  maximum size
                            bytesAvailable = fileInputStream.available();
                            System.out.println("bytesAvail"+bytesAvailable);
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            System.out.println("bytesAvail"+bytesRead);

                            while (bytesRead > 0) {

                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            }

                            // send multipart form data necesssary after file data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            System.out.println("Uploading");
                            // Responses from the server (code and message)
                            int serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();

                            Log.i("uploadFile", "HTTP Response is : "
                                    + serverResponseMessage + ": " + serverResponseCode);

                            System.out.println("serverResponseMessage  - "+serverResponseMessage);
                            if(serverResponseCode == 200){

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(Main3Activity.this, "File upload completed successfully!", Toast.LENGTH_LONG).show();
                                        System.out.println("Success");
                                    }
                                });
                            }

                            //close the streams //
                            fileInputStream.close();
                            dos.flush();
                            dos.close();

                        }  catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(Main3Activity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                            System.out.println("here in exception");


                        }
                    }
                }).start();


            }
        });

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.start();
                video.resolveAdjustedSize(800, 800);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent res){
        super.onActivityResult(requestCode, resultCode, res);
        System.out.println("res"+resultCode+","+requestCode);
        if(resultCode == -1) {
            TextView praticeNumText = findViewById(R.id.textView5);
            praticeNumText.setText("Pratice Number "+praticeNum);
            File SDCardRoot = Environment.getExternalStorageDirectory();
            video.setVideoPath(SDCardRoot+"/MobileComputing/PraticeVideos/"+gestureName.toUpperCase()+"_PRATICE_("+praticeNum+")_"+lastName.toUpperCase()+".mp4");
            praticeNum++;
            bPlay.setEnabled(true);
        }

    }
    protected void uriResult(int request, int result, Intent data){
        if( request == START_CAMERA && result == RESULT_OK){
            Uri vUri = data.getData();
            //data.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            //data.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
            video.setVideoURI(vUri);
        }
    }

}
