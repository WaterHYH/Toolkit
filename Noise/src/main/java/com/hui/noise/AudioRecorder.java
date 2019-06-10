package com.hui.noise;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.dodoo_tech.gfal.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 将通话内容录制为音频文件并保存
 * Created by hui on 2018/12/7.
 */

public class AudioRecorder {

    public static final String TAG = "AudioRecorder";
    private STATE state = STATE.UNINIT;
    private AudioRecord audioRecord;
    private int audioType = TYPE_PCM;
    public static final int TYPE_PCM = 0;
    public static final int TYPE_WAV = 1;

    /*录音参数配置*/
    private int audioSource = MediaRecorder.AudioSource.MIC;
    public static int sampleRateInHz = 16000; //采样率,讯飞转写要求16k
//    public static int sampleRateInHz = 8000;
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;  //采样深度，讯飞转写要求16bits的pcm_s16le音频
//    public static int audioFormat = AudioFormat.ENCODING_PCM_8BIT;  //采样深度，讯飞转写要求16bits的pcm_s16le音频
    public static int channelConfig = AudioFormat.CHANNEL_IN_DEFAULT;   //声道，默认
    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private File recordFile;

    private AudioRecorder() {
        init();
    }

    public static AudioRecorder getInstance() {
        return CallRecorderHolder.instance;
    }

    public synchronized boolean startRecord(String absPath, int audioType) {
        if (!canRecord() || absPath == null) {
            return false;
        }
        recordFile = createFile(absPath);
        this.audioType = audioType;
        setState(STATE.RECORDING);
        if (audioRecord == null) {
            audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        }
        audioRecord.startRecording();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                writeDatasToFile();
            }
        });
        return true;
    }

    public boolean canRecord() {
        Boolean available = false;
        //判断麦克风是否被其他应用占用
        if (getState() == STATE.IDLE || getState() == STATE.OCCUPIED) {
            available = !isOccupied();
        }
        Log.i(TAG, "canRecord available=" + available);
        return available;
    }

    public boolean isRecording(){
        return getState() == STATE.RECORDING;
    }

    private Boolean isOccupied() {
        Boolean occupied = false;
        AudioRecord recorder = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        try {
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                occupied = true;
            }
            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                occupied = true;
            }
            recorder.stop();
        }catch (Exception e){
            e.printStackTrace();
        } finally{
            recorder.release();
            recorder = null;
        }
        return occupied;
    }

    private File createFile(String absPath) {
        File objFile = new File(absPath);
        objFile.getParentFile().mkdirs();
        if (objFile.exists()) {
            objFile.delete();
        }
        if (!objFile.exists()) {
            try {
                objFile.createNewFile();
                return objFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private byte maxVoice,minVoice;
    private void writeDatasToFile() {
        if (recordFile == null) {
            setState(STATE.IDLE);
            return;
        }
        initProccesor();
        try {
            FileOutputStream outputStream = new FileOutputStream(recordFile);
            if (audioType == TYPE_WAV) {
                //预留wav头信息空间,文件头信息长度：44个字节
                outputStream.write(new byte[44]);
            }

            byte[] audioData = new byte[bufferSizeInBytes];
            byte noiseSize = 20;
            while (getState() == STATE.RECORDING && audioRecord != null) {
                audioRecord.read(audioData, 0, bufferSizeInBytes);

                //简单的降噪处理,结合webrtc降噪会有很好的效果，但是会整体降低音量，手柄通话时建议只启用webrtc降噪
                /*for (int i = 0; i < audioData.length; i++) {
                    audioData[i] >>= 2;
                }*/
                //webrtc降噪处理
                processData(audioData);

                outputStream.write(audioData);
            }
            outputStream.close();

            if (audioType == TYPE_WAV) {
                //填写wav头文件信息
                FileInputStream inputStream = new FileInputStream(recordFile);
                //覆盖预留的文件头空间
                RandomAccessFile accessFile = new RandomAccessFile(recordFile,"rw");
                accessFile.seek(0);
                //要减去文件头的长度
                byte[] header = generateHeader(inputStream.getChannel().size()-44,sampleRateInHz,16,1);
                accessFile.write(header);
                inputStream.close();
                accessFile.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setState(STATE.IDLE);
        }
        releaseProcessor();
    }

    private byte[] generateHeader(long pcmFileLen,int sampleRate,int sampleDepth,int channel){

        //总大小，由于不包括RIFF和WAVE，所以是44 - 8 = 36，再加上PCM文件大小
        long totalDataLen = pcmFileLen + 36;
        //音频数据传送速率,采样率*通道数*采样深度/8
        long byteRate = sampleRate * channel * sampleDepth / 8;

        byte[] header = new byte[44];
        //RIFF chunk
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        //数据大小
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        //WAVE chunk
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT chunk
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        // 数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channel;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (channel * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (pcmFileLen & 0xff);
        header[41] = (byte) ((pcmFileLen >> 8) & 0xff);
        header[42] = (byte) ((pcmFileLen >> 16) & 0xff);
        header[43] = (byte) ((pcmFileLen >> 24) & 0xff);
        return header;
    }

    public STATE getState() {
        return state;
    }

    private synchronized void setState(STATE state) {
        this.state = state;
    }

    public synchronized File stopRecord() {
        if (getState() == STATE.RECORDING) {
            setState(STATE.IDLE);
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            return recordFile;
        }
        return null;
    }

    public synchronized void cancelRecord(){
        File file = stopRecord();
        if (file != null) {
            file.delete();
        }
    }

    private void init() {
        if (getState() != STATE.UNINIT) {
            return;
        }
        if (isOccupied()) {
            setState(STATE.OCCUPIED);
        }else {
            setState(STATE.IDLE);
        }
    }

    public enum STATE {
        IDLE, RECORDING, OCCUPIED, UNINIT
    }

    private static final class CallRecorderHolder {
        private static final AudioRecorder instance = new AudioRecorder();
    }

    private WebrtcProcessor mProcessor;

    /**
     * 初始化降噪
     */
    private void initProccesor() {
        try{
            mProcessor = new WebrtcProcessor();
            //当前的so库仅支持8000赫兹
            mProcessor.init(8000);
        }catch (Exception e){
            LogUtil.logError(e);
        }
    }

    /**
     * 释放降噪资源
     */
    private void releaseProcessor() {
        try{
            if (mProcessor != null) {
                mProcessor.release();
            }
        }catch (Exception e){
            LogUtil.logError(e);
        }
    }

    /**
     * 处理需要降噪的音频数据
     *
     * @param data
     */
    private void processData(byte[] data) {
        try{
            if (mProcessor != null) {
                mProcessor.processNoise(data);
            }
        }catch (Exception e){
            LogUtil.logError(e);
        }
    }

    /**
     * 处理需要降噪的音频数据
     *
     * @param data
     */
    private void processData(short[] data) {
        try{
            if (mProcessor != null) {
                mProcessor.processNoise(data);
            }
        }catch (Exception e){
            LogUtil.logError(e);
        }
    }
}
