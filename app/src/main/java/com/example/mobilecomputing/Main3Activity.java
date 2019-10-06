package com.example.mobilecomputing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;


public class Main3Activity extends AppCompatActivity {

    private Button bRecord, bPlay, bUpload;
    private VideoView video;
    private int START_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bRecord = (Button) findViewById(R.id.rbutton);
        bPlay = (Button) findViewById(R.id.pbutton);
        bUpload = (Button) findViewById(R.id.ubutton);
        video = (VideoView) findViewById(R.id.videoView);

        bRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordVideo = new Intent();
                recordVideo.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                //recordVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                //recordVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                //recordVideo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(recordVideo, START_CAMERA);
            }
        });

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.start();
            }
        });

    }

    protected void uriResult(int request, int result, Intent data){
        if( request == START_CAMERA && result == RESULT_OK){
            Uri vUri = data.getData();
            data.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            data.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
            video.setVideoURI(vUri);
        }
    }

}
