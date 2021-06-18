package com.app.codeweb.codeweb.Others;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.app.codeweb.codeweb.R;

/**
 * Created by acer on 5/5/2017.
 */

public class SoundPlayer {
    static SoundPool pool;
    static int check;
    static int wrong;

    public SoundPlayer(Context c){
        pool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);

        check = pool.load(c, R.raw.correct_ef,1);
        wrong = pool.load(c, R.raw.wrong_eff,1);

    }

    public void playCheck(){
        pool.play(check,1.0f,1.0f,1,0,1.0f);
    }
    public void playWrong(){
        pool.play(wrong,1.0f,1.0f,1,0,1.0f);
    }

}
