package com.hui.toolkit;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.dodoo_tech.gfal.utils.LogUtil;
import com.hui.noise.AudioRecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Created by waterHYH on 2019/6/10.
 */
public class TestNoiseActivity extends Activity {

    private static final String TAG = "TestNoiseActivity";

    private static boolean cutNoise = false;
    private int audioSource = cutNoise? MediaRecorder.AudioSource.VOICE_COMMUNICATION:MediaRecorder.AudioSource.MIC;
    public static int sampleRateInHz = 16000; //采样率,讯飞转写要求16k
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;  //采样深度，讯飞转写要求16bits的pcm_s16le音频
    public static int channelConfig = AudioFormat.CHANNEL_IN_MONO;   //声道，默认
    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.setDebugMode(true);
        setContentView(R.layout.activity_test_noise);

        findViewById(R.id.recorder1_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (cutNoise) {
                        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                        audioManager.setSpeakerphoneOn(true);
                    }
                    boolean b = AudioRecorder.getInstance().startRecord(Environment.getExternalStorageDirectory().getAbsolutePath()+"/testRecord_"+System.currentTimeMillis()+".wav",AudioRecorder.TYPE_WAV);
                    addLog(TAG+"->start record1 "+b);
                }catch (Exception e){
                    LogUtil.logError(e);
                }
            }
        });
        findViewById(R.id.recorder1_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = AudioRecorder.getInstance().stopRecord();
                addLog(TAG+"->stop record1 file="+(file==null? "null" : file.getAbsolutePath()));
            }
        });

        findViewById(R.id.recorder2_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog(TAG+"->NoiseSuppressor "+ NoiseSuppressor.isAvailable());
                addLog(TAG+"->AcousticEchoCanceler "+ AcousticEchoCanceler.isAvailable());
                addLog(TAG+"->AutomaticGainControl "+ AutomaticGainControl.isAvailable());
            }
        });
    }

    private String logText = "";
    private TextView logView;
    private void addLog(String log) {
        try{
            logText += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))+" "+log+"\n";
        }catch (Exception e){
            LogUtil.logError(e);
        }
        if (logView == null) {
            logView = findViewById(R.id.log_view);
        }
        logView.setText(logText);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
