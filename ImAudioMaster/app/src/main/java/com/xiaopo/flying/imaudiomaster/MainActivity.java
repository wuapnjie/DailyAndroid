package com.xiaopo.flying.imaudiomaster;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String mOutput;
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    110);
        } else {
            mOutput = FileUtils.getNewFile(this, "ImAudioMaster").getAbsolutePath();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mOutput = FileUtils.getNewFile(this, "ImAudioMaster").getAbsolutePath();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void startRecord(View view) {
        RecordParameter parameter = RecordParameter.createDefault(mOutput);
        parameter.outputFormat = MediaRecorder.OutputFormat.THREE_GPP;
        UseMediaRecorder
                .getInstance()
                .config(parameter)
                .start();
    }

    public void stopRecord(View view) {
        UseMediaRecorder
                .getInstance()
                .stop();
    }

    public void playRecord(View view) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mOutput);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopPlay(View view) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
