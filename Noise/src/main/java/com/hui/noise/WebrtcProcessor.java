package com.hui.noise;

import android.util.Log;


/**
 * Desc:
 */
public class WebrtcProcessor {
    static {
        try {
            //加载降噪库
            System.loadLibrary("webrtc");
        } catch (UnsatisfiedLinkError e) {
            Log.e("TAG", e.getMessage());
        }

    }

    /**
     * 处理降噪
     *
     * @param data
     */
    public void processNoise(byte[] data) {
        if (data == null) return;
        int newDataLength = data.length / 2;
        if (data.length % 2 == 1) {
            newDataLength += 1;
        }
        //此处是将字节数据转换为short数据
        short[] newData = new short[newDataLength];
        for (int i = 0; i < newDataLength; i++) {
            byte low = 0;
            byte high = 0;
            if (2 * i < data.length) {
                low = data[2 * i];
            }
            if ((2 * i + 1) < data.length) {
                high = data[2 * i + 1];
            }
            newData[i] = (short) (((high << 8) & 0xff00) | (low & 0x00ff));
        }

        // 交给底层处理
        processNoise(newData);
        //处理完之后, 又将short数据转换为字节数据
        for (int i = 0; i < newDataLength; i++) {
            if (2 * i < data.length) {
                data[2 * i] = (byte) (newData[i] & 0xff);
            }
            if ((2 * i + 1) < data.length) {
                data[2 * i + 1] = (byte) ((newData[i] >> 8) & 0xff);
            }
        }

    }

    /**
     * 初始化降噪设置，当前的so库仅支持8000赫兹
     *
     * @param sampleRate 采样率
     * @return 是否初始化成功
     */
    public native boolean init(int sampleRate);

    /**
     * 处理降噪
     *
     * @param data
     * @return
     */
    public native boolean processNoise(short[] data);

    /**
     * 释放降噪资源
     */
    public native void release();
}
