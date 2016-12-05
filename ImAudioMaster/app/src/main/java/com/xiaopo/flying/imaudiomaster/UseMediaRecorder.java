package com.xiaopo.flying.imaudiomaster;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by snowbean on 16-12-5.
 */
public class UseMediaRecorder implements Recorder {
    private MediaRecorder mRecorder;
    private static UseMediaRecorder sInstance;
    private RecordParameter mParameter;

    private UseMediaRecorder() {
    }

    public static UseMediaRecorder getInstance() {
        if (sInstance == null) {
            synchronized (UseMediaRecorder.class) {
                if (sInstance == null) {
                    sInstance = new UseMediaRecorder();
                }
            }
        }
        return sInstance;
    }

    public UseMediaRecorder config(RecordParameter parameter) {
        this.mParameter = parameter;
        return this;
    }

    @Override
    public void start() {
        try {
            mRecorder = new MediaRecorder();
            mParameter.config(mRecorder);
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

}