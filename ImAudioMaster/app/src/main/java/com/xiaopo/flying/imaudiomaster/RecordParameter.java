package com.xiaopo.flying.imaudiomaster;

import android.media.MediaRecorder;

/**
 * Created by snowbean on 16-12-5.
 */
public class RecordParameter {

    public static final int AUDIO_CHANNEL_MONO = 1;
    public static final int AUDIO_CHANNEL_STEREO = 2;

    private RecordParameter() {
    }

    public int audioChannels = AUDIO_CHANNEL_MONO;
    public int audioSource = MediaRecorder.AudioSource.DEFAULT;
    public int audioEncoder = MediaRecorder.AudioEncoder.DEFAULT;
    public int outputFormat = MediaRecorder.OutputFormat.DEFAULT;
    public String outputFile;

    public void config(MediaRecorder mediaRecorder) {
        if (mediaRecorder != null) {
            mediaRecorder.setAudioSource(audioSource);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.setOutputFormat(outputFormat);
            mediaRecorder.setAudioChannels(audioChannels);
            mediaRecorder.setAudioEncoder(audioEncoder);
        }
    }

    public static RecordParameter createDefault(String path) {
        RecordParameter parameter = new RecordParameter();
        parameter.outputFile = path;
        return parameter;
    }
}
