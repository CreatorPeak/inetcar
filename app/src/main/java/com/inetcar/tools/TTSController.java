package com.inetcar.tools;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.inetcar.startup.R;

/**
 * 语音播报
 */
public class TTSController{

    public static TTSController ttsManager;
    boolean isfinish = true;
    private Context mContext;
    // 合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;

    TTSController(Context context) {
        mContext = context;
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void init() {
        // 初始化合成对象.
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext,mInitListener);
        initSpeechSynthesizer();
    }

    //初始化单例对象时，通过此回调接口，获取初始化状态
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int errorCode) {
            if(errorCode== ErrorCode.SUCCESS)
                Log.d("mapview", "初始化语音对象成功");
            else
                Log.d("mapview", "初始化语音对象失败："+errorCode);
        }
    };

    /**
     * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
     * @param playText 文字
     */
    public void playText(String playText) {
        if (!isfinish) {
            return;
        }
        if (null == mSpeechSynthesizer) {
            // 创建合成对象.
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext,mInitListener);
            initSpeechSynthesizer();
        }
        // 进行语音合成.
        int code = mSpeechSynthesizer.startSpeaking(playText, mSynthesizerListener);
        Log.d("mapview", "语音合成结果："+code);
    }

    /**
     * 语音合成接口
     */
    private SynthesizerListener mSynthesizerListener = new SynthesizerListener(){

        @Override
        public void onSpeakBegin() {
            isfinish = false;
            Log.d("mapview", "开始说话");
        }

        @Override
        public void onSpeakPaused() { }

        @Override
        public void onSpeakResumed() { }

        /**
         * @param percent  播放进度0~100
         * @param beginPos 播放音频在文本中开始位置
         * @param endPos   播放音频在文本中结束位置
         */
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            isfinish = true;
            Log.d("mapview", "说话完成");
        }

        /**
         * @param percent   缓冲进度0~100
         * @param beginPos  缓冲音频在文本中开始位置
         * @param endPos    缓冲音频在文本中结束位置
         * @param info      附加信息
         */
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    public void stopSpeaking() {
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.stopSpeaking();
    }

    public void startSpeaking() {
        isfinish = true;
    }

    /**
     * 初始化发音设置
     */
    private void initSpeechSynthesizer() {
        // 设置发音人
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
                mContext.getString(R.string.preference_default_tts_role));
        // 设置语速
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
                "" + mContext.getString(R.string.preference_key_tts_speed));
        // 设置音量
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,
                "" + mContext.getString(R.string.preference_key_tts_volume));
        // 设置语调
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH,
                "" + mContext.getString(R.string.preference_key_tts_pitch));

    }

    public void destroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer.destroy();
        }
    }
}
