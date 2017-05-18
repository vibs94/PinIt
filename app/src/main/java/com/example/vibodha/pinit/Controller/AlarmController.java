package com.example.vibodha.pinit.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

/**
 * Created by vibodha on 5/16/17.
 */

public class AlarmController extends Service {

    private Ringtone ringtone;
    Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); //get the url of default device ring tone
        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);  // set vibrator

        if(uri==null){
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(this,uri); // set ring tone
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};  //set vibrate pattern
        vibrator.vibrate(pattern,0);  //vibrate device

        ringtone.play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ringtone.stop();
        vibrator.cancel();
    }
}
